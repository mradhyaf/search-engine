package id.mradhyaf.searchengine.server;

import id.mradhyaf.searchengine.server.handlers.CorsHandler;
import id.mradhyaf.searchengine.server.handlers.IndexHandler;
import id.mradhyaf.searchengine.server.handlers.PageHandler;
import id.mradhyaf.searchengine.server.handlers.SearchHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Chain;
import ratpack.core.handling.RequestLogger;
import ratpack.func.Action;

public class Router implements Action<Chain> {

    private static final Logger LOG = LoggerFactory.getLogger(Router.class);

    @Override
    public void execute(Chain chain) throws Exception {
        chain
                .all(RequestLogger.ncsa())
                .all(new CorsHandler())
                .prefix("page", pageChain -> pageChain
                        .all(new PageHandler()))
                .get("index", new IndexHandler())
                .get("search", new SearchHandler())
                .all(ctx -> {
                    LOG.info("unhandled path: {}", ctx.getRequest().getPath());
                    ctx.notFound();
                });
    }
}
