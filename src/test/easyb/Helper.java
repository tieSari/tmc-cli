package easyb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jani on 12.5.15.
 */
public class Helper {

    public static String startCommand(String command) {
        Process p = null;
        try {
            p = new ProcessBuilder("bash", "cli/frontend.sh", command).start();
            p.waitFor();
        } catch (Exception e) {
            return "";
        }
        InputStream inputStream = p.getInputStream();
        System.out.println(inputStream.toString());

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
