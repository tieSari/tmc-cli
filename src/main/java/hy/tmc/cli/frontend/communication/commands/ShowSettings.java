
package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.TmcCli;
import fi.helsinki.cs.tmc.core.configuration.TmcSettings;

public class ShowSettings extends Command<String> {

    public ShowSettings(TmcCli cli) {
        super(cli);
    }

    @Override
    public String call() throws Exception {
        TmcSettings settings = this.tmcCli.defaultSettings();
        StringBuilder result = new StringBuilder();
        if(settings.userDataExists()){
            result.append("Username: ").append(settings.getUsername()).append("\n");
        }
        if(settings.getServerAddress() != null){
            result.append("Server address: ").append(settings.getServerAddress()).append("\n");
        }
        if(settings.getCurrentCourse().isPresent()){
            result.append("Current course: ").append(settings.getCurrentCourse().get().getName()).append("\n");
        }
        if(settings.getTmcMainDirectory() != null){
            result.append("Tmc Main Directory: ").append(settings.getTmcMainDirectory()).append("\n");
        }
        return result.toString();
    }
    
}
