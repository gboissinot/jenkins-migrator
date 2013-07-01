package com.boissinot.jenkins.jobmigrator;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Gregory Boissinot
 */
public class BatchLauncher {

    private final String defaultJenkinsHome;
    private final String defaultSvnRoot;
    private final String defaultInputMigrationFile;

    private BatchLauncher(String defaultJenkinsHome, String defaultSvnRoot, String defaultInputMigrationFile) {
        this.defaultJenkinsHome = defaultJenkinsHome;
        this.defaultSvnRoot = defaultSvnRoot;
        this.defaultInputMigrationFile = defaultInputMigrationFile;
    }

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {
        String defaultJenkinsHome = "/Users/gregory/Dev/jenkins-migrator/configs_test/";
        String defaultSvnRoot = "https://svn.code.sf.net/p/tango-ds/code/";
        String defaultInputMigrationFile = "/Users/gregory/Dev/tango-ds.csv";

        BatchLauncher launcher = new BatchLauncher(defaultJenkinsHome, defaultSvnRoot, defaultInputMigrationFile);
        launcher.launch();
    }

    private void launch() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {
        ApplicationContext applicationContext
                = new ClassPathXmlApplicationContext("batch.xml");

        final Job job = applicationContext.getBean("batchJobMigration", Job.class);
        final JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);

        long start = System.currentTimeMillis();
        final JobExecution jobExecution = jobLauncher.run(job, this.buildJobParameters());
        long end = System.currentTimeMillis();
        System.out.println(String.format("Batch %s with %d ms.", jobExecution.getExitStatus().getExitCode(), (end - start)));
    }

    private JobParameters buildJobParameters() {

        String jenkinsHome = System.getProperty("jenkinsHome");
        if (jenkinsHome == null) {
            jenkinsHome = defaultJenkinsHome;
        }
        String svnUrlRoot = System.getProperty("svnUrlRoot");
        if (svnUrlRoot == null) {
            svnUrlRoot = defaultSvnRoot;
        }
        String inputMigrationFilePath = System.getProperty("inputMigrationFilePath");
        if (inputMigrationFilePath == null) {
            inputMigrationFilePath = defaultInputMigrationFile;
        }

        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("jenkinsHome", jenkinsHome);
        parametersBuilder.addString("svnUrlRoot", svnUrlRoot);
        parametersBuilder.addString("inputMigrationFilePath", inputMigrationFilePath);

        return parametersBuilder.toJobParameters();
    }
}
