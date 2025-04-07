package id.mradhyaf.searchengine.server.handlers;

import id.mradhyaf.searchengine.core.FileInfo;
import id.mradhyaf.searchengine.core.SearchFilter;
import id.mradhyaf.searchengine.server.data.SearchItem;
import id.mradhyaf.searchengine.server.services.SearchService;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;
import ratpack.core.http.Request;
import ratpack.core.http.Status;
import ratpack.core.jackson.Jackson;
import ratpack.func.MultiValueMap;

import java.util.List;

public class SearchHandler implements Handler {

    @Override
    public void handle(Context ctx) throws Exception {
        Request req = ctx.getRequest();
        MultiValueMap<String, String> queryParams = req.getQueryParams();

        if (!queryParams.containsKey("q")) {
            ctx.getResponse().status(Status.BAD_REQUEST).send();
            return;
        }

        SearchService se = ctx.get(SearchService.class);
        SearchFilter filter = new SearchFilter();
        filter.name = queryParams.get("q");

        List<FileInfo> files = se.search(filter);
        List<SearchItem> result = files.stream().map(SearchItem::fromFileInfo).toList();

        ctx.render(Jackson.json(result));
    }
}
