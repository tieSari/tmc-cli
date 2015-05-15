package hy.tmc.cli.testhelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jani on 12.5.15.
 */
public class Helper {

    public ProcessBuilder createProcessBuilder(String command, String cliPath) {

        if (cliPath == null) {
            cliPath = "scripts/frontend.sh";
        }

        return new ProcessBuilder("bash", cliPath, command);
    }

    public String printOutput(String command, String cliPath) {

        Process p = null;
        try {
            p = createProcessBuilder(command, cliPath).start();
            p.waitFor();
        } catch (Exception e) {
            System.out.println("prosessin luonti feilas");
            return "";
        }

        System.out.println("luotiin prosessi");

        InputStream inputStream = p.getInputStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("rivi: " + line);
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            return "";
        }
        System.out.println("tulos:");
        System.out.println(sb.toString());

        return sb.toString();
    }
}
