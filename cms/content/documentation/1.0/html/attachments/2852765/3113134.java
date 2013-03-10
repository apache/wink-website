package myPackage;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.atom.AtomFeed;
import org.apache.wink.common.model.atom.AtomLink;
import org.apache.wink.common.model.atom.AtomPerson;
import org.apache.wink.common.model.atom.AtomText;
import org.apache.wink.common.model.atom.AtomTextType;

/**
 * Servlet implementation class ProduceAtom
 */
public class ProduceAtomUsingWink extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProduceAtomUsingWink() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
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

        AtomFeed.marshal(feed, response.getOutputStream());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

}
