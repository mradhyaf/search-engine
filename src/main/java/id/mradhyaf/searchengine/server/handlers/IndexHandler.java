package id.mradhyaf.searchengine.server.handlers;

import id.mradhyaf.searchengine.server.services.SearchService;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

public class IndexHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        SearchService searchService = ctx.get(SearchService.class);
        searchService.initIndex();

        ctx.getResponse().status(200).send();
    }
}
