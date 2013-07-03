package com.boissinot.jenkins.jobmigrator.backup.fromurl;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Node;

import javax.xml.transform.Source;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class CopyConfigJobTasklet implements Tasklet {

    private String jenkinsURL;
    private String outputDirPath;

    public CopyConfigJobTasklet(String jenkinsURL, String outputDirPath) {
        this.jenkinsURL = jenkinsURL;
        this.outputDirPath = outputDirPath;
    }

    private List<String> jobNames = new ArrayList<String>();


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        URL url = new URL(jenkinsURL + "/api/xml");
        final InputStream inputStream = url.openStream();

        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != -1) {
            builder.append((char) c);
        }
        String configXMLContent = builder.toString();

        Jaxp13XPathTemplate template = new Jaxp13XPathTemplate();
        Source configXMLSource = new StringSource(configXMLContent);
        final List<Node> nodeList = template.evaluateAsNodeList("//job", configXMLSource);


        for (Node node : nodeList) {
            String jobName = node.getChildNodes().item(0).getTextContent();
            jobNames.add(jobName);
        }

        for (String jobName : jobNames) {
            URL jobURL = new URL(jenkinsURL + "job/" + jobName + "/config.xml");
            final InputStream inputStream2 = jobURL.openStream();
            StringBuilder builder2 = new StringBuilder();
            int c2;
            while ((c2 = inputStream2.read()) != -1) {
                builder2.append((char) c2);
            }
            String configXMLContent2 = builder2.toString();

            File rootOutputDir = new File(outputDirPath);
            rootOutputDir.mkdirs();
            File configFileDir = new File(rootOutputDir, jobName);
            configFileDir.mkdirs();
            File configFile = new File(configFileDir, "config.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(configFile);
            fileOutputStream.write(configXMLContent2.getBytes());
            fileOutputStream.close();
        }

        return null;


    }


}
