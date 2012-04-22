package myPackage;

import java.net.URI;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.annotations.Workspace;
import org.apache.wink.common.model.atom.AtomCategory;
import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.atom.AtomFeed;
import org.apache.wink.common.model.atom.AtomLink;
import org.apache.wink.common.model.atom.AtomPerson;
import org.apache.wink.common.model.atom.AtomText;

@Workspace(workspaceTitle = "IISc MILE Lab Weblog", collectionTitle = "Entries")
@Path("/entries")
public class EntriesCollection {
    private static AtomFeed feed = initializeFeedDocument("entries");

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public AtomFeed getFeed() {
        return feed;
    }

    @POST
    @Consumes(MediaType.APPLICATION_ATOM_XML)
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response postEntry(AtomEntry newEntry, @Context UriInfo uriInfo) {
        if (searchEntry(newEntry.getId()) != null) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }

        URI uri = uriInfo.getAbsolutePathBuilder().segment(newEntry.getId()).build();
        pruneEntry(newEntry, uri);
        feed.getEntries().add(0, newEntry);
        feed.setUpdated(new Date());

        return Response.status(Response.Status.CREATED).entity(newEntry).location(uri)
            .tag(new EntityTag(newEntry.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public AtomEntry getEntry(@PathParam("id") String entryId) {
        AtomEntry entry = searchEntry(entryId);
        if (entry == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return entry;
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_ATOM_XML)
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public AtomEntry putEntry(@PathParam("id") String entryId,
                              AtomEntry updatedEntry,
                              @Context UriInfo uriInfo) {
        AtomEntry oldEntry = getEntry(entryId);
        feed.getEntries().remove(oldEntry);
        pruneEntry(updatedEntry, uriInfo.getAbsolutePath());
        feed.getEntries().add(0, updatedEntry);
        feed.setUpdated(new Date());
        return updatedEntry;
    }

    @DELETE
    @Path("{id}")
    public void deleteEntry(@PathParam("id") String entryId) {
        AtomEntry oldEntry = getEntry(entryId);
        feed.getEntries().remove(oldEntry);
        feed.setUpdated(new Date());
    }

    private static AtomFeed initializeFeedDocument(String basePath) {
        AtomFeed feed = new AtomFeed();
        feed.setId("http://mile.ee.iisc.ernet.in");
        feed.setTitle(new AtomText("Latest developments at MILE Lab, IISc :"));
        feed.setUpdated(new Date());
        AtomLink link1 = new AtomLink();
        link1.setHref(basePath);
        feed.getLinks().add(link1);
        AtomCategory category1 = new AtomCategory();
        category1.setTerm("lab-updates");
        feed.getCategories().add(category1);

        AtomEntry entry1 = new AtomEntry();
        entry1.setId("tts_demo");
        entry1.setTitle(new AtomText("Kannada and Tamil language TTS Web Demo"));
        AtomLink link2 = new AtomLink();
        link2.setHref(basePath + "/" + entry1.getId());
        entry1.getLinks().add(link2);
        entry1.setUpdated(new Date());
        entry1.setPublished(new Date());
        //entry1.setEdited(new Date());
        entry1.setSummary(new AtomText("Kannada/Tamil language Text-To-Speech (TTS) Demo by MILE Lab, " +
        		"IISc is available at http://mile.ee.iisc.ernet.in:8080/tts_demo/ "));
        AtomPerson person1 = new AtomPerson();
        person1.setName("ShivaHR");
        entry1.getAuthors().add(person1);
        feed.getEntries().add(entry1);

        AtomEntry entry2 = new AtomEntry();
        entry2.setId("tamil_ocr");
        entry2.setTitle(new AtomText("Tamil OCR achieves 94.3% accuracy on 1000 scanned pages"));
        AtomLink link3 = new AtomLink();
        link3.setHref(basePath + "/" + entry2.getId());
        entry2.getLinks().add(link3);
        entry2.setUpdated(new Date());
        entry2.setPublished(new Date());
        //entry2.setEdited(new Date());
        entry2.setSummary(new AtomText("Tamil OCR (Optical Character Recognizer) being developed at " +
        		"MILE Lab, IISc achieves an accuracy of 94.3%! The testing was carried out by " +
        		"CDAC-Pune on 1000 pages scanned from books printed between 1950-2000."));
        AtomPerson person2 = new AtomPerson();
        person2.setName("ShivaHR");
        entry2.getAuthors().add(person2);
        feed.getEntries().add(entry2);
        return feed;
    }

    private AtomEntry searchEntry(String entryId) {
        for (AtomEntry entry : feed.getEntries()) {
            if (entry.getId().equals(entryId)) {
                return entry;
            }
        }
        return null;
    }

    private void pruneEntry(AtomEntry newEntry, URI uri) {
        newEntry.getLinks().clear();
        AtomLink link = new AtomLink();
        link.setHref(uri.toString());
        newEntry.getLinks().add(link);
        newEntry.setUpdated(new Date());
    }
}
