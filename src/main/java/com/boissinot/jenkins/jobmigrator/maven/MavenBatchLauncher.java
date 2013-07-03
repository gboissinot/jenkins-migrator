package com.boissinot.jenkins.jobmigrator.maven;

import com.boissinot.jenkins.jobmigrator.backup.fromurl.CopyConfigJobByURLLauncher;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Gregory Boissinot
 */
public class MavenBatchLauncher {

    public void launch() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("batch-maven.xml");
        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);

        final Job batchMavenMigrationJob = applicationContext.getBean("batchMavenMigration", Job.class);

        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        String jobsDirPath = System.getProperty("jobsDirPath");
        if (jobsDirPath == null) {
            jobsDirPath = CopyConfigJobByURLLauncher.DEFAULT_OUTPUT_CONFIGDIR_PATH;
        }

        String outputMavenDirPath = System.getProperty("outputMavenDirPath");
        if (outputMavenDirPath == null) {
            outputMavenDirPath = "mavenPOMS";
        }

        System.out.println("Launching Batch - COPY MAVEN DESCRIPTOR (POM.XML) from JENKINS HOME DIR");
        System.out.println("JOB_DIRECTORY_PATH:" + jobsDirPath);
        System.out.println("MAVEN_OUTPUT_DIR_PATH:" + outputMavenDirPath);

        parametersBuilder.addString("jobsDirPath", jobsDirPath);
        parametersBuilder.addString("outputMavenDirPath", outputMavenDirPath);
        final JobExecution jobExecution = jobLauncher.run(batchMavenMigrationJob, parametersBuilder.toJobParameters());
        System.out.println(jobExecution.getExitStatus());
    }

}
