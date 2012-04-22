package myPackage;

import java.io.IOException;
import java.net.URL;

import org.apache.abdera.Abdera;
import org.apache.abdera.contrib.rss.RssFeed;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.parser.ParseException;
import org.apache.abdera.parser.Parser;

public class ConsumeRssUsingAbdera {

    public static void main(String[] args) throws ParseException, IOException {
        System.out.println("Consuming RSS Documents using Abdera...\n");
        Abdera abdera = new Abdera();
        Parser parser = abdera.getParser();
        URL url = new URL("http://www.rssboard.org/files/sample-rss-2.xml");
        Document<RssFeed> doc = parser.parse(url.openStream());
        RssFeed rssFeed = doc.getRoot();
        System.out.println("Title: " + rssFeed.getTitle());
        System.out.println("Description: " + rssFeed.getSubtitle() + "\n");
        int itemCount = 0;
        for (Entry entry : rssFeed.getEntries()) {
            System.out.println("Item " + ++itemCount + ":");
            System.out.println("\tTitle: " + entry.getTitle());
            System.out.println("\tPublish Date: " + entry.getPublished());
            System.out.println("\tDescription: " + entry.getContent());
        }
    }

}
