package com.boissinot.jenkins.jobmigrator.maven;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;

import java.io.File;

/**
 * @author Gregory Boissinot
 */
public class JobItemProcessor implements ItemProcessor<File, String> {

    private String outputMavenDirPath;

    public JobItemProcessor(String outputMavenDirPath) {
        this.outputMavenDirPath = outputMavenDirPath;
    }

    @Override
    public String process(File file) throws Exception {

        Message<File> message =
                MessageBuilder
                        .withPayload(file)
                        .setHeader("OUTPUT_DIR", outputMavenDirPath)
                        .build();

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("maven-si.xml");
        final MessageChannel inputConfigFilesChannel = applicationContext.getBean("inputConfigFiles", MessageChannel.class);

        MessagingTemplate messagingTemplate = new MessagingTemplate(inputConfigFilesChannel);
        messagingTemplate.setReceiveTimeout(1000);
        @SuppressWarnings("unchecked")
        final Message<String> receivedMessage = (Message<String>) messagingTemplate.sendAndReceive(message);

        return receivedMessage.getPayload();
    }
}
