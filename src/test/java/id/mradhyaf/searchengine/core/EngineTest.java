package id.mradhyaf.searchengine.core;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EngineTest {

    private Path tempRoot;
    private Engine engine;

    @BeforeEach
    void setUp() throws IOException {
        tempRoot = Files.createTempDirectory("engine-test");

        // Simulate a few files and folders
        Files.createDirectories(tempRoot.resolve("docs"));
        Files.createFile(tempRoot.resolve("docs/readme.txt"));
        Files.createFile(tempRoot.resolve("docs/intro.md"));
        Files.createFile(tempRoot.resolve("notes.txt"));

        engine = new Engine(tempRoot);
        engine.initIndex();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempRoot)
                .sorted((a, b) -> b.compareTo(a)) // delete children first
                .forEach(path -> path.toFile().delete());

        // Clean up the "run" directory used by FileIndex
        Path runDir = Paths.get("run");
        if (Files.exists(runDir)) {
            Files.walk(runDir)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> path.toFile().delete());
        }
    }

    @Test
    void testInitIndexIndexesAllFiles() {
        SearchFilter filter = new SearchFilter();
        filter.name = "readme";

        List<FileInfo> results = engine.search(filter);

        assertEquals(1, results.size());
        FileInfo file = results.get(0);
        assertEquals("readme.txt", file.name);
        assertEquals(Paths.get("docs/readme.txt"), file.relativePath);
    }

    @Test
    void testSearchByRelativePath() {
        Path relative = Paths.get("docs/intro.md");
        FileInfo result = engine.search(relative);

        assertNotNull(result);
        assertEquals("intro.md", result.name);
        assertEquals(tempRoot.resolve("docs/intro.md"), result.absolutePath);
    }

    @Test
    void testSearchWithBlankFilterReturnsEmpty() {
        SearchFilter filter = new SearchFilter();
        filter.name = "   "; // blank

        List<FileInfo> results = engine.search(filter);
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchWithNullFilterReturnsEmpty() {
        SearchFilter filter = new SearchFilter();
        filter.name = null;

        List<FileInfo> results = engine.search(filter);
        assertTrue(results.isEmpty());
    }
}
