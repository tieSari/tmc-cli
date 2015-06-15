package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.domain.submission.FeedbackQuestion;
import hy.tmc.cli.frontend.FrontendListener;

<<<<<<< HEAD
@Deprecated
=======
import java.util.List;

>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
public class ServerMock implements FrontendListener{

    private StringBuilder printedLines;

    public ServerMock() {
        this.printedLines = new StringBuilder();
    }
    
    @Override
    public void start() {
        
    }
<<<<<<< HEAD
=======

    @Override
    public void printLine(String line) {
        this.printedLines.append(line);
    }

    @Override
    public void feedback(List<FeedbackQuestion> feedbackQuestions, String feedbackUrl) {

    }

>>>>>>> b4daca84b62f91a2ea5ea563447fb900c9db9f5a
    public String getPrintedLine() {
        return printedLines.toString();
    }
}
