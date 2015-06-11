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

    private ClientData() {
    }

    /**
     * Sets the data for current user.
     *
     * @param username Username of the current user
     * @param password Password of the current user
     */
    public synchronized static void setUserData(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
    }

    private synchronized static ProjectRootFinder getProjectRootFinder() {
        if (rootFinder == null) {
            rootFinder = new ProjectRootFinder(new DefaultRootDetector());
        }
        return rootFinder;
    }

    public synchronized static Optional<Course> getCurrentCourse(String currentPath) {
        return getProjectRootFinder().getCurrentCourse(currentPath);
    }

    public synchronized static boolean userDataExists() {
        return !(USERNAME.isEmpty() || PASSWORD.isEmpty());
    }

    public synchronized static void clearUserData() {
        USERNAME = "";
        PASSWORD = "";
    }

    public synchronized static String getFormattedUserData() {
        return USERNAME + ":" + PASSWORD;
    }

    public synchronized static void logOutCurrentUser() {
        USERNAME = "";
        PASSWORD = "";
    }

    public synchronized static int getPid() {
        return PID;
    }

    public synchronized static void setPid(int pid) {
        ClientData.PID = pid;
    }

    public synchronized static String getUsername() {
        return USERNAME;
    }

    public synchronized static String getPassword() {
        return PASSWORD;
    }
}
