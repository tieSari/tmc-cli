package hy.tmc.cli.frontend;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableList;


public class TestError {
    public final String errorMessage;
    public final ImmutableList<String> stackTrace;
   
    public TestError(String errorMessage, ImmutableList<String> stackTrace) {
        this.errorMessage = checkNotNull(errorMessage);
        this.stackTrace = checkNotNull(stackTrace);
    }
}
