package com.heroes.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//π§æﬂ¿‡
public class Utils {


    public ArrayList<HashMap> listOfHashMaps = new ArrayList<>();

    public Utils(ArrayList<HashMap> listOfHashMaps) {
        this.listOfHashMaps = listOfHashMaps;
    }

    public Utils() {

    }
 
    public List<HashMap> fileRead(ArrayList<String> paths) throws IOException {
        for (String path : paths) {
            HashMap<String, String> unitProperties = new HashMap<>();
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null) {
                String[] propertyKeyAndValue = line.split(",", 2);
                if (propertyKeyAndValue.length >= 2) {
                    String key = propertyKeyAndValue[0];
                    String value = propertyKeyAndValue[1];
                    unitProperties.put(key, value);
                } else {
                    System.out.println("wrong unitProperty in txt file: " + line);
                }
            }
            reader.close();
            listOfHashMaps.add(unitProperties);
        }
        return listOfHashMaps;
    }

 
    public ArrayList<String> createPath() {
        ArrayList<String> pathsToFilesInUnits = new ArrayList<>();
        List<String> castleFileNames =  new ArrayList<String>(Arrays.asList("red", "orange", "yellow", "green", "cyran", "blue", "purple"));

        List<String> infernoFileNames = new ArrayList<String>(Arrays.asList("demon", "devil", "efreet", "gog", "hellhound", "imp", "pitfiend"));

        for (int i = 0; i < castleFileNames.size(); i++) {
            String pathToAdd = "resources/units/castle/" + castleFileNames.get(i) + "/" + castleFileNames.get(i);
            pathsToFilesInUnits.add(pathToAdd);
        }

        for (int i = 0; i < infernoFileNames.size(); i++) {
            String pathToAdd = "resources/units/inferno/" + infernoFileNames.get(i) + "/" + infernoFileNames.get(i);
            pathsToFilesInUnits.add(pathToAdd);
        }
        return pathsToFilesInUnits;

    }
}




