package myPackage;

import java.io.IOException;
import java.net.URL;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

public class ConsumeAtomUsingAbdera {

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("Consuming Atom Documents using Abdera...\n");
        Abdera abdera = new Abdera();
        Parser parser = abdera.getParser();
        URL url = new URL("http://alexharden.org/blog/atom.xml");
        Document<Feed> doc = parser.parse(url.openStream());
        Feed feed = doc.getRoot();
        System.out.println(feed.getTitle());
        for (Entry entry : feed.getEntries()) {
            System.out.println("\t" + entry.getTitle());
        }
    }

}
