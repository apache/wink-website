package myPackage;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.apache.wink.common.model.rss.RssChannel;
import org.apache.wink.common.model.rss.RssFeed;
import org.apache.wink.common.model.rss.RssItem;

public class ConsumeRssUsingWink {

    public static void main(String[] args) {
        System.out.println("Consuming RSS Documents using Apache Wink...\n");
        RestClient client = new RestClient();
        String url = "http://www.rssboard.org/files/sample-rss-2.xml";
        Resource resource = client.resource(url);
        RssFeed rss = resource.accept(MediaType.APPLICATION_XML).get(RssFeed.class);
        RssChannel channel = rss.getChannel();
        System.out.println("Title: " + channel.getTitle());
        System.out.println("Description: " + channel.getDescription() + "\n");
        int itemCount = 0;
        for (RssItem item : channel.getItems()) {
            System.out.println("Item " + ++itemCount + ":");
            System.out.println("\tTitle: " + item.getTitle());
            System.out.println("\tPublish Date: " + item.getPubDate());
            System.out.println("\tDescription: " + item.getDescription());
        }
    }

}
