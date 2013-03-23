package myPackage;

import java.util.Date;
import java.util.List;

import javax.activation.MimeType;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.StreamBuilder;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.protocol.server.ProviderHelper;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.ResponseContext;
import org.apache.abdera.protocol.server.TargetType;
import org.apache.abdera.protocol.server.context.BaseResponseContext;
import org.apache.abdera.protocol.server.context.EmptyResponseContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractCollectionAdapter;
import org.apache.abdera.util.MimeTypeHelper;

/**
 * The Collection Adapter is the piece that actually implements the business
 * logic of the AtomPub server. It bridges the protocol with the backend
 * persistence.
 */
public class APP_CollectionAdapter extends AbstractCollectionAdapter {
    private static String FEED_TAG = "entries";
    private Document      feedDocument;

    public ResponseContext getFeed(RequestContext request) {
        Abdera abdera = request.getAbdera();
        Document feed = getFeedDocument(abdera);
        return new BaseResponseContext(feed);
    }

    public ResponseContext postEntry(RequestContext request) {
        Abdera abdera = request.getAbdera();
        try {
            Document entryDocument = constructEntryDocument(request, abdera);
            if (entryDocument != null) {
                Entry entry = (Entry)entryDocument.getRoot();
                manageAnEntry(entry, null, abdera);
                BaseResponseContext rc = new BaseResponseContext(entry);
                IRI baseUri = ProviderHelper.resolveBase(request);
                rc.setLocation(baseUri.resolve(FEED_TAG + "/" + entry.getId()).toString());
                rc.setStatus(201); //generate a HTTP 201 response in case of success
                return rc;
            } else {
                return new EmptyResponseContext(400);
            }
        } catch (ParseException pe) {
            return new EmptyResponseContext(415);
        } catch (ClassCastException cce) {
            return new EmptyResponseContext(415);
        } catch (Exception e) {
            return new EmptyResponseContext(400);
        }
    }

    public ResponseContext getEntry(RequestContext request) {
        Entry entry = (Entry)getAbderaEntry(request);
        if (entry != null) {
            Document entryDocument = ((Entry)entry.clone()).getDocument();
            return new BaseResponseContext(entryDocument);
        } else {
            return new EmptyResponseContext(404);
        }
    }

    public ResponseContext deleteEntry(RequestContext request) {
        Entry entry = getAbderaEntry(request);
        if (entry != null) {
            entry.discard();
            return new EmptyResponseContext(204);
        } else {
            return new EmptyResponseContext(404);
        }
    }

    public ResponseContext putEntry(RequestContext request) {
        Entry origEntry = getAbderaEntry(request);
        if (origEntry != null) {
            try {
                Abdera abdera = request.getAbdera();
                Document newEntryDocument = constructEntryDocument(request, abdera);
                if (newEntryDocument != null) {
                    Entry newEntry = (Entry)newEntryDocument.getRoot();
                    manageAnEntry(newEntry, origEntry, abdera);
                    return new BaseResponseContext(newEntry);
                } else {
                    return new EmptyResponseContext(400);
                }
            } catch (ParseException pe) {
                return new EmptyResponseContext(415);
            } catch (ClassCastException cce) {
                return new EmptyResponseContext(415);
            } catch (Exception e) {
                if ("409".equalsIgnoreCase(e.getMessage())) {
                    return new EmptyResponseContext(409);
                } else {
                    return new EmptyResponseContext(400);
                }
            }
        } else {
            return new EmptyResponseContext(404);
        }
    }

    private synchronized Document getFeedDocument(Abdera abdera) {
        if (feedDocument == null) {
            feedDocument = initializeFeedDocument(abdera);
        }
        return feedDocument;
    }

    private Document initializeFeedDocument(Abdera abdera) {
        StreamBuilder out = (StreamBuilder)abdera.getWriterFactory().newStreamWriter("fom");
        out.startDocument().startFeed().writeId("http://mile.ee.iisc.ernet.in")
            .writeTitle("Latest developments at MILE Lab, IISc :")
            .writeUpdated(new Date())
            .writeLink(FEED_TAG)
            .writeCategory("lab-updates");

        out
            .startEntry()
            .writeId("tts_demo")
            .writeTitle("Kannada and Tamil language TTS Web Demo")
            .writeLink(FEED_TAG + "/tts_demo")
            .writeUpdated(new Date())
            .writePublished(new Date())
            .writeEdited(new Date())
            .writeSummary("Kannada/Tamil language Text-To-Speech (TTS) Demo by MILE Lab, IISc is " +
            		"available at http://mile.ee.iisc.ernet.in:8080/tts_demo/ ")
            .writeAuthor("ShivaHR").endEntry();

        out
            .startEntry()
            .writeId("tamil_ocr")
            .writeTitle("Tamil OCR achieves 94.3% accuracy on 1000 scanned pages")
            .writeLink(FEED_TAG + "/tamil_ocr")
            .writeUpdated(new Date())
            .writePublished(new Date())
            .writeEdited(new Date())
            .writeSummary("Tamil OCR (Optical Character Recognizer) being developed at MILE Lab, IISc " +
            		"achieves an accuracy of 94.3%! The testing was carried out by CDAC-Pune on " +
            		"1000 pages scanned from books printed between 1950-2000.")
            .writeAuthor("ShivaHR").endEntry();

        Document doc = (Document)out.endFeed().endDocument().getBase();

        return doc;
    }

    /**
     * Create a skeletal entry, to be filled up later
     */
    private Document constructEntryDocument(RequestContext request, Abdera abdera) throws Exception {
        MimeType contentType = request.getContentType();
        if (contentType != null && !MimeTypeHelper.isAtom(contentType.toString())) {
            throw new ParseException();
        }
        return (Document)request.getDocument(abdera.getParser()).clone();
    }

    /*
     * Utility method to manage the creation or update of an entry.
     */
    private void manageAnEntry(Entry currentEntry, Entry originalEntry, Abdera abdera)
        throws Exception {
        IRI id;
        if (originalEntry != null) {
            id = originalEntry.getId();
            if (!currentEntry.getId().equals(id)) {
                throw new Exception("409");
            }
            originalEntry.discard();
        } else {
            id = currentEntry.getId();
            if (id == null) {
                id = new IRI(abdera.getFactory().newUuidUri());
                currentEntry.getIdElement().setValue(id.toString());
            }
        }
        currentEntry.setUpdated(new Date());
        String hrefValue = FEED_TAG + "/" + id;
        List<Link> links = currentEntry.getLinks();
        //links.clear(); //doesn't work - throws a java.lang.UnsupportedOperationException!
        if (links.size() > 0) {
            links.get(0).setHref(hrefValue);
        } else {
            currentEntry.addLink(hrefValue);
        }
        Feed feed = (Feed)getFeedDocument(abdera).getRoot();
        feed.insertEntry(currentEntry);
        feed.setUpdated(new Date());
    }

    private Entry getAbderaEntry(RequestContext request) {
        Abdera abdera = request.getAbdera();
        Document feed = getFeedDocument(abdera);
        try {
            Feed currentFeed = (Feed)feed.getRoot();
            return currentFeed.getEntry(getEntryID(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The id of the entry is the last token in the URL. Ugly way of extracting
     * it, till I find a better mechanism.
     */
    private String getEntryID(RequestContext request) {
        if (request.getTarget().getType() != TargetType.TYPE_ENTRY) {
            return null;
        }
        String path = request.getUri().toString();
        String[] segments = path.split("/");
        return segments[segments.length - 1];
    }

    public String getAuthor(RequestContext request) throws ResponseContextException {
        return "ShivaHR";
    }

    public String getId(RequestContext request) {
        return "blog/entries";
    }

    public String getTitle(RequestContext request) {
        return "Entries";
    }

}
