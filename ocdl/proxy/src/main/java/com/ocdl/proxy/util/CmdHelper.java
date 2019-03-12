package com.ocdl.proxy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class CmdHelper {

    public static String runCommand(String script, String args, String path) {
        Process p = null;

        try {
            String cmd = "sh " + script + " " + args;

            File f = null;
            if(path != null){
                f = new File(path);
                System.out.println(path);
            }
//            String[] evnp = {"val=2", "call=Bash Shell"};
            p = Runtime.getRuntime().exec(cmd, null, f);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String s;
            StringBuilder out = new StringBuilder();
            while((s=bufferedReader.readLine()) != null){ out.append(s); }

            p.waitFor();
            p.destroy();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "ok";
    }
}
