package id.mradhyaf.searchengine.server.services;

import id.mradhyaf.searchengine.core.Engine;
import ratpack.core.service.Service;
import ratpack.core.service.StartEvent;
import ratpack.core.service.StopEvent;

import java.nio.file.Path;

public class SearchService extends Engine implements Service {
    public SearchService(Path root) {
        super(root);
    }

    @Override
    public void onStart(StartEvent event) throws Exception {
        Service.super.onStart(event);
    }

    @Override
    public void onStop(StopEvent event) throws Exception {
        Service.super.onStop(event);
    }
}
