package hy.tmc.cli.zipping;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayDeque;

public class ProjectRootFinder {

    private final ProjectRootDetector detector;
    
    public ProjectRootFinder(ProjectRootDetector detector) {
        this.detector = detector;
    }
    
    public Path getRootDirectory(Path zipRoot) {
        return BreadthFirstSearch(zipRoot);
    }

    private Path BreadthFirstSearch(Path zipRoot) {
        ArrayDeque<Path> queue = new ArrayDeque<>();
        queue.add(zipRoot);
        while (!queue.isEmpty()){
            Path path = queue.removeFirst();
            if (!path.toFile().isDirectory()){
                continue;
            }
            if (this.detector.isRootDirectory(path)){
                return path;
            }
            for (File file : path.toFile().listFiles()){
                queue.addLast(file.toPath());
            }
        }
        return null;
    }



}
