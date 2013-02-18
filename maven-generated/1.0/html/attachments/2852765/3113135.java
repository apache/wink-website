package myPackage;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.atom.AtomFeed;
import org.apache.wink.common.model.atom.AtomLink;
import org.apache.wink.common.model.atom.AtomPerson;
import org.apache.wink.common.model.atom.AtomText;
import org.apache.wink.common.model.atom.AtomTextType;

@Path("/getAtom")
public class ProduceAtom {

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public AtomFeed getFeed() {
        AtomFeed feed = new AtomFeed();
        feed.setId("tag:example.org,2007:/foo");
        feed.setTitle(new AtomText("Test Feed"));
        feed.setSubtitle(new AtomText("Feed subtitle"));
        feed.setUpdated(new Date());

        AtomPerson person = new AtomPerson();
        person.setName("Shiva HR");
        feed.getAuthors().add(person);

        AtomLink link1 = new AtomLink();
        link1.setHref("http://example.com");
        feed.getLinks().add(link1);

        AtomLink link2 = new AtomLink();
        link2.setHref("http://example.com/foo");
        link2.setRel("self");
        feed.getLinks().add(link2);

        AtomEntry entry = new AtomEntry();
        entry.setId("tag:example.org,2007:/foo/entries/1");
        entry.setTitle(new AtomText("Entry title"));

        AtomText summary = new AtomText();
        summary.setType(AtomTextType.html);
        summary.setValue("<p>This is the entry title</p>");
        entry.setSummary(summary);

        entry.setUpdated(new Date());
        entry.setPublished(new Date());

        AtomLink link3 = new AtomLink();
        link3.setHref("http://example.com/foo/entries/1");
        entry.getLinks().add(link3);

        feed.getEntries().add(entry);

        return feed;
    }
}
