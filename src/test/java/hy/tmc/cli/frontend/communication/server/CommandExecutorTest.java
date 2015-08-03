package hy.tmc.cli.frontend.communication.server;

import hy.tmc.cli.TmcCli;
import hy.tmc.cli.testhelpers.TestFuture;
import hy.tmc.core.TmcCore;
import hy.tmc.core.configuration.TmcSettings;
import hy.tmc.core.domain.Course;
import hy.tmc.core.domain.Exercise;
import hy.tmc.core.exceptions.TmcCoreException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;


public class CommandExecutorTest {
    
    CommandExecutor executor;
    TmcCore core;
    
    @Before
    public void setup() throws IOException, TmcCoreException, IllegalStateException, ParseException {
        core = Mockito.mock(TmcCore.class);
        TmcCli cli = new TmcCli(core);
        cli.defaultSettings();
        executor = new CommandExecutor(cli);
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        exerciseList.add(new Exercise());
        TestFuture f = new TestFuture(exerciseList);
        when(core.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
                .thenReturn(f);
    }
    
    @Test
    public void updatesAvailable() throws TmcCoreException, IllegalStateException, IOException, InterruptedException, ParseException, ExecutionException{
        String result = executor.checkUpdates();
        assertTrue(result.contains("Updates available"));
    }
    
    @Test
    public void noUpdatesAvailable() throws TmcCoreException, IOException, IllegalStateException, InterruptedException, ParseException, ExecutionException{
        List<Exercise> exerciseList = new ArrayList<Exercise>();
        TestFuture f = new TestFuture(exerciseList);
        when(core.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
                .thenReturn(f);
         String result = executor.checkUpdates();
        assertTrue(result.contains("No updates available"));
    }
    
}