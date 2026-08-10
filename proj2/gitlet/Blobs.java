package gitlet;

import java.io.Serializable;
import java.io.File;
import static gitlet.Utils.*;

public class Blobs implements Serializable {
    private String name;
    private String content;
    private String uid;

    public Blobs(File file) {
        this.content = readContentsAsString(file);
        this.name = file.getName();
        this.uid = sha1(this.name + this.content);
    }

    public String computeUid() {
        return sha1(getName() + getContent());
    }

    public String makeBlob() {
        String uid = computeUid();
        File blobFile = Repository.makeObject(this.uid);
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