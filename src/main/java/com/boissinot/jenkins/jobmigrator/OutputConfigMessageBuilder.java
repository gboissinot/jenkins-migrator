package com.boissinot.jenkins.jobmigrator;

import org.apache.commons.io.IOUtils;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.springframework.integration.support.MessageBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class OutputConfigMessageBuilder {

    public Message createOutputFile(Message inputMessage) {

        final MessageHeaders headers = inputMessage.getHeaders();
        //String jobConfigDirectory = headers.get(JobSPIConstants.JOB_DIRECTORY_PATH, String.class);
        String initConfigFilePath = headers.get(JobSPIConstants.INIT_CONFIG_FILE_PATH, String.class);

        //Override initial config file
        File configFile = new File(initConfigFilePath);
        try {

            //Writing the outputFile
            System.out.println(String.format("Overriding configFile %s", configFile.getAbsolutePath()));
            FileWriter fileWriter = new FileWriter(configFile);
            IOUtils.write(inputMessage.getPayload().toString(), fileWriter);
            fileWriter.close();

            //Return to the final writer
            return MessageBuilder
                    .withPayload(configFile)
                    .build();

        } catch (IOException ioe) {
            throw new JobMigrationException(ioe);
        }
    }
}
