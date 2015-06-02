package hy.tmc.cli.testhelpers;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;

import java.util.List;


public class FrontendStub implements FrontendListener {
    
    String line;

    @Override
    public void start() {
        
    }

    @Override
    public void printLine(String line) {
        this.line = line;
    }

    @Override
    public void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl) {

    }

    public String getMostRecentLine() {
        return line;
    }
    
}
