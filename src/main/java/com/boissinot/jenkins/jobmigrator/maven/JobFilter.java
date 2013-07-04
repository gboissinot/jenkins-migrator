package com.boissinot.jenkins.jobmigrator.maven;

import java.io.File;

/**
 * @author Gregory Boissinot
 */
public class JobFilter {


    public boolean filterNonRelease(File configXMLFile) {

        final String configXMLFileName = configXMLFile.getName();
        if ((configXMLFileName.endsWith("RELEASE")) || (configXMLFileName.endsWith("RELEASE_REPORT"))) {
            return false;
        }
        return true;
    }
}
