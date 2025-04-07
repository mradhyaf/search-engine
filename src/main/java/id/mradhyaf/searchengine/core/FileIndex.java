package id.mradhyaf.searchengine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates file indexing
 */
public class FileIndex {

    private static final Logger LOG = LoggerFactory.getLogger(FileIndex.class);

    Connection database;

    FileIndex() {
        try {
            if (!Files.isDirectory(Path.of("run"))) {
                Files.createDirectory(Path.of("run"));
            }
        } catch (IOException e) {
            LOG.error("error when creating run directory");
            throw new RuntimeException(e);
        }

        try {
            database = DriverManager.getConnection("jdbc:sqlite:" + Path.of("run/index.db"));
        } catch (SQLException e) {
            LOG.error("error when connecting to database");
            throw new RuntimeException(e);
        }
    }

    public void initTable() {
        try (Statement statement = database.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS files");
            statement.execute("CREATE TABLE IF NOT EXISTS files (id INTEGER PRIMARY KEY, name TEXT, path TEXT, type TEXT);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void put(FileInfo fileInfo) {
        try (PreparedStatement statement = database.prepareStatement("INSERT INTO files (name,path,type) VALUES (?,?,?);")) {
            statement.setString(1, fileInfo.name);
            statement.setString(2, fileInfo.absolutePath.toString());
            statement.setString(3, fileInfo.type);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error("unable to put file: {}", fileInfo.absolutePath);
            throw new RuntimeException(e);
        }
    }

    public List<FileInfo> getByName(String name) {
        String query = "SELECT * FROM files WHERE name LIKE '%" + name + "%'";
        try (Statement statement = database.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            ArrayList<FileInfo> rows = new ArrayList<>();
            while (result.next()) {
                FileInfo row = getCurrentRow(result);
                rows.add(row);
            }

            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public FileInfo getByPath(String path) {
        String query = "SELECT * FROM files WHERE path LIKE '%" + path + "%';";
        try (Statement statement = database.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (!result.next()) {
                return null;
            }

            return getCurrentRow(result);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private FileInfo getCurrentRow(ResultSet resultSet) {
        try {
            FileInfo row = new FileInfo();
            row.name = resultSet.getString("name");
            row.absolutePath = Path.of(resultSet.getString("path"));
            row.type = resultSet.getString("type");

            return row;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
