package id.mradhyaf.searchengine.server;

import id.mradhyaf.searchengine.server.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.server.RatpackServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Path searchRoot = Path.of(System.getProperty("user.home"), "search-root");
        try {
            Files.createDirectories(searchRoot);
        } catch (IOException e) {
            LOG.error("unable to find search-root directory");
            throw new RuntimeException(e);
        }
        LOG.info("search engine searching in: {}", searchRoot.toAbsolutePath());

        SearchService searchService = new SearchService(searchRoot);

        try {
            RatpackServer.start(ratpackServerSpec -> ratpackServerSpec
                    .serverConfig(serverConfigBuilder -> serverConfigBuilder
                            .port(5050))
                    .registryOf(registrySpec -> registrySpec
                            .add(searchService))
                    .handlers(new Router()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
