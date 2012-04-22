package myPackage;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.model.atom.AtomEntry;
import org.apache.wink.common.model.atom.AtomFeed;

public class ConsumeAtomUsingWink {

    public static void main(String[] args) {
        System.out.println("Consuming Atom Documents using Apache Wink...\n");
        RestClient client = new RestClient();
        Resource resource = client.resource("http://alexharden.org/blog/atom.xml");
        AtomFeed feed = resource.accept(MediaType.APPLICATION_ATOM_XML).get(AtomFeed.class);
        System.out.println(feed.getTitle().getValue());
        for (AtomEntry entry : feed.getEntries()) {
            System.out.println("\t" + entry.getTitle().getValue());
        }
    }

}
