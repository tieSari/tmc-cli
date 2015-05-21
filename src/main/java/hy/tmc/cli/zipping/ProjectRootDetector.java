package hy.tmc.cli.zipping;

import java.nio.file.Path;

public interface ProjectRootDetector {

    public boolean isRootDirectory(Path directory);
    
}
