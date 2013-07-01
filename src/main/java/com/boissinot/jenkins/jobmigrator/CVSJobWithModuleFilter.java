package com.boissinot.jenkins.jobmigrator;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.Source;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class CVSJobWithModuleFilter {

    public Message<String> isCVSConfig(Message<String> jobMessage) {
        Jaxp13XPathTemplate template = new Jaxp13XPathTemplate();

        String configXMLContent = jobMessage.getPayload();
        @SuppressWarnings("unchecked")
        Map<String, String> migrationData = (Map<String, String>) jobMessage.getHeaders().get(JobSPIConstants.MIGRATION_DATA_MAP);


        Source configXMLSource = new StringSource(configXMLContent);

        Node scmNode = template.evaluateAsNode("//scm ", configXMLSource);
        String scmClassElement = ((DeferredElementNSImpl) scmNode).getAttribute("class");
        if ("hudson.scm.CVSSCM".equals(scmClassElement)) {

            final NodeList childNodes = scmNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {

                Node currentNode = childNodes.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

                    String currentNodeName = currentNode.getLocalName();
                    if ("module".equals(currentNodeName)) {
                        String moduleValue = currentNode.getTextContent();
                        moduleValue = moduleValue.trim();

                        //TODO URGENT EXTRACT REFACTOR
                        if (moduleValue.contains("/")) {
                            moduleValue = moduleValue.substring(moduleValue.lastIndexOf("/") + 1);
                        }

                        final String svnURL = migrationData.get(moduleValue);
                        if (svnURL == null) {
                            return null;
                        }

                        return MessageBuilder.fromMessage(jobMessage)
                                .setHeader(JobSPIConstants.SVN_URL, svnURL).build();
                    }
                }
            }
        }

        return null;

    }

}
