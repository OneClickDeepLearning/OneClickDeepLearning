package com.ocdl.proxy.util;

import java.io.File;
import java.util.*;

public class FileTool {

    public static Set<String> listModel(String source) {

        System.out.println("the file source is: " + source);

        Set<String> models = new HashSet<String>();

        File jkModel = new File(source);
        if (! jkModel.isDirectory()) {
            System.out.println("Cannot find such directory");
        }
        // find all the file or directory below the source
        File[] files = jkModel.listFiles();

        for (File f : files) {

            if (isModel(f)) {
                models.add(f.getName());
            }
        }
        return models;

    }

    public static Set<String> getNewModels(Set<String> curModel, Set<String> preModel) {

        Set<String> newModel = new HashSet<>();

        newModel.addAll(curModel);
        newModel.removeAll(preModel);

        return newModel;

    }

    public static HashMap<String, Set<String>> getNewModels(HashMap<String, Set<String>> curModel, HashMap<String, Set<String>> preModel) {

        HashMap<String, Set<String>> newModel = new HashMap<String, Set<String>>();

        for (String directory : curModel.keySet()) {

            if (!preModel.containsKey(directory)) {

                newModel.put(directory, curModel.get(directory));

            } else {

                Set<String> intersaction = new HashSet<String>();
                for (String each : curModel.get(directory)) {
                    intersaction.add(each);
                }
                intersaction.removeAll(preModel.get(directory));
                newModel.put(directory, intersaction);
            }
        }
        return newModel;
    }

    private static Boolean isModelDirectory(File f) {

        if (f.getName().startsWith(".") || !f.isDirectory()) return false;
        return true;
    }

    private static Boolean isModel(File f) {

        if (f.getName().endsWith(".model")) return true;
        return false;

    }

}
