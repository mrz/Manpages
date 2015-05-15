package mrz.android.manpages.model.entities;

import com.google.gson.Gson;

import io.realm.RealmObject;

public class Archive extends RealmObject {

    private String version;

    private String uri;

    private String filename;

    private String path;

    private boolean downloaded;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public Archive() {
        setDownloaded(false);
    }

    public static Archive fromJson(CharSequence json) {
        Archive archive = new Gson().fromJson(json.toString(), Archive.class);

        archive.setDownloaded(false);

        return archive;
    }
}
