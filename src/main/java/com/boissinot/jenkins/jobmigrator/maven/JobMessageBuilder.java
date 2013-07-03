package com.boissinot.jenkins.jobmigrator.maven;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.apache.commons.io.FileUtils;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import java.io.File;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class JobMessageBuilder {

    public Message<String> buildMessageWithConfigXMLContent(Message<File> configXMLFileMessage) {

        File configXMLFile = configXMLFileMessage.getPayload();

        String configXMLContent;
        try {
            configXMLContent = FileUtils.readFileToString(configXMLFile);
        } catch (IOException ioe) {
            throw new JobMigrationException(ioe);
        }
        String jobName = configXMLFile.getParentFile().getName();

        return MessageBuilder
                .withPayload(configXMLContent)
                .copyHeaders(configXMLFileMessage.getHeaders())
                .setHeader("JOB_NAME", jobName)
                .build();
    }
}
