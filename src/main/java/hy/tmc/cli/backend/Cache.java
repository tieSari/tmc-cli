package hy.tmc.cli.backend;

import hy.tmc.cli.domain.Course;
import hy.tmc.cli.domain.Review;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cache {
    
    private static Map<Integer, Course> courses;
    private static Date lastUpdated;
    
    public static void update(Map<Integer, Course> freshData){
        lastUpdated = new Date();
    }
    
    public static Date getLastUpdated() {
        return lastUpdated;
    }
    
    public static void clear() {
        courses = new HashMap<>();
    }
    
    public static void loadFromDatabase() {
        // TODO: implement
    }
    
    public static void backupToDatabase() {
        // TODO: implement
    }
}
    
