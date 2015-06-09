/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hy.tmc.cli.domain;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.hamcrest.CoreMatchers.not;

import com.google.common.base.Optional;

import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.eq;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UrlCommunicator.class)
public class ReviewTest {

    private final String updateUrl = "http://test.mooc.duck.fi/courses/47/reviews/8";
    private final String putUrl = this.updateUrl + ".json?api_version=7";
    private Review review;
    
    public ReviewTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        review = new Review();
        review.setUpdateUrl(updateUrl);
        
        
        PowerMockito.mockStatic(UrlCommunicator.class);
        PowerMockito
                .when(UrlCommunicator.makePutRequest(argThat(not(putUrl)), any(Optional.class)))
                .thenReturn(new HttpResult("", 400, false));
        PowerMockito
                .when(UrlCommunicator.makePutRequest(eq(putUrl), any(Optional.class)))
                .thenReturn(new HttpResult(
                        "{\"status\":OK}", 200, true
                ));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void toStringTest() {
        Review r = new Review();
        r.setExerciseName("viikko1_tehtava007");
        r.setReviewerName("ilari");
        r.setReviewBody("ihan hyvä, muista sisennys!");
        String expected = "viikko1_tehtava007 reviewed by ilari:\nihan hyvä, muista sisennys!";
        assertEquals(expected, r.toString());
    }

    @Test
    public void markAsReadTest() {
        review.setMarkedAsRead(false);
        review.markAs(true);
        assertTrue(review.isMarkedAsRead());
    }

    @Test
    public void markAsUnreadTest() {
        review.setMarkedAsRead(true);
        review.markAs(false);
        assertFalse(review.isMarkedAsRead());
    }
    
    //TODO: verify review sends good data
}
