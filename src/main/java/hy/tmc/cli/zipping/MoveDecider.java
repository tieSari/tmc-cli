package hy.tmc.cli.zipping;

import java.nio.file.Path;

public interface MoveDecider {
    
    public boolean shouldMove(String path);
    public void readTmcprojectYml(Path zipRoot);
}