package hy.tmc.cli.zipping;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayDeque;

public class ProjectRootFinder {

    private final ProjectRootDetector detector;
    
    /**
     * A helper class that searches for a project root directory. It must be 
     * given a ProjectRootDetector that corresponds with the project in question.
     * For example, for a Maven project a DefaultRootDetector can be used.
     * 
     * @param detector the RootDetector that specifies if a directory is a project root
     */
    public ProjectRootFinder(ProjectRootDetector detector) {
        this.detector = detector;
    }
    
    /**
     * Get the path of the project root directory.
     * @param zipRoot the path of the extracted zip.
     * @return path of the root, of null if no root was found
     */
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
