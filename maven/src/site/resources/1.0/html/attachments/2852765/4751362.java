package myPackage;

import java.util.Date;
import java.util.List;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Collection;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Service;
import org.apache.abdera.model.Workspace;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;

public class APP_Client_Abdera {
    private static String     SERVICE_URL = "http://localhost:8080/APP_Server_Abdera/blog";
    private static String     FEED_URL    = "http://localhost:8080/APP_Server_Abdera/blog/entries";
    private static String     ENTRY_URL   = "http://localhost:8080/APP_Server_Abdera/blog/entries/kan_ocr";

    private static Abdera abdera      = new Abdera();

    public static void main(String[] args) {
        System.out.println("--- I. Contents of Service Document at " + SERVICE_URL + " ---");
        getServiceDocument();

        System.out.println("--- II. Contents of Feed Document at " + FEED_URL + " ---");
        getEntries();

        Entry newEntry = createNewEntry();
        System.out.println("--- III. Posting a new entry into the Feed ---");
        postEntry(newEntry);
        System.out.println("--- IV. Contents of Feed Document after Post ---");
        getEntries();

        Entry changedEntry = changeEntry(newEntry);
        System.out.println("--- V. Changing the last entry posted ---");
        putEntry(changedEntry);
        System.out.println("--- VI. Entry content after change ---");
        getEntry();

        System.out.println("--- VII. Deleting the last entry posted ---");
        deleteEntry();
        System.out.println("--- VIII. Contents of Feed Document after Delete ---");
        getEntries();
    }

    private static void getServiceDocument() {
        AbderaClient abderaClient = new AbderaClient(abdera);
        Document<Service> introspection = abderaClient.get(SERVICE_URL).getDocument();
        Service service = introspection.getRoot();
        List<Workspace> workspaces = service.getWorkspaces();
        for (Workspace workspace : workspaces) {
            System.out.println("\t" + workspace.getTitle());
            List<Collection> collections = workspace.getCollections();
            for (Collection collection : collections) {
                System.out.println("\t" + collection.getTitle() + "\t:\t" + collection.getHref());
            }
            System.out.print("\n");
        }
    }

    private static void getEntries() {
        AbderaClient abderaClient = new AbderaClient(abdera);
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=feed");
        ClientResponse response = abderaClient.get(FEED_URL, opts);
        Feed feed = (Feed)response.getDocument().getRoot();
        int counter = 1;
        System.out.println("\t" + feed.getTitle() + "\n");
        List<Entry> entries = feed.getEntries();
        for (Entry atomEntry : entries) {
            System.out.print("\t" + counter++ + ") ");
            displayEntry(atomEntry);
        }
    }

    private static Entry createNewEntry() {
        Factory factory = abdera.getFactory();
        Entry entry = factory.newEntry();
        entry.setId("kan_ocr");
        entry.setTitle("New algorithm to detect and split merged characters in Kannada OCR");
        entry.addLink(FEED_URL + "/kan_ocr");
        entry.setUpdated(new Date());
        entry.setPublished(new Date());
        entry.setSummary("The new algorithm to detect and split merged characters in " +
                        "Kannada OCR is yeilding good results. We will soon be publishing the " +
                        "algorithm and results in one of the upcoming conferences.");
        entry.addAuthor("ShivaHR");
        return entry;
    }

    private static Entry changeEntry(Entry curEntry) {
        curEntry.setTitle("A new algorithm to detect and split merged characters in Kannada OCR will be published soon!");
        return curEntry;
    }

    private static void postEntry(Entry newEntry) {
        AbderaClient abderaClient = new AbderaClient(abdera);
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");
        ClientResponse response = abderaClient.post(FEED_URL, newEntry, opts);
        if (response.getStatus() == 201) {
            System.out.println("\t" + "Post successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatus()
                + "\n");
        }
    }

    private static void putEntry(Entry changedEntry) {
        AbderaClient abderaClient = new AbderaClient(abdera);
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");
        ClientResponse response = abderaClient.put(ENTRY_URL, changedEntry.getDocument(), opts);
        if (response.getStatus() == 200) {
            System.out.println("\t" + "Change successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatus()
                + "\n");
        }
    }

    private static void getEntry() {
        AbderaClient abderaClient = new AbderaClient(abdera);
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");
        ClientResponse response = abderaClient.get(ENTRY_URL, opts);
        Entry entry = (Entry)response.getDocument().getRoot();
        System.out.print("\t");
        displayEntry(entry);
    }

    private static void deleteEntry() {
        AbderaClient abderaClient = new AbderaClient(abdera);
        ClientResponse response = abderaClient.delete(ENTRY_URL);
        if (response.getStatus() == 204) {
            System.out.println("\t" + "Delete successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatus()
                + "\n");
        }
    }

    private static void displayEntry(Entry atomEntry) {
        System.out.println(atomEntry.getTitle());
        System.out.println("\t" + atomEntry.getUpdated().toString());
        System.out.println("\t" + atomEntry.getSummary() + "\n");
    }
}
