/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backend_communication;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hy.tmc.cli.Configuration.ClientData;
import hy.tmc.cli.backendCommunication.ExerciseDownloader;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.testhelpers.FrontendStub;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ExerciseDownloaderTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private ArrayList<Exercise> exercises;

    private ExerciseDownloader exDl;
    private FrontendStub front;

    public ExerciseDownloaderTest() {
    }
    
    @Before
    public void setup() {

        front = new FrontendStub();
        exDl = new ExerciseDownloader(front);
        exercises = new ArrayList<>();

        Exercise e1 = new Exercise();
        e1.setZip_url("http://127.0.0.1:8080/ex1.zip");
        e1.setName("Exercise1");
        exercises.add(e1);

        Exercise e2 = new Exercise();
        e2.setZip_url("http://127.0.0.1:8080/ex2.zip");
        e2.setName("Exercise2");
        exercises.add(e2);

        stubFor(get(urlEqualTo("/ex1.zip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 1</response>")));

        wireMockRule.stubFor(get(urlEqualTo("/ex2.zip"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Exercise 2</response>")));
        
        ClientData.setUserData("pihla", "juuh");
        
    }

    @After
    public void remove() {
        new File("Exercise1.zip").delete();
        new File("Exercise2.zip").delete();
    }
    


    @Test
    public void downloadExercisesDoesRequests() {
        exDl.downloadFiles(exercises);

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex1.zip")));
        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex2.zip")));
    }

    @Test
    public void requestsHaveAuth() {
        exDl.downloadFiles(exercises);

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex1.zip"))
                .withHeader("Authorization", equalTo("Basic cGlobGE6anV1aA==")));

        wireMockRule.verify(getRequestedFor(urlEqualTo("/ex2.zip"))
                .withHeader("Authorization",equalTo("Basic cGlobGE6anV1aA==")));
    }

    @Test
    public void downloadingGivesOutput() {
        exDl.downloadFiles(exercises);

        assertTrue(front.getMostRecentLine().endsWith(" exercises downloaded."));
    }

    @Test
    public void downloadedExercisesExists(){


        exDl.downloadFiles(exercises);

        File exercise1 = new File("Exercise1.zip");
        assertTrue("File Exercise1 was not downloaded to the fs", exercise1.exists());
        File exercise2 = new File("Exercise2.zip");
        assertTrue("File Exercise2 was not downloaded to the fs", exercise2.exists());
    }

    @Test
    public void downloadedExercisesHasContent(){


        exDl.downloadFiles(exercises);

        String ex1content;
        try {
            ex1content = new String(Files.readAllBytes(Paths.get("Exercise1.zip")));
        } catch (IOException e) {
            e.printStackTrace();
            ex1content = "";
        }

        String ex2content;
        try {
            ex2content = new String(Files.readAllBytes(Paths.get("Exercise2.zip")));
        } catch (IOException e) {
            e.printStackTrace();
            ex2content = "";
        }

        assertEquals("<response>Exercise 1</response>", ex1content);
        assertEquals("<response>Exercise 2</response>", ex2content);
    }
    
}
