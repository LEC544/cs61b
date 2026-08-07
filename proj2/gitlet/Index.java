package gitlet;

import java.io.Serializable;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.*;

public class Index implements Serializable {
    private HashMap<String, String> addIndex;
    private HashSet<String> removeIndex;

    public Index() {
        this.addIndex = new HashMap<>();
        this.removeIndex = new HashSet<>();
    }

    public static void initIndex() {
        writeObject(Repository.INDEX, new Index());
    }

    public static void add(String name, String blob) {
        Index i = readObject(Repository.INDEX, Index.class);
        i.addIndex.put(name, blob);
        writeObject(Repository.INDEX, i);
    }

    public static HashMap<String, String> commitIndex() {
        Index i = readObject(Repository.INDEX, Index.class);
        Commit parent = readObject(Ref.returnHeadCommit(), Commit.class);
        HashMap<String, String> hashMap = parent.getMap2File();
        for (String s : i.getAddIndex().keySet()) {
            hashMap.put(s, i.addIndex.get(s));
        }
        initIndex();
        return hashMap;
    }

    public HashMap<String, String> getAddIndex() {
        return addIndex;
    }

    public HashSet<String> getRemoveIndex() {
        return removeIndex;
    }
}