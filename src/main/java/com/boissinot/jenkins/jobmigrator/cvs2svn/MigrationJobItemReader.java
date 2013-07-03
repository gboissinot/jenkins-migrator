package com.boissinot.jenkins.jobmigrator.cvs2svn;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class MigrationJobItemReader implements ItemReader<JobMigrationRequest> {

    private List<File> jobList = new ArrayList<File>();
    private Map<String, String> migrationMap = new HashMap<String, String>();

    @BeforeStep
    @SuppressWarnings("unchecked")
    private void gatherConfigJobList(StepExecution stepExecution) {
        final JobExecution jobExecution = stepExecution.getJobExecution();
        final ExecutionContext executionContext = jobExecution.getExecutionContext();

        //-- Retrieve configJobPaths value
        final List<String> configJobPaths = (List<String>) executionContext.get(BackupJobItemReader.EXECUTIONCONTEXT_KEY_CONFIGJOBPATHS);
        for (String configJobPath : configJobPaths) {
            jobList.add(new File(configJobPath));
        }

        //-- Retrieve migration data
        migrationMap = (Map<String, String>) executionContext.get(BackupJobItemReader.EXECUTIONCONTEXT_KEY_MIGRATION_MAP);
    }

    @Override
    public JobMigrationRequest read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if (jobList.size() == 0) {
            return null;
        }

        final File file = jobList.get(0);
        System.out.println("Checking config file: " + file.getAbsolutePath());
        jobList.remove(0);

        return new JobMigrationRequest(migrationMap, file);
    }

}
