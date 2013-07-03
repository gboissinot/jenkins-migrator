package com.boissinot.jenkins.jobmigrator.maven;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;
import org.apache.commons.io.IOUtils;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/**
 * @author Gregory Boissinot
 */
public class POMFromSVNURLBuilder {


    //Returns a message with pom file as payload
    //Takes a message with remote SVN URL content
    public Message<String> process(Message<String> remoteURLMessage) {

        String remoteURL = remoteURLMessage.getPayload();

//        DAVRepositoryFactory.setup();
//        SVNRepository repository = null;
//        try {
//            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(remoteURL));
//        } catch (SVNException e) {
//            e.printStackTrace();
//        }
//        SVNProperties fileProperties = new SVNProperties();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        final long revision;
//        try {
//            revision = repository.getFile("pom.xml", -1, fileProperties, byteArrayOutputStream);
//        } catch (SVNException e) {
//            throw new JobMigrationException(e);
//        }
//
//        final String pomFileContent = new String(byteArrayOutputStream.toByteArray());

        String pomFileContent = null;
        try {
            String pomURL = remoteURL+ "pom.xml";

            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy", 8080));
            pomURL=pomURL.replace("https", "http");
            System.out.println("Fetching " + pomURL);

            URL url = new URL(pomURL);

            final InputStream inputStream = url.openStream();
            pomFileContent = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();

        } catch (MalformedURLException e) {
            throw new JobMigrationException(e);
        } catch (IOException e) {
            throw new JobMigrationException(e);
        }


        return MessageBuilder
                .withPayload(pomFileContent)
                .copyHeaders(remoteURLMessage.getHeaders())
                .setHeader("REMOTE_URL", remoteURL)
                .build();
    }
}
