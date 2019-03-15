package com.ocdl.proxy.util;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FileTool {

    public static HashMap<String, Set<String>> listModel(String source) {

        System.out.println("the file source is: " + source);

        HashMap<String, Set<String>> models = new HashMap<String, Set<String>>();

        File jkModel = new File(source);
        if (! jkModel.isDirectory()) {
            System.out.println("Cannot find such directory");
        }
        // find all the file or directory below the source
        File[] files = jkModel.listFiles();

        for (File f : files) {

            if (isModelDirectory(f)) {

                // if f not in the models, creat a new key-value pairs
                if ( !models.containsKey(f.getName())) {
                    models.put(f.getName(), new HashSet<>());
                }

                for (File m : f.listFiles()) {
                    if (isModel(m)) {
                        models.get(f.getName()).add(m.getName());
                    }
                }
            }
        }
        return models;

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

        if (f.getName().startsWith(".") || f.isDirectory()) return false;
        return true;

    }

}
