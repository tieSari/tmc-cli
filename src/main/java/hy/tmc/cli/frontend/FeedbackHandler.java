package hy.tmc.cli.frontend;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.UrlCommunicator;
import hy.tmc.cli.configuration.ConfigHandler;
import hy.tmc.cli.domain.submission.FeedbackQuestion;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;

public class FeedbackHandler {


    private final FrontendListener server;
    private ArrayDeque<FeedbackQuestion> feedbackQueue;
    private int lastQuestionId;
    private String feedbackUrl;

    public FeedbackHandler(FrontendListener server) {
        this.server = server;
        this.feedbackQueue = new ArrayDeque<>();
    }

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

    public void askQuestion() {
        FeedbackQuestion nextQuestion = this.feedbackQueue.removeFirst();
        lastQuestionId = nextQuestion.getId();
        server.printLine(nextQuestion.getQuestion());
        server.printLine(nextQuestion.getKind());
    }

    public String validateAnswer(String answer, int lowerbound, int upperbound) {
        return answer;
    }

    public int getLastId() {
        return this.lastQuestionId;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }
}
