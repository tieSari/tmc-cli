package hy.tmc.cli.frontend;

import com.google.common.base.Optional;

import fi.helsinki.cs.tmc.core.domain.Course;

import hy.tmc.cli.frontend.communication.server.ProtocolException;

import java.io.File;
import java.util.List;

public class CourseFinder {

    public CourseFinder() {
    }

    /**
     * Returns a course object. Finds it from the current path.
     *
     * @param path Path to look up course from.
     * @return Course-object containing information of the course found.
     */
    public Optional<Course> getCurrentCourse(String path, List<Course> courses) {
        String[] foldersOfPwd = path.split("\\" + File.separator);
        try {
            checkPwd(foldersOfPwd);
        } catch (ProtocolException ex) {
            return Optional.absent();
        }
        return findCourseByPath(foldersOfPwd, courses);
    }

    /**
     * Downloads all courses and iterates over them. Returns Course whose name
     * matches with one folder in given path.
     *
     * @param foldersPath contains the names of the folders in path
     * @return Course
     */
    public Optional<Course> findCourseByPath(String[] foldersPath, List<Course> courses) {
        for (Course course : courses) {
            for (String folderName : foldersPath) {
                if (course.getName().equals(folderName)) {
                    return Optional.of(course);
                }
            }
        }
        return Optional.absent();
    }

    private void checkPwd(String[] foldersOfPwd) throws ProtocolException {
        if (foldersOfPwd.length == 0) {
            throw new ProtocolException("No folders found from the path.");
        }
    }
}
