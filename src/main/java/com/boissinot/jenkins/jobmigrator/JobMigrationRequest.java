package com.boissinot.jenkins.jobmigrator;

import java.io.File;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class JobMigrationRequest {

    private Map<String, String> migrationData;

    private File configFile;

    public JobMigrationRequest(Map<String, String> migrationData, File configFile) {
        this.migrationData = migrationData;
        this.configFile = configFile;
    }

    public Map<String, String> getMigrationData() {
        return migrationData;
    }

    public File getConfigFile() {
        return configFile;
    }

    @Override
    public String toString() {
        StringBuilder builder =
                new StringBuilder("JobMigrationRequest{");
        if (configFile != null) {
            builder.append("configFile=");
            builder.append(configFile.getAbsolutePath());
        }
        builder.append("}");
        return builder.toString();
    }
}
