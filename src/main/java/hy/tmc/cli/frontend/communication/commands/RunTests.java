//package hy.tmc.cli.frontend.communication.commands;
//
//import com.google.common.base.Optional;
//import fi.helsinki.cs.tmc.langs.NoLanguagePluginFoundException;
//import fi.helsinki.cs.tmc.langs.RunResult;
//import fi.helsinki.cs.tmc.langs.util.TaskExecutorImpl;
//import hy.tmc.cli.frontend.FrontendListener;
//import hy.tmc.cli.frontend.ResultInterpreter;
//import hy.tmc.cli.frontend.communication.server.ProtocolException;
//import hy.tmc.cli.frontend.formatters.CommandLineTestResultFormatter;
//import hy.tmc.cli.frontend.formatters.TestResultFormatter;
//import hy.tmc.cli.frontend.formatters.VimTestResultFormatter;
//import hy.tmc.cli.zipping.DefaultRootDetector;
//import hy.tmc.cli.zipping.ProjectRootFinder;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class RunTests extends Command {
//
//    private TestResultFormatter formatter;
//
//    @Override
//    protected Optional<String> functionality() {
//        formatter = getFormatter();
//        String path = this.data.get("filepath");
//        ProjectRootFinder finder = new ProjectRootFinder(new DefaultRootDetector());   
//        Optional<Path> exercise = finder.getRootDirectory(Paths.get(path));
//        if (!exercise.isPresent()){
//            return Optional.of("Not an exercise. (null)");
//        }
//        try {
//            return Optional.of(runTests(exercise.get()));
//        } catch (NoLanguagePluginFoundException ex) {
//            return Optional.of("Not an exercise.");
//        }
//    }
//    
//    private TestResultFormatter getFormatter(){
//        if(data.containsKey("--vim")){
//            return new VimTestResultFormatter();
//        } else {
//            return new CommandLineTestResultFormatter();
//        }
//    }
//
//    /**
//     * Runs tests for exercise.
//     * @param exercise Path object
//     * @throws NoLanguagePluginFoundException if path doesn't contain exercise
//     */
//    public String runTests(Path exercise) throws NoLanguagePluginFoundException {
//        TaskExecutorImpl taskExecutor = new TaskExecutorImpl();
//        RunResult result = taskExecutor.runTests(exercise);
//        
//        boolean showStackTrace = this.data.containsKey("verbose");
//        ResultInterpreter resInt = new ResultInterpreter(result, formatter);
//        return resInt.interpret(showStackTrace);
//    }
//
//    @Override
//    public void checkData() throws ProtocolException {
//        if (!this.data.containsKey("filepath")) {
//            throw new ProtocolException("File path to exercise required.");
//        }
//    }
//}
