package id.mradhyaf.searchengine.core;

import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileIndexTest {

    private FileIndex fileIndex;
    private Path tempDir;

    @BeforeEach
    void setUp() throws Exception {
        tempDir = Files.createTempDirectory("testdb");
        fileIndex = new FileIndex(tempDir);
        fileIndex.initTable();
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testInitTableCreatesTable() {
        assertDoesNotThrow(() -> fileIndex.initTable());
    }

    @Test
    void testPutAndGetByName() {
        FileInfo info = new FileInfo();
        info.name = "testFile";
        info.absolutePath = tempDir.resolve("testFile.txt");
        info.type = "text";

        fileIndex.put(info);

        List<FileInfo> results = fileIndex.getByName("testFile");

        assertEquals(1, results.size());
        FileInfo retrieved = results.getFirst();
        assertEquals(info.name, retrieved.name);
        assertEquals(info.absolutePath, retrieved.absolutePath);
        assertEquals(info.type, retrieved.type);
    }

    @Test
    void testPutAndGetByPath() {
        FileInfo info = new FileInfo();
        info.name = "example";
        info.absolutePath = tempDir.resolve("example.txt");
        info.type = "text";

        fileIndex.put(info);

        FileInfo retrieved = fileIndex.getByPath("example.txt");
        assertNotNull(retrieved);
        assertEquals(info.name, retrieved.name);
        assertEquals(info.absolutePath, retrieved.absolutePath);
        assertEquals(info.type, retrieved.type);
    }

    @Test
    void testGetByPathReturnsNullIfNotFound() {
        FileInfo result = fileIndex.getByPath("nonexistent.txt");
        assertNull(result);
    }
}
