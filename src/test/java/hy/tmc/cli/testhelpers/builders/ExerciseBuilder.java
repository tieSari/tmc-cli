package hy.tmc.cli.testhelpers.builders;

import fi.helsinki.cs.tmc.core.domain.Exercise;

import java.net.URI;

public class ExerciseBuilder {

    private Exercise exercise;

    public ExerciseBuilder() {
        exercise = new Exercise();
    }

    public ExerciseBuilder withName(String name) {
        exercise.setName(name);
        return this;
    }

    public ExerciseBuilder withCourseName(String name) {
        exercise.setCourseName(name);
        return this;
    }

    public ExerciseBuilder withDeadline(String deadLine) {
        exercise.setDeadline(deadLine);
        return this;
    }

    public ExerciseBuilder withReturnUrl(URI returnUrl) {
        exercise.setReturnUrl(returnUrl);
        return this;
    }

    public ExerciseBuilder asReturnable() {
        exercise.setReturnable(true);
        return this;
    }

    public ExerciseBuilder asRequiringReview() {
        exercise.setRequiresReview(true);
        return this;
    }

    public ExerciseBuilder withSubmissionUrl(URI submissionUrl) {
        exercise.setExerciseSubmissionsUrl(submissionUrl);
        return this;
    }

    public ExerciseBuilder asAttempted() {
        exercise.setAttempted(true);
        return this;
    }

    public ExerciseBuilder asCompleted() {
        exercise.setCompleted(true);
        return this;
    }

    public ExerciseBuilder asLocked() {
        exercise.setLocked(true);
        return this;
    }

    public ExerciseBuilder withSolutionUrl(URI solution) {
        exercise.setSolutionDownloadUrl(solution);
        return this;
    }

    public ExerciseBuilder asReviewed() {
        exercise.setReviewed(true);
        return this;
    }

    public ExerciseBuilder withRuntimeParams(String... params) {
        exercise.setRuntimeParams(params);
        return this;
    }

    public ExerciseBuilder withId(int id) {
        exercise.setId(id);
        return this;
    }

    public Exercise build() {
        Exercise built = exercise;
        exercise = new Exercise();
        return built;
    }
}
