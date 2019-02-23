package acceler.ocdl.utils;

import java.io.*;
import java.util.List;

public class CmdHelper {

    public static String runCommand(List<String> cmds) {
//        StringBuilder output = new StringBuilder();
//        System.out.println("[DEBug] docker container is running");


        File f = new File("/home/ec2-user/model_repo/models/");

        Process p = null;
        for (String cmd : cmds) {
            try {
                System.out.println("[Debug] " + cmd);

                p = Runtime.getRuntime().exec(cmd, null, f);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s;
                StringBuilder out = new StringBuilder();
                while((s=bufferedReader.readLine()) != null){ out.append(s); }

                p.waitFor();
                p.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        try {
//
//	        File f = new File("/home/ec2-user/model_repo/models/1001/");
//
//            Process p = Runtime.getRuntime().exec(cmd,null,f);
//            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String s;
//            int maxOutputLines = 100;
//            while ((s = br.readLine()) != null && maxOutputLines-- > 0) {
//                output.append(s);
//            }
//            p.waitFor();
//            p.destroy();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return output.toString();
        return "ok";
    }
}
