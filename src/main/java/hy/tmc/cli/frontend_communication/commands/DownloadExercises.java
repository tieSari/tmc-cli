/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.frontend_communication.commands;

import hy.tmc.cli.backendCommunication.ExerciseDownloader;
import hy.tmc.cli.backendCommunication.JSONParser;
import hy.tmc.cli.domain.Exercise;
import hy.tmc.cli.frontend_communication.FrontendListener;
import hy.tmc.cli.frontend_communication.Server.ProtocolException;
import hy.tmc.cli.logic.Logic;

import java.util.List;

public class DownloadExercises extends Command {
    /**
     * ExerciseDownloader that is used for downloading
     */
    private ExerciseDownloader exDl;
    
    public DownloadExercises(FrontendListener front, Logic backend) {
        super(front, backend);
        this.exDl = new ExerciseDownloader(front);
    }
    /**
     * Parses the course JSON and executes downloading of the course exercises
     */
    @Override
    protected void functionality() {
        List<Exercise> exercises = JSONParser.getExercises(Integer.parseInt(this.data.get("courseID")));
        exDl.downloadFiles(exercises, this.data.get("pwd"));
    }
    /**
     * Checks that command has required parameters
     * courseID is the id of the course and pwd is the path of where files are downloaded and extracted.
     * @throws ProtocolException
     */
    @Override
    public void checkData() throws ProtocolException {
        checkCourseId();
        if(!this.data.containsKey("pwd")){
            throw new ProtocolException("Pwd required");
        }
    }
    
    private void checkCourseId() throws ProtocolException {
        if(!this.data.containsKey("courseID")){
            throw new ProtocolException("Course ID required");
        }
        try {
            int courseID = Integer.parseInt(this.data.get("courseID"));
        } catch (NumberFormatException e){
            throw new ProtocolException("Given course id is not a number");
        }
    }
}
