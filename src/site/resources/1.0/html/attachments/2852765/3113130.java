package myPackage;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

/**
 * Servlet implementation class ProduceAtom
 */
public class ProduceAtomUsingAbdera extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProduceAtomUsingAbdera() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Abdera abdera = new Abdera();
        Feed feed = abdera.newFeed();

        feed.setId("tag:example.org,2007:/foo");
        feed.setTitle("Test Feed");
        feed.setSubtitle("Feed subtitle");
        feed.setUpdated(new Date());
        feed.addAuthor("Shiva HR");
        feed.addLink("http://example.com");
        feed.addLink("http://example.com/foo", "self");

        Entry entry = feed.addEntry();
        entry.setId("tag:example.org,2007:/foo/entries/1");
        entry.setTitle("Entry title");
        entry.setSummaryAsHtml("<p>This is the entry title</p>");
        entry.setUpdated(new Date());
        entry.setPublished(new Date());
        entry.addLink("http://example.com/foo/entries/1");

        feed.getDocument().writeTo(response.getWriter());
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
