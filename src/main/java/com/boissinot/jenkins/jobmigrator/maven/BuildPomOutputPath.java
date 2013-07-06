package com.boissinot.jenkins.jobmigrator.maven;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Gregory Boissinot
 */
public class BuildPomOutputPath {

    public String process(Message<String> pomContentMessage) {

        final MessageHeaders headers = pomContentMessage.getHeaders();

        String outputDir = (String) headers.get("OUTPUT_DIR");

        String baseSVNURL = "https://svn.code.sf.net/p/tango-ds/code/";
        String remoteURL = (String) headers.get("REMOTE_URL");
        String moduleSVN = remoteURL.substring(remoteURL.lastIndexOf(baseSVNURL) + baseSVNURL.length());

        File dirPomFile = new File(outputDir, moduleSVN);
        dirPomFile.mkdirs();

        File outputPomFile = new File(dirPomFile, "pom.xml");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outputPomFile);
            fileOutputStream.write(pomContentMessage.getPayload().getBytes());
        } catch (FileNotFoundException fne) {
            throw new JobMigrationException(fne);
        } catch (IOException ioe) {
            throw new JobMigrationException(ioe);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException ioe) {
                    throw new JobMigrationException(ioe);
                }
            }
        }

        return outputPomFile.getAbsolutePath();

    }
}
