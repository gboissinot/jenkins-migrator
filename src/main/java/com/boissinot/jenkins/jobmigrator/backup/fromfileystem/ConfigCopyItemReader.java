package com.boissinot.jenkins.jobmigrator.backup.fromfileystem;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class ConfigCopyItemReader implements ItemReader<File> {

    private String jobsDirPath;

    public ConfigCopyItemReader(String jobsDirPath) {
        this.jobsDirPath = jobsDirPath;
    }

    private List<String> configJobPaths = new ArrayList<String>();

    @BeforeStep
    public void gatherJobConfigsByFileSystem() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        File jobsDir = new File(jobsDirPath);
        if (!jobsDir.exists()) {
            throw new JobMigrationException(String.format("The job directory directory in JENKINS_HOME doesn't with %s", jobsDir));
        }
        final File[] jobFiles = jobsDir.listFiles();
        for (File jobFile : jobFiles) {
            if (jobFile.isDirectory()) {
                final File[] configs = jobFile.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().startsWith("config") && name.toLowerCase().endsWith(".xml");
                    }
                });
                for (File configFile : configs) {
                    configJobPaths.add(configFile.getAbsolutePath());
                }
            }
        }
    }

    @Override
    public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (configJobPaths == null || configJobPaths.size() == 0) {
            return null;
        }

        final String configJobPath = configJobPaths.get(0);
        configJobPaths.remove(0);
        return new File(configJobPath);
    }
}
