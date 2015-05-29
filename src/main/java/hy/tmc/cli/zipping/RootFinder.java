package hy.tmc.cli.zipping;

import java.nio.file.Path;

public interface RootFinder {

    public Path getRootDirectory(Path zipRoot);

}
