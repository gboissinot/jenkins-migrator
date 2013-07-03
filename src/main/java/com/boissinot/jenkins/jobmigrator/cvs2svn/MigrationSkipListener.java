package com.boissinot.jenkins.jobmigrator.cvs2svn;

import org.springframework.batch.core.SkipListener;

import java.io.File;

/**
 * @author Gregory Boissinot
 */
public class MigrationSkipListener implements SkipListener<JobMigrationRequest, File> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        System.err.println("Can't read item");
        throwable.printStackTrace();
    }

    @Override
    public void onSkipInProcess(JobMigrationRequest jobMigrationRequest, Throwable throwable) {
        System.err.println("Can't process item " + jobMigrationRequest);
    }

    @Override
    public void onSkipInWrite(File file, Throwable throwable) {
        System.err.println("Can't write config File " + file.getAbsolutePath());
    }

}
