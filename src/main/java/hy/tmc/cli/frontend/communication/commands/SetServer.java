package hy.tmc.cli.frontend.communication.commands;

import com.google.common.base.Strings;
import hy.tmc.cli.TmcCli;
import java.util.regex.Pattern;

public class SetServer extends Command<Boolean> {

    private final String dataKey = "tmc-server";

    public SetServer(TmcCli cli) {
        super(cli);
    }

    public SetServer(TmcCli cli, String serverAddress) {
        this(cli);
        this.setParameter(dataKey, serverAddress);
    }

    private boolean isValidTmcUrl(String url) {
        String urlPattern = "(https?://)?([a-z]+\\.){2,}[a-z]+(/[a-z]+)*";
        Pattern tmcServerAddress = Pattern.compile(urlPattern);
        if (Strings.isNullOrEmpty(url)) {
            return false;
        }
        return tmcServerAddress.matcher(url).matches();
    }

    @Override
    public Boolean call() throws Exception {
        String address = data.get(dataKey);
        return tmcCli.setServer(address);
    }
}
