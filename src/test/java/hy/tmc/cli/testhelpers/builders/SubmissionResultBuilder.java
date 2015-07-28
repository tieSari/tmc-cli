package hy.tmc.cli.testhelpers.builders;

import hy.tmc.core.domain.submission.FeedbackQuestion;
import hy.tmc.core.domain.submission.SubmissionResult;
import hy.tmc.core.domain.submission.SubmissionResult.Status;
import hy.tmc.core.domain.submission.TestCase;
import hy.tmc.core.domain.submission.ValidationError;
import hy.tmc.core.domain.submission.Validations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionResultBuilder {

    private SubmissionResult result;

    public SubmissionResultBuilder() {
        result = new SubmissionResult();
        setValidations();
    }

    private void setValidations() {
        Validations v = new Validations();
        v.setValidationErrors(new HashMap<String, List<ValidationError>>());
        result.setValidations(v);
    }

    public SubmissionResultBuilder withAllTestsPassed() {
        result.setAllTestsPassed(true);
        return this;
    }

    public SubmissionResultBuilder withCourse(String courseName) {
        result.setCourse(courseName);
        return this;
    }

    public SubmissionResultBuilder withExerciseName(String name) {
        result.setExerciseName(name);
        return this;
    }

    public SubmissionResultBuilder withFeedbackUrl(String feedbackUrl) {
        result.setFeedbackAnswerUrl(feedbackUrl);
        return this;
    }

    public SubmissionResultBuilder withFeedbackQuestions(List<FeedbackQuestion> qs) {
        result.setFeedbackQuestions(qs);
        return this;
    }

    public SubmissionResultBuilder withPasteMessage(String message) {
        result.setMessageForPaste(message);
        return this;
    }

    public SubmissionResultBuilder withPoints(List<String> points) {
        result.setPoints(points);
        return this;
    }

    public SubmissionResultBuilder withPoints(String... points) {
        result.setPoints(Arrays.asList(points));
        return this;
    }

    public SubmissionResultBuilder asReviewed() {
        result.setReviewed(true);
        return this;
    }

    public SubmissionResultBuilder withSolutionUrl(String url) {
        result.setSolutionUrl(url);
        return this;
    }

    public SubmissionResultBuilder withStatus(Status status) {
        result.setStatus(status);
        return this;
    }

    public SubmissionResultBuilder withTestCases(List<TestCase> testCases) {
        result.setTestCases(testCases);
        return this;
    }

    public SubmissionResultBuilder withTestCase(TestCase testCase) {
        List<TestCase> cases = new ArrayList<>(result.getTestCases());
        cases.add(testCase);
        result.setTestCases(cases);
        return this;
    }

    public SubmissionResultBuilder withValidations(Validations v) {
        result.setValidations(v);
        return this;
    }

    public SubmissionResultBuilder withValidationError(String filename, ValidationError error) {
        Validations v = result.getValidations();
        Map<String, List<ValidationError>> map = v.getValidationErrors();
        List<ValidationError> errors;
        if (map.containsKey(filename)) {
            errors = map.get(filename);
        } else {
            errors = new ArrayList<>();
        }
        errors.add(error);
        map.put(filename, errors);
        return this;
    }

    public SubmissionResult build() {
        SubmissionResult built = result;
        result = new SubmissionResult();
        setValidations();
        return built;
    }

}
