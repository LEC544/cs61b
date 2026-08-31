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

    public static File returnHeadCommitFile() {
        Ref ref = readObject(Repository.HEAD, Ref.class);
        Branch B = readObject(ref.getPointer(), Branch.class);
        ref = B.getRef2Commit();
        return ref.getPointer();
    }

    public static Commit returnHeadCommit() {
        return readObject(returnHeadCommitFile(), Commit.class);
    }

    public static File returnHeadBranchFile() {
        Ref ref = readObject(Repository.HEAD, Ref.class);
        return ref.getPointer();
    }

    public static Branch returnHeadBranch() {
        return readObject(returnHeadBranchFile(), Branch.class);
    }

    public File getPointer() {
        return pointer;
    }
}
