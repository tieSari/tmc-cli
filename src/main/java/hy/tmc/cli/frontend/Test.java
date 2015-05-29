package hy.tmc.cli.frontend;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableList;


public class Test {

    public final String name;
    public final boolean passed;
    public final TestError error;
    public final ImmutableList<String> points;

    public Test(String name, TestError error, ImmutableList<String> points){
        this.name = checkNotNull(name);
        this.error = error;
        this.passed = error == null;
        this.points = checkNotNull(points);
    }
}
