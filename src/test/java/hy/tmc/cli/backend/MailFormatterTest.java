package hy.tmc.cli.backend;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.testhelpers.MailExample;

import org.junit.Test;

public class MailFormatterTest {

    @Test
    public void formatReviewsFormatsCorrectly() throws Exception {
        String output = MailFormatter.formatReviews(MailExample.reviewExample());
        assertTrue(output.contains("\n"));
        assertFalse(output.contains("["));
        assertFalse(output.contains("]"));
    }


} 
