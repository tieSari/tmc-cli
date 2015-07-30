
package hy.tmc.cli.frontend.communication.commands;

import hy.tmc.cli.TmcCli;
import hy.tmc.core.configuration.TmcSettings;

public class ShowSettings extends Command<String> {

    public ShowSettings(TmcCli cli) {
        super(cli);
    }

    @Override
    public String call() throws Exception {
        TmcSettings settings = this.tmcCli.defaultSettings();
        StringBuilder result = new StringBuilder();
        if(settings.userDataExists()){
            result.append("Username: " + settings.getUsername() + "\n");
        }
        if(settings.getServerAddress() != null){
            result.append("Server address: " + settings.getServerAddress() + "\n");
        }
        if(settings.getCurrentCourse().isPresent()){
            result.append("Current course: " + settings.getCurrentCourse().get().getName() + "\n");
        }
        if(settings.getTmcMainDirectory() != null){
            result.append("Tmc Main Directory: " + settings.getTmcMainDirectory() + "\n");
        }
        return result.toString();
    }
    
}
