package hy.tmc.cli.configuration;

import com.google.common.base.Optional;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;

/**
 * This class will be initialized when Auth is successful. Use this to get data
 * of user
 */
public final class ClientData {

    private static int PID;
    private static String USERNAME = "";
    private static String PASSWORD = "";
    private static ProjectRootFinder rootFinder;
    private static Optional<Course> cachedCourse;

    private ClientData() {
    }

    /**
     * Sets the data for current user.
     *
     * @param username Username of the current user
     * @param password Password of the current user
     */
    public static void setUserData(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
    }

    private static ProjectRootFinder getProjectRootFinder() {
        if (rootFinder == null) {
            rootFinder = new ProjectRootFinder(new DefaultRootDetector());
        }
        return rootFinder;
    }

    public static Optional<Course> getCurrentCourse(String currentPath) {
        cachedCourse = getProjectRootFinder().getCurrentCourse(currentPath);
        return cachedCourse;
    }

    public static Optional<Course> getCachedCourse() {
        return cachedCourse;
    }

    public static boolean userDataExists() {
        return !(USERNAME.isEmpty() || PASSWORD.isEmpty());
    }

    public static void clearUserData() {
        USERNAME = "";
        PASSWORD = "";
    }

    public static String getFormattedUserData() {
        return USERNAME + ":" + PASSWORD;
    }

    public static void logOutCurrentUser() {
        USERNAME = "";
        PASSWORD = "";
    }

    public static int getPid() {
        return PID;
    }

    public static void setPid(int pid) {
        ClientData.PID = pid;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
