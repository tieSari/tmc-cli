package hy.tmc.cli.frontend.communication.server;

import com.google.common.util.concurrent.Futures;

import fi.helsinki.cs.tmc.core.TmcCore;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;
import fi.helsinki.cs.tmc.core.domain.Course;
import fi.helsinki.cs.tmc.core.domain.Exercise;
import fi.helsinki.cs.tmc.core.exceptions.TmcCoreException;

import hy.tmc.cli.CliSettings;
import hy.tmc.cli.TmcCli;
import hy.tmc.cli.configuration.ConfigHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;



public class CommandExecutorTest {

    CommandExecutor executor;
    TmcCore core;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

    @Before
    public void setup()
        throws IOException, TmcCoreException, IllegalStateException, ParseException {
        core = Mockito.mock(TmcCore.class);
        TmcCli cli = new TmcCli(core);
        cli.setServer("tmc.mooc.fi");
        cli.defaultSettings();

        new ConfigHandler().writeLastUpdate(sdf.parse("04-4-1950 10:00:00"));

        executor = new CommandExecutor(cli);
        List<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new Exercise());
        when(core.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
            .thenReturn(Futures.immediateFuture(exerciseList));
    }

    @Test
    public void updatesAvailable()
        throws TmcCoreException, IllegalStateException, IOException, InterruptedException,
        ParseException, ExecutionException {
        CliSettings settings = new CliSettings();
        settings.setCurrentCourse(new Course());
        settings.setLastUpdate(sdf.parse("04-4-1950 10:00:00"));
        String result = executor.checkUpdates(settings);
        assertTrue(result.contains("Updates available"));
    }

    @Test
    public void noUpdatesAvailable()
        throws TmcCoreException, IOException, IllegalStateException, InterruptedException,
        ParseException, ExecutionException {
        List<Exercise> exerciseList = new ArrayList<>();
        when(core.getNewAndUpdatedExercises(any(Course.class), any(TmcSettings.class)))
            .thenReturn(Futures.immediateFuture(exerciseList));
        CliSettings settings = new CliSettings();
        settings.setCurrentCourse(new Course());
        settings.setLastUpdate(sdf.parse("04-4-1950 10:00:00"));
        String result = executor.checkUpdates(settings);
        assertTrue(result.contains("No updates available"));
    }

}
