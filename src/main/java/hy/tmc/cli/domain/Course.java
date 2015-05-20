package hy.tmc.cli.domain;

public class Course {
    
    private int id;
    private String name;
    private String details_url;

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public String getDetails_url() {
        return details_url;
    }

    public void setDetails_url(String details_url) {
        this.details_url = details_url;
    }
}
