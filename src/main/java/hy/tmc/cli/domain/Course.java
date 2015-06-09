package hy.tmc.cli.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {
    
    private int id;
    private String name;

    private List<Exercise> exercises;

    @SerializedName("details_url")
    private String detailsUrl;

    public String getReviewsUrl() {
        return reviewsUrl;
    }

    public void setReviewsUrl(String reviewsUrl) {
        this.reviewsUrl = reviewsUrl;
    }
    
    @SerializedName("spyware_urls")
    private List<String> spywareUrls;

    @SerializedName("reviews_url")
    private String reviewsUrl;
    
    public Course() {

    }

    public List<String> getSpywareUrls() {
        return spywareUrls;
    }

    public void setSpywareUrls(List<String> spywareUrls) {
        this.spywareUrls = spywareUrls;
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
