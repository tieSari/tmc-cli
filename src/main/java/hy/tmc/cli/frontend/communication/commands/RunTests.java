package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
import fi.helsinki.cs.tmc.langs.RunResult;
import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;
import hy.tmc.cli.configuration.ClientData;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.ResultInterpreter;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.frontend.formatters.CommandLineTestResultFormatter;
import hy.tmc.cli.frontend.formatters.TestResultFormatter;
import hy.tmc.cli.frontend.formatters.VimTestResultFormatter;
import hy.tmc.cli.synchronization.TmcServiceScheduler;
import hy.tmc.cli.zipping.DefaultRootDetector;
import hy.tmc.cli.zipping.ProjectRootFinder;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RunTests extends Command<RunResult> {

    private TestResultFormatter formatter;
    private Course course;
    private MailChecker mail;

    public RunTests() {
        mail = new MailChecker();
    }

    /**
     * Runs tests for exercise.
     *
     * @param exercise Path object
     * @return String contaning results
     * @throws NoLanguagePluginFoundException if path doesn't contain exercise
     */
    public RunResult runTests(Path exercise) throws NoLanguagePluginFoundException {
        TaskExecutorImpl taskExecutor = new TaskExecutorImpl();
        return taskExecutor.runTests(exercise);
    }

    @Override
    public void checkData() throws ProtocolException {
        if (!this.data.containsKey("path")) {
            throw new ProtocolException("File path to exercise required.");
        }
        Optional<Course> currentCourse = ClientData.getCurrentCourse(data.get("path"));
        if (currentCourse.isPresent()) {
            course = currentCourse.get();
        } else {
            throw new ProtocolException("Unable to determine course");
        }
    }

    @Override
    public Optional<String> parseData(Object data) {
        String mail = checkMail();
        RunResult result = (RunResult) data;
        boolean showStackTrace = this.data.containsKey("verbose");
        ResultInterpreter resInt = new ResultInterpreter(result, formatter);

        return Optional.of(mail + "\n" +resInt.interpret(showStackTrace));
    }

    @Override
    public RunResult call() throws ProtocolException, NoLanguagePluginFoundException {
        TmcServiceScheduler.startIfNotRunning(course);
        checkData();
        formatter = getFormatter();
        String path = this.data.get("path");
        ProjectRootFinder finder = new ProjectRootFinder(new DefaultRootDetector());
        Optional<Path> exercise = finder.getRootDirectory(Paths.get(path));
        if (!exercise.isPresent()) {
            throw new ProtocolException("Not an exercise. (null)");
        }
        return runTests(exercise.get());
    }

    /**
     * Executes the mail command with necessary params.
     * Gives the mail command either a courseID (preferably) or a path
     * for determining which courses reviews and updates should be fetched.
     *
     * @throws ProtocolException if unable to find necessary params.
     */
    private String checkMail() {
        if (data.containsKey("courseID")) {
            mail.setParameter("courseID", data.get("courseID"));
        } else if (data.containsKey("path")) {
            mail.setParameter("path", data.get("path"));
        } else {
            return "must specify path";
        }
        try {
            return mail.call();
        } catch (ProtocolException e) {
            return e.getMessage();
        }
    }

    private TestResultFormatter getFormatter() {
        if (data.containsKey("--vim")) {
            return new VimTestResultFormatter();
        } else {
            return new CommandLineTestResultFormatter();
        }
    }
}
