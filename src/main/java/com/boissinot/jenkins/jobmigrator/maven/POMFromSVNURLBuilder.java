package com.boissinot.jenkins.jobmigrator.maven;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import com.boissinot.jenkins.jobmigrator.JobMigrationException;

/**
 * @author Gregory Boissinot
 */
public class POMFromSVNURLBuilder {

    // Returns a message with pom file as payload
    // Takes a message with remote SVN URL content
    public Message<String> process(Message<String> remoteURLMessage) {

        String remoteURL = remoteURLMessage.getPayload();

        String pomFileContent;

        String pomURL;
        if (remoteURL.endsWith("/"))
            pomURL = remoteURL + "pom.xml";
        else
            pomURL = remoteURL + "/pom.xml";

        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.synchrotron-soleil.fr", 8080));
            pomURL = pomURL.replace("https", "http");
            System.out.println("Fetching " + pomURL);

            URL url = new URL(pomURL);
            URLConnection urlConnection = url.openConnection(proxy);
            final InputStream inputStream = urlConnection.getInputStream();
            pomFileContent = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();

        } catch (MalformedURLException e) {
            throw new JobMigrationException(e);
        } catch (IOException e) {
            throw new JobMigrationException(e);
        }

        return MessageBuilder.withPayload(pomFileContent).copyHeaders(remoteURLMessage.getHeaders())
                .setHeader("REMOTE_URL", remoteURL).build();
    }
}
