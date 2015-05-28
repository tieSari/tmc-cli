package hy.tmc.cli.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {
    
    private int id;
    private String name;

    private List<Exercise> exercises;

    @SerializedName("details_url")
    private String detailsUrl;

    public Course() {

    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
