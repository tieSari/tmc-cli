package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Strings;

import hy.tmc.cli.TmcCli;

import java.util.regex.Pattern;

public class SetServer extends Command<String> {

    private String newAddress;

    public SetServer(TmcCli cli) {
        super(cli);
    }

    public SetServer(TmcCli cli, String serverAddress) {
        this(cli);
        this.newAddress = serverAddress;
    }

    private boolean isValidTmcUrl(String url) {
        String urlPattern = "(https?://)([a-z]+\\.){2,}[a-z]+(/[a-z]+)*";
        Pattern tmcServerAddress = Pattern.compile(urlPattern);
        return !Strings.isNullOrEmpty(url) && tmcServerAddress.matcher(url).matches();
    }

    @Override
    public String call() throws Exception {
        if (!isValidTmcUrl(newAddress)) {
            return "Address is not valid tmc-url. It must start with http:// or https:// ";
        }
        if (tmcCli.setServer(newAddress)) {
            return "Server address changes to " + newAddress;
        }
        return "Could not change address. Is the file config.properties present?";
    }
}
