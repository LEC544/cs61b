package gitlet;

import java.io.File;
import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author joshua lee
 */
public class Repository {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File BLOB = join(GITLET_DIR, "blob");
    public static final File COMMIT = join(GITLET_DIR, "commit");
    public static final File INDEX = join(GITLET_DIR, "index");
    public static final File HEAD = join(GITLET_DIR, "head");
    public static final File BRANCH = join(GITLET_DIR, "branch");

    /** Create and Init the Repository **/
    public static void createRepository() {
        GITLET_DIR.mkdir();
        BLOB.mkdir();
        BRANCH.mkdir();
        COMMIT.mkdir();
    }

    public static File makeCommit(String commmitUid) {
        return join(Repository.COMMIT, commmitUid);
    }

    public static boolean isCommitExists(String commitUid) {
        if (commitUid.isEmpty()) {
            return false;
        }
        return join(COMMIT, commitUid).exists();
    }

    public static File makeBlob(String blobUid) {
        return join(Repository.BLOB, blobUid);
    }

    public static boolean isBlobExists(String blobUid) {
        if (blobUid.isEmpty()) {
            return false;
        }
        return join(BLOB, blobUid).exists();
    }

    public static boolean isFileExists(String fileName) {
        if (fileName.isEmpty()) {
            return false;
        }
        return join(CWD, fileName).exists();
    }

    public static Blobs findBlob(String blobName) {
        if (blobName.isEmpty()) {
            MainMethods.exit("File does not exist in that commit.");
        }
        File blobFile = join(BLOB, blobName);
        if (!blobFile.exists()) {
            MainMethods.exit("File does not exist in that commit.");
        }
        Blobs blob = readObject(blobFile, Blobs.class);
        return blob;
    }

    public static Commit findCommit(String commitUid) {
        if (commitUid.isEmpty()) {
            MainMethods.exit("No commit with that id exists.");
        }
        File commitFile = join(COMMIT, commitUid);
        if (!commitFile.exists()) {
            MainMethods.exit("No commit with that id exists.");
        }
        Commit commit = readObject(commitFile, Commit.class);
        return commit;
    }

    public static Branch findBranch(String branchName) {
        if (branchName.isEmpty()) {
            MainMethods.exit("No such branch exists.");
        }
        File branchFile = join(BRANCH, branchName);
        if (!branchFile.exists()) {
            MainMethods.exit("No such branch exists.");
        }
        Branch branch = readObject(branchFile, Branch.class);
        return branch;
    }
}
