package hy.tmc.cli.testhelpers;


import java.io.*;

public class Helper {
    private ProcessBuilder createProcessBuilder(String command, String cliPath) {
        if (cliPath == null) {
            cliPath = "scripts/frontend.sh";
        }
        return new ProcessBuilder("bash", cliPath, command);
    }
    private Process createProcess(String command, String cliPath, boolean waitUntilFinished) {
        Process p = null;
        try {
            p = createProcessBuilder(command, cliPath).start();
            if (waitUntilFinished) {
                p.waitFor();
            }
        } catch (Exception e) {
            System.out.println("prosessin luonti feilas");
        }
        return p;
    }
    public String printOutput(String command, String cliPath) throws InterruptedException {
        Process p = createProcess(command, cliPath, true);
        return readOutputFromProcess(p);
    }

    public String readOutputFromProcess(Process process) throws InterruptedException {
        process.waitFor();
        InputStream inputStream = process.getInputStream();
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
    
    public Process startDialogWithCommand(String command, String cliPath) {
        Process process = createProcess(command, cliPath, false);
        waitMilliseconds(100);
        return process;
    }
    
    private void waitMilliseconds(int s) {
        try {
            Thread.sleep(s);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public Process writeInputToProcess(Process loginDialog, String input) throws IOException {
        OutputStream outputStream = loginDialog.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            writer.write(input);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Kirjoitus epäonnistui.");
        } finally {
            writer.flush();
        }
        waitMilliseconds(100);
        return loginDialog;
    }
}
