
package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import hy.tmc.cli.backend.communication.HttpResult;
import hy.tmc.cli.backend.communication.TmcJsonParser;
import hy.tmc.cli.domain.Course;
import hy.tmc.cli.frontend.communication.server.ProtocolException;
import hy.tmc.cli.spyware.DiffSender;
import java.io.IOException;
import java.util.List;


public class SendSpywareDiffs extends Command<Boolean>{

    private byte[] spywareDiffs;
    private int courseId;
    private Course currentCourse;
    private DiffSender sender;

    /**
     * Standard constructor.
     * 
     * @param spywereDiffs includes diffs that editor has collected from user input.
     * @param courseId specifies currentCourse, which is needed to spyware-serverUrl of the course.
     */
    public SendSpywareDiffs(byte[] spywereDiffs, int courseId) {
        this(spywereDiffs, courseId, new DiffSender());
    }
    
    /**
     * Dependecy injection for tests.
     */
    public SendSpywareDiffs(byte[] spywareDiffs, int courseId, DiffSender sender) {
        this.spywareDiffs = spywareDiffs;
        this.courseId = courseId;
        this.sender = sender;
    }
    
    @Override
    public void checkData() throws ProtocolException, IOException {
        if (this.spywareDiffs == null) {
            throw new ProtocolException("No spyware-diff given.");
        }
        Optional<Course> course = TmcJsonParser.getCourse(courseId);
        if (course.isPresent()) {
            this.currentCourse = course.get();
        } else {
            throw new ProtocolException("No course found with given id: " + this.courseId);
        }
    }

    @Override
    public Optional<String> parseData(Object data) throws IOException {
       return Optional.of("This method should not be invoked, becouse atm tmc-cli do not collect any diffs"
               + "Please check docs of this super method.");
    }

    @Override
    public Boolean call() throws Exception {
        checkData();
        List<HttpResult> resultsFromSpywareServers = this.sender.sendToSpyware(spywareDiffs, currentCourse);
        if (allServersOk(resultsFromSpywareServers)) {
            return true;
        }
        return false;
    }

    private boolean allServersOk(List<HttpResult> resultsFromSpywareServers) {
        for (HttpResult result : resultsFromSpywareServers) {
            if (result.getStatusCode() < 200 || result.getStatusCode() >= 300) {
                return false;
            }
        }
        return true;
    }
}
