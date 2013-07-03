package com.boissinot.jenkins.jobmigrator.backup.fromurl;

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
public class CopyConfigJobByURLLauncher {

    public static final String DEFAULT_JENKINS_URL = "http://calypso/jenkins/";
    public static final String DEFAULT_OUTPUT_CONFIGDIR_PATH = "outputConfigs";

    public void launch() throws JobInstanceAlreadyCompleteException, JobParametersInvalidException, JobRestartException, JobExecutionAlreadyRunningException {

        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("batch-backupfromurl.xml");

        final JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        final Job job = applicationContext.getBean("copyJenkinsHomeByURL", Job.class);


        String jenkinsURL = System.getProperty("jenkinsURL");
        if (jenkinsURL == null) {
            jenkinsURL = DEFAULT_JENKINS_URL;
        }

        String outputConfigsDirPath = System.getProperty("outputConfigsDirPath");
        if (outputConfigsDirPath == null) {
            outputConfigsDirPath = DEFAULT_OUTPUT_CONFIGDIR_PATH;
        }

        System.out.println("Launching Batch - COPY CONFIG JOBS FROM JENKINS URL BY REST");
        System.out.println("JENKINS_URL:" + jenkinsURL);
        System.out.println("OUTPUT_CONFIG_JOBS_DIRECTORY_PATH:" + outputConfigsDirPath);

        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString("jenkinsURL", jenkinsURL);
        parametersBuilder.addString("outputConfigsDirPath", outputConfigsDirPath);
        final JobExecution jobExecution = jobLauncher.run(job, parametersBuilder.toJobParameters());
        System.out.println(jobExecution.getExitStatus());

    }
}
