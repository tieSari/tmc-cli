package hy.tmc.cli.domain;

import com.google.gson.annotations.SerializedName;

public class Course {
    
    private int id;
    private String name;

    @SerializedName("details_url")
    private String detailsUrl;

    public Course() {

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
