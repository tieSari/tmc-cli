package hy.tmc.cli.frontend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import hy.tmc.cli.testhelpers.FeedbackBuilder;
import hy.tmc.cli.testhelpers.FrontendStub;

import org.junit.Before;
import org.junit.Test;

public class FeedbackHandlerTest {

    private FrontendStub frontend;
    private FeedbackHandler handler;
    private FeedbackBuilder builder;

    /**
     * Make the frontend, feedbackHandler and FeedbackBuilder.
     */
    @Before
    public void setUp() throws Exception {
        this.frontend = new FrontendStub();
        this.handler = new FeedbackHandler(frontend);
        this.builder = new FeedbackBuilder();
    }

    @Test
    public void answerOneQuestion() {
        builder.withSimpleTextQuestion();
        handler.feedback(builder.build(), "");
        assertTrue(frontend.getAllLines().contains("hello world"));
    }

    @Test
    public void answerManyQuestions() {
        builder.withSimpleTextQuestion()
               .withBasicIntRangeQuestion();
        handler.feedback(builder.build(), "");
        assertTrue(frontend.getAllLines().contains("how many points"));
        assertFalse(frontend.getAllLines().contains("hello world"));
        handler.askQuestion();
        assertTrue(frontend.getAllLines().contains("hello world"));
    }

    @Test
    public void checkQuestionOrder() {
        builder.withSimpleTextQuestion()
               .withBasicIntRangeQuestion()
               .withNegativeIntRange();
        handler.feedback(builder.build(), "");
        assertTrue(frontend.getAllLines().contains("how many points"));
        assertFalse(frontend.getAllLines().contains("hello world"));
        handler.askQuestion();
        assertTrue(frontend.getAllLines().contains("how cold is it"));
        assertFalse(frontend.getAllLines().contains("hello world"));
        handler.askQuestion();
        assertTrue(frontend.getAllLines().contains("hello world"));
    }
    
    @Test
    public void validateIntRange() {
        builder.withBasicIntRangeQuestion();
        handler.feedback(builder.build(), "");
        assertEquals("0", handler.validateAnswer("-1"));
    }
    
    @Test
    public void instructionMessage() {
        builder.withBasicIntRangeQuestion();
        handler.feedback(builder.build(), "");
        String expected = "Please give your answer as an integer between [0..10] (inclusive)";
        assertEquals(expected, frontend.getMostRecentLine());
    }
    
    @Test
    public void instructionMessageTest2() {
        builder.withNegativeIntRange();
        handler.feedback(builder.build(), "");
        String expected = "Please give your answer as an integer between [-10..10] (inclusive)";
        assertEquals(expected, frontend.getMostRecentLine());
    }
    
    @Test
    public void urlGetterTest() {
        String url = "http://mooc.helsinki.fi/staging/test";
        builder.withSimpleTextQuestion();
        handler.feedback(builder.build(), url);
        assertEquals(url, handler.getFeedbackUrl());
    }
}