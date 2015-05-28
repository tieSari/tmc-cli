package hy.tmc.cli.testhelpers.testresults;

import fi.helsinki.cs.tmc.langs.TestResult;
import java.util.ArrayList;
import java.util.List;

public class TestResultFactory {

    public static List<TestResult> failedTests() {
        List<TestResult> tests = new ArrayList<>();
        tests.add(kuusi());
        tests.add(toinen());
        
        return tests;
    }

    private static TestResult kuusi() {
        TestResultBuilder builder = new TestResultBuilder();
        ArrayList<String> stackTrace = new ArrayList<>();
        stackTrace.add("in line 5");
        stackTrace.add("in xyzw.java");

        builder.withName("KuusiTest")
                .withPassedStatus(false)
                .withErrorMessage("Ohjelmasi pitäisi tulostaa 6 riviä, eli siinä pitäisi olla 6"
                        + " System.out.println()-komentoa. expected:<6> but was:<1>")
                .withStackTrace(stackTrace);
        return builder.build();
    }

    private static TestResult toinen() {
        TestResultBuilder builder = new TestResultBuilder();
        ArrayList<String> stackTrace = new ArrayList<>();
        stackTrace.add("in line 5");
        stackTrace.add("in xyzw.java");
        builder.withName("KuusiTest")
                .withErrorMessage("ComparisonFailure: Kuusen toinen rivi on väärin expected:"
                        + "<  [ *]**> but was:<  []**>")
                .withPassedStatus(false)
                .withStackTrace(stackTrace);
        return builder.build();
    }

    public static List<TestResult> passedTests() {
        List<TestResult> tests = new ArrayList<>();
        TestResultBuilder builder = new TestResultBuilder();
        tests.add(builder.withName("test").withPassedStatus(true).build());
        tests.add(builder.withName("test2").withPassedStatus(true).build());
        tests.add(builder.withName("test3").withPassedStatus(true).build());
        tests.add(builder.withName("test4").withPassedStatus(true).build());
        tests.add(builder.withName("test5").withPassedStatus(true).build());
        return tests;
    }

}
