package gitlet;

import java.io.Serializable;
import java.io.File;
import static gitlet.Utils.*;

public class Blobs implements Serializable {
    private String name;
    private String content;

    public Blobs(File file) {
        this.content = readContentsAsString(file);
        this.name = file.getName();
    }

    public String makeBlob() {
        String uid = sha1(getName() + getContent());
        File blobFile = Repository.makeObject(getName() + getContent());
        writeObject(blobFile, this);
        return uid;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}