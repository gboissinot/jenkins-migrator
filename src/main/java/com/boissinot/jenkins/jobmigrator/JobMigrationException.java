package com.boissinot.jenkins.jobmigrator;

/**
 * @author Gregory Boissinot
 */
@SuppressWarnings("serial")
public class JobMigrationException extends RuntimeException {

    public JobMigrationException() {
    }

    public JobMigrationException(String s) {
        super(s);
    }

    public JobMigrationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JobMigrationException(Throwable throwable) {
        super(throwable);
    }
}
