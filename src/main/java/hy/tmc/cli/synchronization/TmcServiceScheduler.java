package hy.tmc.cli.synchronization;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import hy.tmc.cli.backend.communication.StatusPoller;
import hy.tmc.cli.domain.Course;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TmcServiceScheduler {

    private static TmcServiceScheduler instance;

    private static boolean isRunningTasks = false;
    private static boolean isDisabled;
    private final Set<Service> tasks;
    private ServiceManager serviceManager;

    /**
     * Retrieves the scheduler. Only one scheduler per
     * client can be running at the time.
     *
     * @return Scheduler which has fixed time tasks
     */
    public static TmcServiceScheduler getScheduler() {
        if (instance == null) {
            instance = new TmcServiceScheduler();
        }
        return instance;
    }

    public static void startIfNotRunning(Course course) {
        startIfNotRunning(course, 5, TimeUnit.SECONDS);
    }

    /**
     * Starts the scheduled time tasker.
     * @param course for denominating current course
     * @param interval when tasks are executed
     * @param timeunit in milliseconds, seconds, minutes, hours etc.
     */
    public static void startIfNotRunning(Course course, long interval, TimeUnit timeunit) {
        if (!isRunning()) {
            TmcServiceScheduler.getScheduler().addService(
                    new StatusPoller(course, new PollScheduler(interval, timeunit))
            ).start();
            isRunningTasks = true;
        }
    }

    public static boolean isRunning() {
        return isRunningTasks;
    }

    /**
     * Disables scheduled tasks until it enablePolling is called.
     */
    public static void disablePolling() {
        if (isRunningTasks) {
            getScheduler().stop();
        }
        isRunningTasks = true;
        isDisabled = true;
    }

    /**
     * Enables scheduled tasks to be executed.
     */
    public static void enablePolling() {
        if (isDisabled) {
            isDisabled = false;
            isRunningTasks = false;
        }
    }

    private TmcServiceScheduler() {
        tasks = new HashSet<>();
    }

    private TmcServiceScheduler addService(Service service) {
        if (isRunningTasks) {
            return this;
        }
        tasks.add(service);
        return this;
    }

    /**
     * Starts every service as asynced process.
     */
    public void start() {
        this.serviceManager = new ServiceManager(tasks);
        isRunningTasks = true;
        serviceManager.startAsync();
    }

    /**
     * Stops every service as asynced process.
     */
    public void stop() {
        isRunningTasks = false;
        if (serviceManager == null) {
            return;
        }
        serviceManager.stopAsync();
        instance = new TmcServiceScheduler();
    }
}
