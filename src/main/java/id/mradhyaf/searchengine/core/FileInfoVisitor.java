package id.mradhyaf.searchengine.core;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Consumer;

public class FileInfoVisitor implements FileVisitor<Path> {

    private static final Logger LOG = LoggerFactory.getLogger(FileInfoVisitor.class);

    private final Consumer<FileInfo> fileInfoConsumer;

    FileInfoVisitor(Consumer<FileInfo> fileInfoConsumer) {
        this.fileInfoConsumer = fileInfoConsumer;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes basicFileAttributes) throws
            IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
        LOG.debug("visiting file {}", path.toString());
        FileInfo fileInfo = new FileInfo();
        fileInfo.name = path.getFileName().toString();
        fileInfo.absolutePath = path;
        fileInfo.type = Files.getFileExtension(fileInfo.name);

        fileInfoConsumer.accept(fileInfo);

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path path, IOException e) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException e) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
