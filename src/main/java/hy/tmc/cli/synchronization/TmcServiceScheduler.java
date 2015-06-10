package hy.tmc.cli.synchronization;

import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import hy.tmc.cli.configuration.ClientData;
import java.util.HashSet;
import java.util.Set;

public class TmcServiceScheduler {

    private boolean started;
    private Set<Service> tasks;
    private ServiceManager serviceManager;

    public TmcServiceScheduler() {
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
        started = true;
        ClientData.setPolling(true);
        serviceManager.startAsync();
    }

}
