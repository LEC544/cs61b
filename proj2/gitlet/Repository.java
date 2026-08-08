package gitlet;

import java.io.File;
import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File OBJECT = join(GITLET_DIR, "object");
    public static final File INDEX = join(GITLET_DIR, "index");
    public static final File HEAD = join(GITLET_DIR, "head");
    public static final File BRANCH = join(GITLET_DIR, "branches");


    /* TODO: fill in the rest of this class. */

    public static void createRepository() {
        GITLET_DIR.mkdir();
        OBJECT.mkdir();
        BRANCH.mkdir();
        try {
            HEAD.createNewFile();
            INDEX.createNewFile();
        } catch (Exception ignore) {

        }
    }

    public static File makeObject(String object) {
        String uid = sha1(object);
        File compressionPath = join(Repository.OBJECT, uid.substring(0, 2));
        if (!compressionPath.exists()) {
            compressionPath.mkdir();
        }
        File Object = join(compressionPath, uid.substring(2));
        if (Object.exists()) {
            return Object;
        }
        try {
            Object.createNewFile();
        } catch (Exception ignore) {

        }
        return  Object;
    }

    public static Blobs findBlob(String blobName) {
        File blobDir = join(OBJECT, blobName.substring(0, 2));
        File blobFile = join(blobDir, blobName.substring(2));
        if (!blobFile.exists()) {
            throw new GitletException("Can't Find This File");
        }
        Blobs b = readObject(blobFile, Blobs.class);
        return b;
    }

    public static Commit findCommit(String commitName) {
        File commitDir = join(OBJECT, commitName.substring(0, 2));
        File commitFile = join(commitDir,commitName.substring(2));
        if (!commitFile.exists()) {
            throw new GitletException("Can't Find This Branch");
        }
        Commit c = readObject(commitFile, Commit.class);
        return c;
    }

    public static Branch findBranch(String branchName) {
        File branchFile = join(BRANCH, branchName);
        Branch b = readObject(branchFile, Branch.class);
        return b;
    }
}
