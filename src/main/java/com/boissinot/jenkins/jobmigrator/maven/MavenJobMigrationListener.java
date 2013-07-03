package com.boissinot.jenkins.jobmigrator.maven;

import org.springframework.batch.core.SkipListener;

/**
 * @author Gregory Boissinot
 */
public class MavenJobMigrationListener implements SkipListener<Object, Object> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        if (throwable instanceof org.tmatesoft.svn.core.SVNException) {
            System.out.println("No POM with " + throwable.getMessage());
        }
    }

    @Override
    public void onSkipInWrite(Object o, Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSkipInProcess(Object o, Throwable throwable) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
