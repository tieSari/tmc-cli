/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.backend_communication;

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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

/**
 *
 * @author pihla
 */
public class ExerciseDownloaderTest {
    
    public ExerciseDownloaderTest() {
    }
    
    @Before
    public void setup() {
        PowerMockito.mockStatic(URLCommunicator.class);

        HTTPResult fakeResult = new HTTPResult(ExampleJSON.courseExample, 200, true);
        
        ArrayList<Exercise> exercises = new ArrayList<>();
        
        Exercise e1 = new Exercise();
        e1.setZip_url("http://lol.fi/ex1.zip");
        exercises.add(e1);
        
        Exercise e2 = new Exercise();
        e2.setZip_url("http://lol.fi/ex2.zip");
        exercises.add(e2);
        
        ClientData.setUserData("pihla", "juuh");
        PowerMockito
                .when(JSONParser.getExercises(Mockito.anyString()))
                .thenReturn(exercises);
        
    }
    
    @Test
    public void downloadExercise(){
        ConfigHandler confighandler = new ConfigHandler();
        String courseUrl = confighandler.getCourseUrl(1);
        System.out.println(courseUrl);
        List<Exercise> exercises = JSONParser.getExercises(courseUrl);
        ExerciseDownloader.downloadFiles(exercises);
    }
    
}
