package com.boissinot.jenkins.jobmigrator.maven;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Node;

import javax.xml.transform.Source;

/**
 * @author Gregory Boissinot
 */
public class RemoteSVNURLMessageBuilder {

    //Takes a message with config XML content as message payload
    //Returns a message with the remote url as message content
    public Message<String> process(Message<String> configXMLMessage) {

        String configXMLContent = configXMLMessage.getPayload();

        Jaxp13XPathTemplate template = new Jaxp13XPathTemplate();
        Source configXMLSource = new StringSource(configXMLContent);
        Node remoteNode = template.evaluateAsNode("//scm/locations/hudson.scm.SubversionSCM_-ModuleLocation/remote ", configXMLSource);

        String remoteURLStr = remoteNode.getTextContent();

        if (remoteURLStr == null || remoteURLStr.trim().isEmpty()) {
            return null;
        }

        if (!remoteURLStr.contains("svn.code.sf.net/p/tango-ds/code/")) {
            return null;
        }

        return MessageBuilder
                .withPayload(remoteURLStr)
                .copyHeaders(configXMLMessage.getHeaders())
                .build();

    }
}
