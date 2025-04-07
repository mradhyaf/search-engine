package id.mradhyaf.searchengine.server.handlers;

import id.mradhyaf.searchengine.core.FileInfo;
import id.mradhyaf.searchengine.server.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;
import ratpack.core.http.Request;

import java.nio.file.Path;

public class PageHandler implements Handler {
    private static final Logger LOG = LoggerFactory.getLogger(PageHandler.class);

    @Override
    public void handle(Context ctx) throws Exception {
        Request req = ctx.getRequest();
        Path path = Path.of(req.getPath().replaceFirst("page", ""));
        LOG.info("searching for page at path: {}", path);

        SearchService searchEngine = ctx.get(SearchService.class);
        FileInfo pageInfo = searchEngine.search(path);

        if (pageInfo == null) {
            ctx.notFound();
            return;
        }

        ctx.render(pageInfo.absolutePath);
    }
}
