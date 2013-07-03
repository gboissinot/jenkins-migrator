package com.boissinot.jenkins.jobmigrator.cvs2svn;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gregory Boissinot
 */
public class BackupJobItemReader implements ItemReader<String> {

    /**
     * Execution context key saved and promoted to the next step
     */
    public static final String EXECUTIONCONTEXT_KEY_MIGRATION_MAP = "migrationMap";
    public static final String EXECUTIONCONTEXT_KEY_CONFIGJOBPATHS = "configJobPaths";

    /**
     * The state computed before the step execution
     */
    private final List<String> jobConfigJobPaths = new ArrayList<String>();

    /**
     * Input elements for the reader
     */
    private final String jenkinsHome;
    private final String svnUrlRoot;
    private final String inputMigrationFilePath;

    public BackupJobItemReader(String jenkinsHome, String svnUrlRoot, String inputMigrationFilePath) {

        this.jenkinsHome = jenkinsHome;
        if (!new File(jenkinsHome).exists()) {
            throw new JobMigrationException(String.format("JENKINS_HOME %s must exist.", jenkinsHome));
        }

        this.svnUrlRoot = svnUrlRoot;
        if (svnUrlRoot == null) {
            throw new JobMigrationException("A root SVN URL must be set.");
        }

        this.inputMigrationFilePath = inputMigrationFilePath;
        if (!new File(inputMigrationFilePath).exists()) {
            throw new JobMigrationException(String.format("The migration data file path %s must be set.", inputMigrationFilePath));
        }
    }

    @BeforeStep
    @SuppressWarnings("unused")
    private void initData(StepExecution stepExecution) {
        final ExecutionContext executionContext = stepExecution.getExecutionContext();
        gatherMigrationData(executionContext);
        saveConfigJobPaths(executionContext);
    }

    private void gatherMigrationData(ExecutionContext executionContext) {
        DataMigrationGather dataMigrationGather = new DataMigrationGather(svnUrlRoot, inputMigrationFilePath);
        executionContext.put(EXECUTIONCONTEXT_KEY_MIGRATION_MAP, dataMigrationGather.gatherConfigJobList());
    }

    private void saveConfigJobPaths(ExecutionContext executionContext) {

        List<String> configJobPaths = new ArrayList<String>();
        File jenkinsHomeFile = new File(jenkinsHome);

        File jobsDir = new File(jenkinsHomeFile, "jobs");
        if (!jobsDir.exists()) {
            throw new JobMigrationException(String.format("The jobs directory directory in JENKINS_HOME doesn't with %s", jobsDir));
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
        jobConfigJobPaths.addAll(configJobPaths);
        executionContext.put(EXECUTIONCONTEXT_KEY_CONFIGJOBPATHS, configJobPaths);
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (jobConfigJobPaths.size() == 0) {
            return null;
        }

        final String configJobFilePath = jobConfigJobPaths.get(0);
        jobConfigJobPaths.remove(0);
        return configJobFilePath;
    }

}

