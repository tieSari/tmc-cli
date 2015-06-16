
package hy.tmc.cli.domain;

import fi.helsinki.cs.tmc.langs.RunResult;


public class TestResult implements CommandResult{
    private RunResult result;

    public RunResult getResult() {
        return result;
    }

    public void setResult(RunResult result) {
        this.result = result;
    }
}
