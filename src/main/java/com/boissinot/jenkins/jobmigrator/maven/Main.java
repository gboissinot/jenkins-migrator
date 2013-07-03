package com.boissinot.jenkins.jobmigrator.maven;


import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.Element;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Gregory Boissinot
 */
public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
              DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(new File("/Users/gregory/Dev/jenkins-migrator/src/main/resources/testedPom.xml"));
        XPath xpath = XPathFactory.newInstance().newXPath();
        //Element scmElement = (Element) xpath.evaluate("//scm", document, XPathConstants.NODE);
        //connection
        //developerConnection
        //scmElement.

        final NodeList connection = document.getElementsByTagName("connection");
        final Node node = connection.item(0);
        String previousNodeValie= node.getTextContent();
        System.out.println("PreviousValue: "+ previousNodeValie);
        node.setTextContent("toto");

        Writer out = new StringWriter();
        OutputFormat format = new OutputFormat();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

        System.out.println(out);

//        if (e != null)
//            e.setTextContent("Wonderland");
    }

    public static void main2(String[] args) throws Exception {

        //String url = "http://svn.svnkit.com/repos/svnkit/trunk/doc";
        String url = "file:///Users/gregory/tmp/svnRepo/test/trunk";
        String name = "anonymous";
        String password = "anonymous";

        SVNRepository repository = null;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        } catch (SVNException e) {
            e.printStackTrace();
        }

        m1(url, repository);
    }

    private static void m1(String url, SVNRepository repository) throws SVNException, IOException {

//        SVNNodeKind nodeKind = repository.checkPath("a.txt", -1);
//
//        Collection entries = repository.getDir("", -1, null, (Collection) null);
//        Iterator iterator = entries.iterator();
//        while (iterator.hasNext()) {
//            SVNDirEntry entry = (SVNDirEntry) iterator.next();
//            System.out.println(entry.getRelativePath());
//        }

        //File fileA = new File("a.txt");
        //FileOutputStream fileOutputStream = new FileOutputStream(fileA);

//        //--Fetch file
//        SVNProperties fileProperties = new SVNProperties();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        final long revision = repository.getFile("a.txt", -1, fileProperties, byteArrayOutputStream);
//
//        //--Build Content and modify
//        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
////        StringBuilder stringBuilder = new StringBuilder();
////        stringBuilder.append(new String(byteArrayOutputStream.toByteArray()));
////        stringBuilder.append("\n");
//        writer.append("YING");
//
//        StringWriter stringWriter = new StringWriter();
//        stringWriter.write(writer.toString());
//
//
//        File fileA = new File("a.txt");
//        FileOutputStream fileOutputStream = new FileOutputStream(fileA);
//        fileOutputStream.write(byteArrayOutputStream.toByteArray());
//

        final SVNClientManager cm = SVNClientManager.newInstance(new DefaultSVNOptions(), null, null);

        SVNUpdateClient uc = cm.getUpdateClient();
        File dstPath = new File("dir");
        uc.doCheckout(SVNURL.parseURIDecoded(url), dstPath, SVNRevision.UNDEFINED, SVNRevision.HEAD, true);

        File aTxtFile = new File(dstPath, "a.txt");

        String content = IOUtils.toString(new FileInputStream(aTxtFile));
        content = content + "YING";
        FileOutputStream fileOutputStream = new FileOutputStream(aTxtFile);
        fileOutputStream.write(content.getBytes());

        //Fetch content
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        File
//        StringWriter fileWriter = new StringWriter();
//        fileWriter.append(new String(byteArrayOutputStream.toByteArray()));
//        fileWriter.append("\nYING");
//
//        new FileOutputStream(aTxtFile).
//        System.out.println(IOUtils.toString(new FileInputStream(aTxtFile)));

        cm.getCommitClient().doCommit(new File[]{aTxtFile}, false, "commit", null, null, false, true, SVNDepth.INFINITY);


//        String dirPath = "";//fileA.getParentFile().getAbsolutePath();
//        String filePath = fileA.getAbsolutePath();
//
//        byte[] oldData = byteArrayOutputStream.toByteArray();
//        byte[] newData = byteArrayOutputStream.toByteArray();
//
//
//        ISVNEditor editor = repository.getCommitEditor("Patching POM", null);
//        editor.openRoot(-1);
//        editor.openDir("", -1);
//        editor.openFile(filePath, -1);
//        editor.applyTextDelta(filePath, null);
//        SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
//        String checksum = deltaGenerator.sendDelta(filePath,
//                new ByteArrayInputStream(oldData), 0,
//                new ByteArrayInputStream(newData), editor, true);
//        editor.closeFile(filePath, checksum);
//        editor.closeDir();
//        editor.closeDir();
//        editor.closeEdit();

        //repository.checkout();

    }

    private static void m2(String url, SVNRepository repository) {

//        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
//        repository.setAuthenticationManager(authManager);


        try {
            /*
             * Checks up if the specified path/to/repository part of the URL
             * really corresponds to a directory. If doesn't the program exits.
             * SVNNodeKind is that one who says what is located at a path in a
             * revision. -1 means the latest revision.
             */
            SVNNodeKind nodeKind = repository.checkPath("", -1);
            if (nodeKind == SVNNodeKind.NONE) {
                System.err.println("There is no entry at '" + url + "'.");
                System.exit(1);
            } else if (nodeKind == SVNNodeKind.FILE) {
                System.err.println("The entry at '" + url + "' is a file while a directory was expected.");
                System.exit(1);
            }
            /*
             * getRepositoryRoot() returns the actual root directory where the
             * repository was created. 'true' forces to connect to the repository
             * if the root url is not cached yet.
             */
            System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
            /*
             * getRepositoryUUID() returns Universal Unique IDentifier (UUID) of the
             * repository. 'true' forces to connect to the repository
             * if the UUID is not cached yet.
             */
            System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
            System.out.println("");

            /*
             * Displays the repository tree at the current path - "" (what means
             * the path/to/repository directory)
             */
            listEntries(repository, "");
        } catch (SVNException svne) {
            System.err.println("error while listing entries: "
                    + svne.getMessage());
            System.exit(1);
        }
        /*
         * Gets the latest revision number of the repository
         */
        long latestRevision = -1;
        try {
            latestRevision = repository.getLatestRevision();
        } catch (SVNException svne) {
            System.err
                    .println("error while fetching the latest repository revision: "
                            + svne.getMessage());
            System.exit(1);
        }
        System.out.println("");
        System.out.println("---------------------------------------------");
        System.out.println("Repository latest revision: " + latestRevision);
        System.exit(0);
    }

    /*
     * Initializes the library to work with a repository via
     * different protocols.
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();

        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }

    /*
    * Called recursively to obtain all entries that make up the repository tree
    * repository - an SVNRepository which interface is used to carry out the
    * request, in this case it's a request to get all entries in the directory
    * located at the path parameter;
    *
    * path is a directory path relative to the repository location path (that
    * is a part of the URL used to create an SVNRepository instance);
    *
    */
    public static void listEntries(SVNRepository repository, String path)
            throws SVNException {
        /*
         * Gets the contents of the directory specified by path at the latest
         * revision (for this purpose -1 is used here as the revision number to
         * mean HEAD-revision) getDir returns a Collection of SVNDirEntry
         * elements. SVNDirEntry represents information about the directory
         * entry. Here this information is used to get the entry name, the name
         * of the person who last changed this entry, the number of the revision
         * when it was last changed and the entry type to determine whether it's
         * a directory or a file. If it's a directory listEntries steps into a
         * next recursion to display the contents of this directory. The third
         * parameter of getDir is null and means that a user is not interested
         * in directory properties. The fourth one is null, too - the user
         * doesn't provide its own Collection instance and uses the one returned
         * by getDir.
         */
        Collection entries = repository.getDir(path, -1, null,
                (Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            System.out.println("/" + (path.equals("") ? "" : path + "/")
                    + entry.getName() + " (author: '" + entry.getAuthor()
                    + "'; revision: " + entry.getRevision() + "; date: " + entry.getDate() + ")");
            /*
             * Checking up if the entry is a directory.
             */
            if (entry.getKind() == SVNNodeKind.DIR) {
                listEntries(repository, (path.equals("")) ? entry.getName()
                        : path + "/" + entry.getName());
            }
        }
    }

}
