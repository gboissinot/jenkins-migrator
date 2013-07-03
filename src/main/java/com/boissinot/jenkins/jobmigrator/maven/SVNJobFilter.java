package com.boissinot.jenkins.jobmigrator.maven;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Node;

import javax.xml.transform.Source;

/**
 * @author Gregory Boissinot
 */
public class SVNJobFilter {

    public boolean filter(String configXMLContent) {

        Jaxp13XPathTemplate template = new Jaxp13XPathTemplate();
        Source configXMLSource = new StringSource(configXMLContent);
        Node scmNode = template.evaluateAsNode("//scm ", configXMLSource);
        String scmClassElement = ((DeferredElementNSImpl) scmNode).getAttribute("class");

        if ("hudson.scm.SubversionSCM".equals(scmClassElement)) {
            return true;
        }

        return false;
    }

}
