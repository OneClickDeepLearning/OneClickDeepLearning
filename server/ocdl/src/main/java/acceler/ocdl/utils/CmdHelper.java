package acceler.ocdl.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CmdHelper {

    public static String runCommand(String cmd) {
        StringBuilder output = new StringBuilder();

        System.out.println("[DEBug] docker container is running");

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            int maxOutputLines = 100;
            while ((s = br.readLine()) != null && maxOutputLines-- > 0) {
                output.append(s);
            }
            p.waitFor();
            p.destroy();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
