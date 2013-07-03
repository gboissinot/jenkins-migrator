package com.boissinot.jenkins.jobmigrator.cvs2svn;

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
public class CVS2SVNBatchLauncher {

    private String defaultJenkinsHome;
    private String defaultSvnRoot;
    private String defaultInputMigrationFile;

    public CVS2SVNBatchLauncher() {
    }

    private CVS2SVNBatchLauncher(String defaultJenkinsHome, String defaultSvnRoot, String defaultInputMigrationFile) {
        this.defaultJenkinsHome = defaultJenkinsHome;
        this.defaultSvnRoot = defaultSvnRoot;
        this.defaultInputMigrationFile = defaultInputMigrationFile;
    }

    public void launch() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {
        String defaultJenkinsHome = "/Users/gregory/Dev/jenkins-migrator/configs/";
        String defaultSvnRoot = "https://svn.code.sf.net/p/tango-ds/code/";
        String defaultInputMigrationFile = "/Users/gregory/Dev/tango-ds.csv";

        CVS2SVNBatchLauncher launcher = new CVS2SVNBatchLauncher(defaultJenkinsHome, defaultSvnRoot, defaultInputMigrationFile);
        launcher.launchJob();
    }

    private void launchJob() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {
        ApplicationContext applicationContext
                = new ClassPathXmlApplicationContext("batch-cvs2svn.xml");

        final Job job = applicationContext.getBean("batchCVS2SVNMigration", Job.class);
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
