package hy.tmc.cli.testhelpers;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import java.util.ArrayList;
import java.util.List;


public class FeedbackBuilder {

    private List<FeedbackQuestion> questions;
    
    public FeedbackBuilder() {
        questions = new ArrayList<>();
    }
    
    public List<FeedbackQuestion> build() {
        return this.questions;
    }
    
    public FeedbackBuilder withSimpleTextQuestion() {
        addQuestion("hello world", "text");
        return this;
    }
    
    public FeedbackBuilder withLongTextQuestion() {
        addQuestion("A very long strory\nblaablaablaa\n<(^)\n(___)", "text");
        return this;
    }
    
    public FeedbackBuilder withBasicIntRangeQuestion() {
        addQuestion("how many points", intRange(0,10));
        return this;
    }
    
    public FeedbackBuilder withNegativeIntRange() {
        addQuestion("how cold is it", intRange(-10, 10));
        return this;
    }
    
    private void addQuestion(String message, String kind) {
        FeedbackQuestion fbq = new FeedbackQuestion();
        fbq.setQuestion(message);
        fbq.setKind(kind);
        questions.add(fbq);
    }
    
    private String intRange(int a, int b) {
        return "intrange[" + a + ".." + b + "]";
    }
}
