package myPackage;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.internal.utils.MediaTypeUtils;
import org.apache.wink.common.model.app.AppCollection;
import org.apache.wink.common.model.app.AppService;
import org.apache.wink.common.model.app.AppWorkspace;
import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.atom.AtomFeed;
import org.apache.wink.common.model.atom.AtomLink;
import org.apache.wink.common.model.atom.AtomPerson;
import org.apache.wink.common.model.atom.AtomText;

public class APP_Client_Wink {
    private static String     SERVICE_URL = "http://localhost:8080/APP_Server_Wink/blog/";
    private static String     FEED_URL    = "http://localhost:8080/APP_Server_Wink/blog/entries/";
    private static String     ENTRY_URL   = "http://localhost:8080/APP_Server_Wink/blog/entries/kan_ocr";

    private static RestClient restClient  = new RestClient();

    public static void main(String[] args) {
        System.out.println("--- I. Contents of Service Document at " + SERVICE_URL + " ---");
        getServiceDocument();

        System.out.println("--- II. Contents of Feed Document at " + FEED_URL + " ---");
        getEntries();

        AtomEntry newEntry = createNewEntry();
        System.out.println("--- III. Posting a new entry into the Feed ---");
        postEntry(newEntry);
        System.out.println("--- IV. Contents of Feed Document after Post ---");
        getEntries();

        AtomEntry changedEntry = changeEntry(newEntry);
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
        Resource resource = restClient.resource(SERVICE_URL);
        AppService service =
            resource.accept(MediaTypeUtils.ATOM_SERVICE_DOCUMENT).get(AppService.class);
        List<AppWorkspace> workspaces = service.getWorkspace();
        for (AppWorkspace workspace : workspaces) {
            System.out.println("\t" + workspace.getTitle().getValue());
            List<AppCollection> collections = workspace.getCollection();
            for (AppCollection collection : collections) {
                System.out.println("\t" + collection.getTitle().getValue()
                    + "\t:\t"
                    + collection.getHref());
            }
            System.out.print("\n");
        }
    }

    private static void getEntries() {
        Resource feedResource = restClient.resource(FEED_URL);
        AtomFeed feed = feedResource.accept(MediaType.APPLICATION_ATOM_XML).get(AtomFeed.class);
        int counter = 1;
        System.out.println("\t" + feed.getTitle().getValue() + "\n");
        List<AtomEntry> entries = feed.getEntries();
        for (AtomEntry atomEntry : entries) {
            System.out.print("\t" + counter++ + ") ");
            displayEntry(atomEntry);
        }
    }

    private static AtomEntry createNewEntry() {
        AtomEntry entry = new AtomEntry();
        entry.setId("kan_ocr");
        entry.setTitle(new AtomText("New algorithm to detect and split merged characters in Kannada OCR"));
        AtomLink link = new AtomLink();
        link.setHref(FEED_URL + "/" + entry.getId());
        entry.getLinks().add(link);
        entry.setUpdated(new Date());
        entry.setPublished(new Date());
        entry.setSummary(new AtomText("The new algorithm to detect and split merged characters in " +
        		"Kannada OCR is yeilding good results. We will soon be publishing the " +
        		"algorithm and results in one of the upcoming conferences."));
        AtomPerson person = new AtomPerson();
        person.setName("ShivaHR");
        entry.getAuthors().add(person);
        return entry;
    }

    private static AtomEntry changeEntry(AtomEntry curEntry) {
        curEntry.setTitle(new AtomText("A new algorithm to detect and split merged characters in Kannada OCR will be published soon!"));
        return curEntry;
    }

    private static void postEntry(AtomEntry newEntry) {
        Resource feedResource = restClient.resource(FEED_URL);
        ClientResponse response =
            feedResource.contentType(MediaType.APPLICATION_ATOM_XML).post(newEntry);
        if (response.getStatusCode() == 201) {
            System.out.println("\t" + "Post successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatusCode()
                + "\n");
        }
    }

    private static void putEntry(AtomEntry changedEntry) {
        Resource feedResource = restClient.resource(ENTRY_URL);
        ClientResponse response =
            feedResource.contentType(MediaType.APPLICATION_ATOM_XML).put(changedEntry);
        if (response.getStatusCode() == 200) {
            System.out.println("\t" + "Change successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatusCode()
                + "\n");
        }
    }

    private static void getEntry() {
        Resource feedResource = restClient.resource(ENTRY_URL);
        AtomEntry atomEntry = feedResource.accept(MediaType.APPLICATION_ATOM_XML).get(AtomEntry.class);
        System.out.print("\t");
        displayEntry(atomEntry);
    }

    private static void deleteEntry() {
        Resource feedResource = restClient.resource(ENTRY_URL);
        ClientResponse response = feedResource.delete();
        if (response.getStatusCode() == 204) {
            System.out.println("\t" + "Delete successful\n");
        } else {
            System.out.println("\t" + "Response code received from server = "
                + response.getStatusCode()
                + "\n");
        }
    }

    private static void displayEntry(AtomEntry atomEntry) {
        System.out.println(atomEntry.getTitle().getValue());
        System.out.println("\t" + atomEntry.getUpdated().toString());
        System.out.println("\t" + atomEntry.getSummary().getValue() + "\n");
    }
}
