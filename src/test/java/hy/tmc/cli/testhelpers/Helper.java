package hy.tmc.cli.testhelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jani on 12.5.15.
 */
public class Helper {

    public String startCommand(String command, String cliPath) {
        Process p = null;

        if (cliPath == null) {
            cliPath = "cli/frontend.sh";
        }

        try {
            p = new ProcessBuilder("bash", cliPath, command).start();
            p.waitFor();
        } catch (Exception e) {
            return "";
        }
        InputStream inputStream = p.getInputStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            return "";
        }
        return sb.toString();
    }
}
