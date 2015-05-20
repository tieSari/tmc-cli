/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backend_communication;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.Configuration.ConfigHandler;
import hy.tmc.cli.backendCommunication.ExerciseDownloader;
import hy.tmc.cli.backendCommunication.HTTPResult;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.backendCommunication.URLCommunicator;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.testhelpers.ExampleJSON;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(JSONParser.class)
public class ExerciseDownloaderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    public ExerciseDownloaderTest() {
    }
    
    @Before
    public void setup() {


        HTTPResult fakeResult = new HTTPResult(ExampleJSON.courseExample, 200, true);
        
        ArrayList<Exercise> exercises = new ArrayList<>();
        
        Exercise e1 = new Exercise();
        e1.setZip_url("http://127.0.0.1:8080/ex1.zip");
        e1.setName("Exercise1");
        exercises.add(e1);

        wireMockRule.start();

        wireMockRule.stubFor(get(urlEqualTo("/ex1.zip"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 1</response>")));

        Exercise e2 = new Exercise();
        e2.setZip_url("http://127.0.0.1:8080/ex2.zip");
        e2.setName("Exercise2");
        exercises.add(e2);

        wireMockRule.stubFor(get(urlEqualTo("/ex2.zip"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 2</response>")));
        
        ClientData.setUserData("pihla", "juuh");
        PowerMockito.mockStatic(JSONParser.class);
        PowerMockito
                .when(JSONParser.getExercises(Mockito.anyString()))
                .thenReturn(exercises);
        
    }

    @After
    public void after() {
        wireMockRule.stop();
        // hammertime
    }
    
    @Test
    public void downloadExercise(){
        ConfigHandler confighandler = new ConfigHandler();
        String courseUrl = confighandler.getCourseUrl(1);
        System.out.println(courseUrl);
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        System.out.println(exercises);
        System.out.println(exercises.get(0));
        ExerciseDownloader.downloadFiles(exercises);

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex1.zip")));
        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex2.zip")));
    }
    
}
