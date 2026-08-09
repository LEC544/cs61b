package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

public class Ref implements Serializable {
    private File pointer;

    public Ref() {
        this.pointer = null;
    }

    public Ref(File pointer) {
        this.pointer = pointer;
    }

    public static void changeHeadRef(File pointer) {
        Ref ref = new Ref(pointer);
        writeObject(Repository.HEAD, ref);
    }

    public static File returnHeadCommit() {
        Ref ref = readObject(Repository.HEAD, Ref.class);
        Branch B = readObject(ref.getPointer(), Branch.class);
        ref = B.getRef2Commit();
        return ref.getPointer();
    }

    public static File returnHeadBranch() {
        Ref ref = readObject(Repository.HEAD, Ref.class);
        return ref.getPointer();
    }

    public static void printDiff() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public File getPointer() {
        return pointer;
    }
}