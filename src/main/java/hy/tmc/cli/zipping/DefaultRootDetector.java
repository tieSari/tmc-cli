package hy.tmc.cli.zipping;

import java.io.File;
import java.nio.file.Path;


public class DefaultRootDetector implements ProjectRootDetector{
    
    @Override
    public boolean isRootDirectory(Path path) {
        File dir = path.toFile();
        if (!dir.isDirectory()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.getName().equals("pom.xml")) {
                return true;
            }
            if (file.getName().equals("build.xml")) {
                return true;
            }
        }
        return false;
    }
}
