package com.boissinot.jenkins.jobmigrator;

import com.boissinot.jenkins.jobmigrator.backup.fromfileystem.CopyConfigJobByFilesystemLauncher;
import com.boissinot.jenkins.jobmigrator.backup.fromurl.CopyConfigJobByURLLauncher;
import com.boissinot.jenkins.jobmigrator.cvs2svn.CVS2SVNBatchLauncher;
import com.boissinot.jenkins.jobmigrator.maven.MavenBatchLauncher;

/**
 * @author Gregory Boissinot
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length==0){
            System.out.println("java <-Doption1=value1> <-Doption2=value2> ... -jar job-extractor.jar <cmd>");
            System.out.println("The following commands are available.");
            System.out.println("[cvs2svn] - Converts cvs to svn section in the job configuration file for a Jenkins home.");
            System.out.println("Parameters: jenkinsHome,svnUrlRoot and inputMigrationFilePath");
            System.out.println();
            System.out.println();
            System.out.println("[backup-fromfs] - Backup job configuration file from a JENKINS_HOME directory.");
            System.out.println("Parameters: jobsDirPath and outputConfigsDirPath");
            System.out.println();
            System.out.println();
            System.out.println("[backup-fromurl] - Backup job configuration file from JENKINS instance with its URL.");
            System.out.println("Parameters: jenkinsURL and outputConfigsDirPath");
            System.out.println();
            System.out.println();
            System.out.println("[extract-pom] - Extract Maven descriptor from information of Jenkins instance with a JENKINS_HOME.");
            System.out.println("Parameters: jobsDirPath and outputMavenDirPath");
            System.out.println();
            System.out.println();
            return;
        }

        String cmd = args[0];

        if ("extract-pom".equalsIgnoreCase(cmd)) {
            MavenBatchLauncher mavenBatchLauncher = new MavenBatchLauncher();
            mavenBatchLauncher.launch();
            return;
        }

        if ("cvs2svn".equalsIgnoreCase(cmd)) {
            CVS2SVNBatchLauncher cvs2SVNBatchLauncher = new CVS2SVNBatchLauncher();
            cvs2SVNBatchLauncher.launch();
            return;
        }

        if ("backup-fromfs".equalsIgnoreCase(cmd)) {
            CopyConfigJobByFilesystemLauncher copyConfigJobByFilesystemLauncher = new CopyConfigJobByFilesystemLauncher();
            copyConfigJobByFilesystemLauncher.launch();
            return;
        }

        if ("backup-fromurl".equalsIgnoreCase(cmd)) {
            CopyConfigJobByURLLauncher copyConfigJobByURLLauncher = new CopyConfigJobByURLLauncher();
            copyConfigJobByURLLauncher.launch();
            return;
        }


    }
}
