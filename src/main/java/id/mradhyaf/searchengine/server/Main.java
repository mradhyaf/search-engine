package id.mradhyaf.searchengine.server;

import id.mradhyaf.searchengine.server.services.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.server.RatpackServer;

import java.nio.file.Path;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final String[] REQUIRED_ENV = {"SEARCH_ROOT", "SEARCH_SERVER_PORT"};

    static void validateEnv(String key) {
        if (System.getenv(key) != null) {
            return;
        }

        LOG.error("missing {} in environment", key);
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        for (String env : REQUIRED_ENV) {
            validateEnv(env);
        }

        SearchService searchService = new SearchService(Path.of(System.getenv("SEARCH_ROOT")));

        try {
            RatpackServer.start(ratpackServerSpec -> ratpackServerSpec
                    .serverConfig(serverConfigBuilder -> serverConfigBuilder
                            .port(Integer.parseInt(System.getenv("SEARCH_SERVER_PORT"))))
                    .registryOf(registrySpec -> registrySpec
                            .add(searchService))
                    .handlers(new Router()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
