package id.mradhyaf.searchengine.server.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import id.mradhyaf.searchengine.core.FileInfo;

public class SearchItem {

    @JsonProperty
    String filename;

    @JsonProperty
    String endpoint;

    public static SearchItem fromFileInfo(FileInfo fileInfo) {
        SearchItem val = new SearchItem();
        val.filename = fileInfo.name;
        val.endpoint = fileInfo.relativePath.toString();

        return val;
    }
}
