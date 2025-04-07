package id.mradhyaf.searchengine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class Engine {

    private static final Logger LOG = LoggerFactory.getLogger(Engine.class);

    Path root;
    FileIndex fileIndex;

    public Engine(Path root) {
        this.root = root;
        this.fileIndex = new FileIndex();
    }

    public void initIndex() {
        LOG.info("starting file indexing");
        Instant start = Instant.now();

        fileIndex.initTable();
        FileVisitor<Path> visitor = new FileInfoVisitor(fileInfo -> {
            fileIndex.put(fileInfo);
        });

        try {
            Files.walkFileTree(root, visitor);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        Duration time = Duration.between(start, Instant.now());
        LOG.info("indexing time taken: {}", time);
    }

    public List<FileInfo> search(SearchFilter filter) {
        if (filter.name == null || filter.name.isBlank()) {
            return Collections.emptyList();
        }

        List<FileInfo> res = fileIndex.getByName(filter.name);
        res.forEach(item -> item.relativePath = root.relativize(item.absolutePath));

        return res;
    }

    public FileInfo search(Path relativePath) {
        Path absolutePath = root.resolve(relativePath);
        return fileIndex.getByPath(absolutePath.toString());
    }
}
