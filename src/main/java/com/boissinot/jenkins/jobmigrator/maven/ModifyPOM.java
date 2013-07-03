package com.boissinot.jenkins.jobmigrator.maven;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHeaders;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Gregory Boissinot
 */
public class ModifyPOM {

    public String process(Message<String> pomFileMessage) {

        final MessageHeaders headers = pomFileMessage.getHeaders();
        final String remoteUrl = (String) headers.get("REMOTE_URL");

        String pomFileContent = pomFileMessage.getPayload();


        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pomFileContent.getBytes());
            InputSource inputSource = new InputSource(byteArrayInputStream);
            Document document = docBuilder.parse(inputSource);


            //--Change connection
            //String previousNodeValue = node.getTextContent();
            //System.out.println("PreviousValue: " + previousNodeValie);
            //node.setTextContent("toto");

            //${scm.connection.cvs.tango-ds}:Instrumentation/AcquireWaveformLecroy

            //String newValue =  previousNodeValue.replace("scm.connection.cvs.tango-ds","scm.connection.svn.tango-ds");

            String baseSVNURL = "https://svn.code.sf.net/p/tango-ds/code/";
            String newValue = remoteUrl.substring(remoteUrl.lastIndexOf(baseSVNURL) + baseSVNURL.length());
            newValue = "${scm.connection.svn.tango-ds}/" + newValue;

            //Connection element
            final NodeList connection = document.getElementsByTagName("connection");
            final Node connectionNode = connection.item(0);
            connectionNode.setTextContent(newValue);
            //developerConnection element
            final NodeList developerConnection = document.getElementsByTagName("developerConnection");
            final Node developerConnectionNode = developerConnection.item(0);
            developerConnectionNode.setTextContent(newValue);
            //url element
            final NodeList url = document.getElementsByTagName("url");
            final Node urlNode = url.item(0);
            urlNode.setTextContent(newValue);


            Writer out = new StringWriter();
            OutputFormat format = new OutputFormat();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }

            return out.toString();


        } catch (Exception e) {
            throw new JobMigrationException(e);
        }

    }
}
