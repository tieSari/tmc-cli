package hy.tmc.cli.frontend;

import hy.tmc.cli.domain.submission.FeedbackQuestion;

import java.util.List;

public interface FrontendListener {
    /**
     * Starts the Frontend Listener.
     */
    void start();
    
    /**
     * prints Strings.
     * @param line is printed out
     */
    void printLine(String line);

    /**
     * Take feedback question to get feedback from the user with.
     * @param feedbackUrl the Url where the feedback data should be sent to
     */
    void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl);
}
