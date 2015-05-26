package hy.tmc.cli.domain;

import java.util.Date;

public class Exercise {
    
    private int id; // = 284;
    private String name; //": "viikko1-Viikko1_000.Hiekkalaatikko",
    private boolean locked; // false,
    private String deadline_description; //: null,

    // todo make this Date?
    private String deadline; //: null,

    private String checksum; //: "406f2f0690550c6dea94f319b2b1580c",
    private String return_url; //: "https://tmc.mooc.fi/staging/exercises/284/submissions.json",
    private String zip_url; //": "https://tmc.mooc.fi/staging/exercises/284.zip",
    private boolean returnable; //": true,
    private boolean requires_review;//": false,
    private boolean attempted; //": false,
    private boolean completed; //": false,
    private boolean reviewed; //": false,
    private boolean all_review_points_given; //": true,
    private String memory_limit; //": null,
    private String[] runtime_params; //": [ ],
    private String valgrind_strategy; //": null,
    private boolean code_review_requests_enabled; //": true,
    private boolean run_tests_locally_action_enabled; //": true,
    private String exercise_submissions_url; //": "https://tmc.mooc.fi/staging/exercises/284.json?api_version=7

    public Exercise(int id, String name, boolean locked, String deadline_description, String deadline, String checksum, String return_url, String zip_url, boolean returnable, boolean requires_review, boolean attempted, boolean completed, boolean reviewed, boolean all_review_points_given, String memory_limit, String[] runtime_params, String valgrind_strategy, boolean code_review_requests_enabled, boolean run_tests_locally_action_enabled, String exercise_submissions_url) {
        this.id = id;
        this.name = name;
        this.locked = locked;
        this.deadline_description = deadline_description;
        this.deadline = deadline;
        this.checksum = checksum;
        this.return_url = return_url;
        this.zip_url = zip_url;
        this.returnable = returnable;
        this.requires_review = requires_review;
        this.attempted = attempted;
        this.completed = completed;
        this.reviewed = reviewed;
        this.all_review_points_given = all_review_points_given;
        this.memory_limit = memory_limit;
        this.runtime_params = runtime_params;
        this.valgrind_strategy = valgrind_strategy;
        this.code_review_requests_enabled = code_review_requests_enabled;
        this.run_tests_locally_action_enabled = run_tests_locally_action_enabled;
        this.exercise_submissions_url = exercise_submissions_url;
    }
    
    public Exercise() {
        
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getDeadline_description() {
        return deadline_description;
    }

    public void setDeadline_description(String deadline_description) {
        this.deadline_description = deadline_description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getZip_url() {
        return zip_url;
    }

    public void setZip_url(String zip_url) {
        this.zip_url = zip_url;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public boolean isRequires_review() {
        return requires_review;
    }

    public void setRequires_review(boolean requires_review) {
        this.requires_review = requires_review;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public boolean isAll_review_points_given() {
        return all_review_points_given;
    }

    public void setAll_review_points_given(boolean all_review_points_given) {
        this.all_review_points_given = all_review_points_given;
    }

    public String getMemory_limit() {
        return memory_limit;
    }

    public void setMemory_limit(String memory_limit) {
        this.memory_limit = memory_limit;
    }

    public String[] getRuntime_params() {
        return runtime_params;
    }

    public void setRuntime_params(String[] runtime_params) {
        this.runtime_params = runtime_params;
    }

    public String getValgrind_strategy() {
        return valgrind_strategy;
    }

    public void setValgrind_strategy(String valgrind_strategy) {
        this.valgrind_strategy = valgrind_strategy;
    }

    public boolean isCode_review_requests_enabled() {
        return code_review_requests_enabled;
    }

    public void setCode_review_requests_enabled(boolean code_review_requests_enabled) {
        this.code_review_requests_enabled = code_review_requests_enabled;
    }

    public boolean isRun_tests_locally_action_enabled() {
        return run_tests_locally_action_enabled;
    }

    public void setRun_tests_locally_action_enabled(boolean run_tests_locally_action_enabled) {
        this.run_tests_locally_action_enabled = run_tests_locally_action_enabled;
    }

    public String getExercise_submissions_url() {
        return exercise_submissions_url;
    }

    public void setExercise_submissions_url(String exercise_submissions_url) {
        this.exercise_submissions_url = exercise_submissions_url;
    }
}