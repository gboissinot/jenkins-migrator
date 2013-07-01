package com.boissinot.jenkins.jobmigrator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gregory Boissinot
 */
public class DataMigrationGather {

    private final String svnUrlRoot;
    private final String inputMigrationFilePath;

    public DataMigrationGather(String svnUrlRoot, String inputMigrationFilePath) {
        this.svnUrlRoot = svnUrlRoot;
        this.inputMigrationFilePath = inputMigrationFilePath;
    }

    public Map<String, String> gatherConfigJobList() {

        Map<String, String> migrationDataMap = new ConcurrentHashMap<String, String>();

        try {
            FileReader fileReader = new FileReader(inputMigrationFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] tab = line.split(";");
                String cvsColumn = removeQuoteCharacter(tab[0]);
                String svnColumn = removeQuoteCharacter(tab[1]);
                String moduleName = cvsColumn.substring(cvsColumn.lastIndexOf("/") + 1);
                String svnURL = svnUrlRoot + svnColumn + "/" + moduleName + "/trunk/";
                migrationDataMap.put(moduleName, svnURL);
            }
        } catch (IOException ioe) {
            throw new JobMigrationException(ioe);
        }

        return migrationDataMap;

    }


    private static String removeQuoteCharacter(String s) {
        if (s == null) {
            return null;
        }

        s = s.trim();

        if (s.isEmpty()) {
            return s;
        }

        if (s.startsWith("\"")) {
            return removeQuoteCharacter(s.substring(1));
        }

        if (s.endsWith("\"")) {
            return s.substring(0, s.length() - 1);
        }

        return s;
    }
}
