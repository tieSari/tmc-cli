package hy.tmc.cli.zipping;

import com.google.common.base.Optional;
import java.nio.file.Path;

public class ProjectRootFinder implements RootFinder {

    private final ProjectRootDetector detector;

    /**
     * A helper class that searches for a project root directory. It must be
     * given a ProjectRootDetector that corresponds with the project in
     * question. For example, for a Maven project a DefaultRootDetector can be
     * used.
     *
     * @param detector the RootDetector that specifies if a directory is a
     * project root
     */
    public ProjectRootFinder(ProjectRootDetector detector) {
        this.detector = detector;
    }

    /**
     * Get the path of the project root directory.
     *
     * @param start the path of the extracted zip.
     * @return path of the root, of null if no root was found
     */
    @Override
    public Optional<Path> getRootDirectory(Path start) {
        return search(start);
    }

    private Optional<Path> search(Path path) {
        while (path.getParent() != null) {
            System.out.println(path.toString());
            if (detector.isRootDirectory(path)) {
                return Optional.of(path);
            }
            path = path.getParent();
        }
        return Optional.absent();
    }
}
