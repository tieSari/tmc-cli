package hy.tmc.cli.domain;

import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class ReviewTest {

    private Review review;

    @Before
    public void setUp() {
        review = new Review();
        review.setId(1);
        review.setCreatedAt("10.6.2015");
        review.setExerciseName("Nimi01");
        review.setMarkedAsRead(false);
        String[] points = new String[]{"1.1", "1.2"};
        String[] pointsNot = new String[]{"1.3", "1.4"};
        review.setPoints(new ArrayList<>(Arrays.asList(points)));
        review.setPointsNotAwarded(new ArrayList<>(Arrays.asList(pointsNot)));
        review.setReviewBody("Nice coding!");
        review.setReviewerName("Samu");
        review.setSubmissionId(2);
        review.setUpdateUrl("http://localhost:8080/update");
        review.setUpdatedAt("09.6.2015");
        review.setUrl("http://localhost:8080/url");
    }

    @Test
    public void testGetSubmissionId() {
        assertEquals(2, review.getSubmissionId());
    }

    @Test
    public void testGetExerciseName() {
        assertEquals("Nimi01", review.getExerciseName());
    }

    @Test
    public void testGetId() {
        assertEquals(1, review.getId());
    }

    @Test
    public void testIsMarkedAsRead() {
        assertEquals(false, review.isMarkedAsRead());
    }


    @Test
    public void testGetReviewerName() {
        assertEquals("Samu", review.getReviewerName());
    }
    
    @Test
    public void testGetReviewBody() {
        assertEquals("Nice coding!", review.getReviewBody());
    }


    @Test
    public void testGetPoints() {
        assertEquals("1.1", review.getPoints().get(0));
    }

    @Test
    public void testGetPointsNotAwarded() {
        assertEquals("1.3", review.getPointsNotAwarded().get(0));
    }


    @Test
    public void testGetUrl() {
        assertEquals("http://localhost:8080/url", review.getUrl());
    }


    @Test
    public void testGetUpdateUrl() {
        assertEquals("http://localhost:8080/update", review.getUpdateUrl());
    }


    @Test
    public void testGetCreatedAt() {
        assertEquals("10.6.2015", review.getCreatedAt());
    }


    @Test
    public void testGetUpdatedAt() {
    }


    @Test
    public void testMarkAs() {
        // TODO mock
    }

}
