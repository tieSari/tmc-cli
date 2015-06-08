package hy.tmc.cli.backend.communication;

import com.google.common.util.concurrent.AbstractScheduledService;
import static com.google.common.util.concurrent.AbstractScheduledService.Scheduler.newFixedRateSchedule;
import java.util.concurrent.TimeUnit;

public class StatusPoller extends AbstractScheduledService {

    
    
    
    @Override
    protected void runOneIteration() throws Exception {
        System.out.println("Jee from cron-like");
    }

    @Override
    protected Scheduler scheduler() {
        return newFixedRateSchedule(0, 5, TimeUnit.SECONDS);
    }
    
    private void checkReviews(){
        
    }

}
