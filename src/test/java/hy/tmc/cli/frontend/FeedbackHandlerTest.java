package hy.tmc.cli.frontend;

import hy.tmc.cli.testhelpers.FeedbackBuilder;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class FeedbackHandlerTest {

    private FrontendStub frontend;
    private FeedbackHandler handler;
    private FeedbackBuilder builder;

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
        assertTrue(frontend.getAllLines().contains("text"));
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
}