package com.boissinot.jenkins.jobmigrator.backup.fromfileystem;

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
public class CopyConfigJobByFilesystemLauncher {

    public void launch() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {

        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("batch-backupfromfilesystem.xml");

        final JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        final Job job = applicationContext.getBean("copyJenkinsHomeByFilesysten", Job.class);

        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        String jobsDirPath = System.getProperty("jobsDirPath");
        if (jobsDirPath == null) {
            jobsDirPath = "/home/jenkins/jobs";
        }
        String outputConfigsDirPath = System.getProperty("outputConfigsDirPath");
        if (outputConfigsDirPath == null) {
            outputConfigsDirPath = CopyConfigJobByURLLauncher.DEFAULT_OUTPUT_CONFIGDIR_PATH;
        }

        System.out.println("Launching Batch - COPY CONFIG JOBS FROM FILESYSTEM");
        System.out.println("JOB_DIRECTORY_PATH:" + jobsDirPath);
        System.out.println("OUTPUT_CONFIG_JOBS_DIRECTORY_PATH:" + outputConfigsDirPath);

        parametersBuilder.addString("jobsDirPath", jobsDirPath);
        parametersBuilder.addString("outputConfigsDirPath", outputConfigsDirPath);
        final JobExecution jobExecution = jobLauncher.run(job, parametersBuilder.toJobParameters());
        System.out.println(jobExecution.getExitStatus());
    }
}
