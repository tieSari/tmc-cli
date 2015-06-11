package hy.tmc.cli.frontend;

import hy.tmc.cli.domain.submission.FeedbackQuestion;

import java.util.ArrayDeque;
import java.util.List;

/**
 * A class to manage asking feedback questions in the correct order. The server will take care of
 * getting the answers from the user, and maintaining the answers, the handler will keep track of
 * asked questions and validate output (the server will have to call the validation method) for
 * intranges.
 */
public class FeedbackHandler {

    private final FrontendListener server;
    private final ArrayDeque<FeedbackQuestion> feedbackQueue;
    private int lastQuestionId;
    private String feedbackUrl;
    private String kind;

    public FeedbackHandler(FrontendListener server) {
        this.server = server;
        this.feedbackQueue = new ArrayDeque<>();
    }

    /**
     * Take the next feedback questions, arrange them so that text-questions are last, and ask the
     * first question.
     *
     * @param feedbackQuestions the questions that will be asked.
     * @param feedbackUrl the url where feedback answers should be sent to.
     */
    public void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl) {
        this.feedbackQueue.clear();

        for (FeedbackQuestion question : feedbackQuestions) {
            if (!question.getKind().equals("text")) {
                this.feedbackQueue.add(question);
            }
        }

        for (FeedbackQuestion question : feedbackQuestions) {
            if (question.getKind().equals("text")) {
                this.feedbackQueue.add(question);
            }
        }

        this.feedbackUrl = feedbackUrl;

        this.askQuestion();
    }

    public boolean allQuestionsAsked() {
        return this.feedbackQueue.isEmpty();
    }

    /**
     * Take the next question, and ask it. the ID of the question will be remembered, and 
     * is available through a getter.
     */
    public void askQuestion() {
        FeedbackQuestion nextQuestion = this.feedbackQueue.removeFirst();
        lastQuestionId = nextQuestion.getId();
        this.kind = nextQuestion.getKind();
        server.printLine(nextQuestion.getQuestion());
        String instructions = instructions(nextQuestion.getKind());
        if (!instructions.isEmpty()) {
            server.printLine(instructions);
        }
    }

    private String instructions(String kind) {
        if (kind.equals("text")) {
            return "text";
        }
        String range = kind.replace("intrange", "");
        return "Please give your answer as an integer within " + range + " (inclusive)";
    }

    /**
     * Make sure an intrange answer is valid. If it's not valid return a valid answer
     * 
     * @param answer the answer given by the user
     * @return the answer if its valid, or the intranges lower bound otherwise
     */
    public String validateAnswer(String answer) {
        if (kind.equals("text")) {
            return answer;
        }
        String bounds = kind.split("[\\[\\]]")[1];
        int lowerbound = Integer.parseInt(bounds.split("\\.\\.")[0]);
        int upperbound = Integer.parseInt(bounds.split("\\.\\.")[1]);

        int ans;
        try {
            ans = Integer.parseInt(answer);
        } catch (NumberFormatException ex) {
            return "" + lowerbound;
        }
        if (ans < lowerbound || ans > upperbound) {
            return "" + lowerbound;
        }
        return answer;

    }

    /**
     * ID of the last question asked. 
     */
    public int getLastId() {
        return this.lastQuestionId;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }
}
