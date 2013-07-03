package com.boissinot.jenkins.jobmigrator.maven;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class JobItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> outputPomFilePaths) throws Exception {
        for (String outputPomFilePath : outputPomFilePaths) {
            System.out.println(outputPomFilePath);
        }
    }

}
