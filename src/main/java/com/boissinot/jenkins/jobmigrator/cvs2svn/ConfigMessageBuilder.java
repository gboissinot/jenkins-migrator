package com.boissinot.jenkins.jobmigrator.cvs2svn;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.apache.commons.io.FileUtils;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import java.io.File;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class ConfigMessageBuilder {

    public Message<String> buildMessage(JobMigrationRequest jobMigrationRequest) {

        try {
            File configFile = jobMigrationRequest.getConfigFile();
            String currentJobDirectoryPath = configFile.getParentFile().getAbsolutePath();
            final String configFileString = FileUtils.readFileToString(configFile);
            return MessageBuilder
                    .withPayload(configFileString)
                    .setHeader(JobSPIConstants.JOB_DIRECTORY_PATH, currentJobDirectoryPath)
                    .setHeader(JobSPIConstants.INIT_CONFIG_FILE_PATH, configFile.getAbsolutePath())
                    .setHeader(JobSPIConstants.MIGRATION_DATA_MAP, jobMigrationRequest.getMigrationData())
                    .build();

        } catch (IOException ioe) {
            throw new JobMigrationException(ioe);
        }
    }
}
