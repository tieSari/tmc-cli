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
    private boolean started = false;
    private Set<Service> tasks;
    private ServiceManager serviceManager;

    public static TmcServiceScheduler getScheduler() {
        if (instance == null) {
            instance = new TmcServiceScheduler();
        }
        return instance;
    }

    public static void startIfNotRunning(Course course) {
        if (!isRunning()) {
            TmcServiceScheduler.getScheduler().addService(
                    new StatusPoller(course, new PollScheduler(5, TimeUnit.SECONDS))
            );
        }
    }

    public static boolean isRunning() {
        return isRunningTasks;
    }

    /**
     * Disable polling.
     */
    public static void disablePolling() {
        if (isRunningTasks) {
            getScheduler().stop();
        }
        isRunningTasks = true;
    }

    private TmcServiceScheduler() {
        tasks = new HashSet<>();
    }

    public TmcServiceScheduler addService(Service service) {
        if (started) {
            return this;
        }
        tasks.add(service);
        return this;
    }

    public void start() {
        this.serviceManager = new ServiceManager(tasks);
        isRunningTasks = true;
        serviceManager.startAsync();
    }

    public void stop() {
        isRunningTasks = false;
        if (serviceManager == null) {
            return;
        }
        serviceManager.stopAsync();
        instance = new TmcServiceScheduler();
    }
}
