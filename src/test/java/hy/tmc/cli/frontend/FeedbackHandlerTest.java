package hy.tmc.cli.frontend;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.testhelpers.FrontendStub;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FeedbackHandlerTest {

    private FrontendStub frontend;
    private FeedbackHandler handler;

    @Before
    public void setUp() throws Exception {
        this.frontend = new FrontendStub();
        this.handler = new FeedbackHandler(frontend);
    }

    @Test
    public void answerOneQuestion() {
        handler.feedback(testQuestions(), "");
        // handler.askQuestion();
        assertEquals("text", frontend.getMostRecentLine());
    }

    @Test
    public void answerManyQuestions() {

    }

    @Test
    public void checkQuestionOrder() {

    }

    @Test
    public void validateIntRange() {

    }

    private List<FeedbackQuestion> testQuestions() {
        List<FeedbackQuestion> questions = new ArrayList<>();
        FeedbackQuestion fbq = new FeedbackQuestion();
        fbq.setQuestion("hello world");
        fbq.setKind("text");
        questions.add(fbq);
        return questions;
    }
}