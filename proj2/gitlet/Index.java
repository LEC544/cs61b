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

    public static void removeAddFile(String fileName) {
        Index i = readObject(Repository.INDEX, Index.class);
        i.getAddIndex().remove(fileName);
        writeObject(Repository.INDEX, i);
    }

    public static void addRemove(String fileName) {
        Index i = readObject(Repository.INDEX, Index.class);
        i.getRemoveIndex().add(fileName);
        writeObject(Repository.INDEX, i);
    }

    public static HashMap<String, String> commitIndex() {
        Index i = readObject(Repository.INDEX, Index.class);
        Commit parent = readObject(Ref.returnHeadCommit(), Commit.class);
        HashMap<String, String> hashMap = parent.getMap2File();
        for (String s : i.getAddIndex().keySet()) {
            hashMap.put(s, i.addIndex.get(s));
        }
        for (String s : i.getRemoveIndex()) {
            hashMap.remove(s);
        }
        initIndex();
        return hashMap;
    }

    public static void printIndex() {
        System.out.println("=== Staged Files ===");
        Index i = readObject(Repository.INDEX, Index.class);
        for (String addFile : i.getAddIndex().keySet()) {
            System.out.println(addFile);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removeFile : i.getRemoveIndex()) {
            System.out.println(removeFile);
        }
        System.out.println();
    }

    public static boolean isChanged() {
        Index i = readObject(Repository.INDEX, Index.class);
        return !i.getAddIndex().isEmpty() || !i.getRemoveIndex().isEmpty();
    }

    public static boolean inCurrentIndex(String fileName) {
        Index i = readObject(Repository.INDEX, Index.class);
        return i.getAddIndex().containsKey(fileName);
    }

    public HashMap<String, String> getAddIndex() {
        return addIndex;
    }

    public HashSet<String> getRemoveIndex() {
        return removeIndex;
    }
}