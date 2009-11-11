package myPackage;

import org.apache.abdera.protocol.server.CollectionAdapter;
import org.apache.abdera.protocol.server.Provider;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.TargetType;
import org.apache.abdera.protocol.server.impl.AbstractWorkspaceProvider;
import org.apache.abdera.protocol.server.impl.RegexTargetResolver;
import org.apache.abdera.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera.protocol.server.impl.TemplateTargetBuilder;

public class APP_ContentProvider extends AbstractWorkspaceProvider implements Provider {
    public static String FEED_TAG = "entries";

    private final APP_CollectionAdapter collectionAdapter;

    public APP_ContentProvider() {
        this.collectionAdapter = new APP_CollectionAdapter();

        super.setTargetResolver(new RegexTargetResolver()
            .setPattern("/blog(\\?[^#]*)?", TargetType.TYPE_SERVICE)
            .setPattern("/blog/" + FEED_TAG + "(\\?[^#]*)?", TargetType.TYPE_COLLECTION)
            .setPattern("/blog/" + FEED_TAG + "/([^/#?]+)(\\?[^#]*)?", TargetType.TYPE_ENTRY));

        setTargetBuilder(new TemplateTargetBuilder()
            .setTemplate(TargetType.TYPE_SERVICE, "{target_base}/blog")
            .setTemplate(TargetType.TYPE_COLLECTION, "{target_base}/blog/{collection}{-opt|?|q,c,s,p,l,i,o}{-join|&|q,c,s,p,l,i,o}")
            .setTemplate(TargetType.TYPE_CATEGORIES, "{target_base}/blog/{collection};categories")
            .setTemplate(TargetType.TYPE_ENTRY, "{target_base}/blog/{collection}/{entry}"));

        SimpleWorkspaceInfo workspace = new SimpleWorkspaceInfo();
        workspace.setTitle("IISc MILE Lab Weblog");
        workspace.addCollection(collectionAdapter);
        addWorkspace(workspace);
    }

    public CollectionAdapter getCollectionAdapter(RequestContext request) {
        return collectionAdapter;
    }

}
