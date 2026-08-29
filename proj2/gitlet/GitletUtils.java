package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class GitletUtils {
    public static void init() {
        Repository.createRepository();
        Index.initIndex();
        File firstCommit = Commit.createFirstCommit();
        File branch = Branch.createBranch("master", firstCommit);
        Ref.changeHeadRef(branch);
    }

    public static void addFile(String fileName) {
        if (!Repository.isFileExists(fileName)) {
            MainMethods.exit("File does not exist.");
        }
        Blobs blob = new Blobs(fileName);
        if (Repository.isBlobExists(blob.getUid())) {
            return;
        }
        String blobFile = blob.makeBlob();
        Index.add(fileName, blobFile);
    }

    public static void rmFile(String fileName) {
        if (Index.inCurrentIndex(fileName)) {
            Index.removeAddFile(fileName);
        } else if (Commit.inCurrentCommit(fileName)) {
            Index.addRemove(fileName);
        } else {
            MainMethods.exit("No reason to remove the file.");
        }
    }

    public static void commit(String msg) {
        File headCommitFile = Ref.returnHeadCommit();
        String parentUid = Utils.readObject(headCommitFile, Commit.class).getUid();
        File commitFile = Commit.createCommit(msg, parentUid, null);
        File headBranch = Ref.returnHeadBranch();
        Branch.branchRepoint(headBranch, commitFile);
    }

    public static boolean isTrucked(File file) {
        Blobs blob = new Blobs(file);
        File blobFile = Utils.join(Repository.BLOB, blob.getUid());
        return blobFile.exists();
    }

    public static void checkUntrack(Blobs blobs) {
        String fileName = blobs.getName();
        File file = Utils.join(Repository.CWD, fileName);
        if (file.exists() & !isTrucked(file)) {
            MainMethods.exit("There is an untracked file in the way; "
                             + "delete it, or add and commit it first.");
        }
    }

    public static void checkUntrack(Branch branch) {
        Commit commit = branch.getCommit();
        for (String fileName : commit.getMap2File().keySet()) {
            File file = Utils.join(Repository.CWD, fileName);
            if (file.exists()) {
                Blobs fileBlob = new Blobs(file);
                checkUntrack(fileBlob);
            }
        }
    }

    public static void checkoutBranch(String branchName) {
        if (Ref.returnHeadBranch().getName().equals(branchName)) {
            MainMethods.exit("No need to checkout the current branch.");
        }
        Branch branch = Repository.findBranch(branchName);
        checkUntrack(branch);
        Commit commit = branch.getCommit();
        HashMap<String, String> map2File = commit.getMap2File();
        for (String blobName : map2File.keySet()) {
            Blobs blob = Repository.findBlob(map2File.get(blobName));
            File targetFile = Utils.join(Repository.CWD, blobName);
            Utils.writeContents(targetFile, blob.getContent());
        }
        Ref.changeHeadRef(Utils.join(Repository.BRANCH, branchName));
    }

    public static void checkoutFile(String fileName) {
        File blobFile = Ref.returnHeadCommit();
        Commit commit = Utils.readObject(blobFile, Commit.class);
        Blobs blob = Repository.findBlob(commit.getMap2File().get(fileName));
        File file = Utils.join(Repository.CWD, fileName);
        Utils.writeContents(file, blob.getContent());
    }

    public static void checkoutFileInCommit(String commitName, String fileName) {
        Commit targetCommit = Repository.findCommit(commitName);
        String blobId = targetCommit.getMap2File().get(fileName);
        Blobs targetBlob = Repository.findBlob(blobId);
        File targetFile = Utils.join(Repository.CWD, fileName);
        Utils.writeContents(targetFile, targetBlob.getContent());
    }

    public static void printLog() {
        Commit commit = Utils.readObject(Ref.returnHeadCommit(), Commit.class);
        while (commit.getParent() != null) {
            commit.logPrint();
            commit = Repository.findCommit(commit.getParent());
        }
        commit.logPrint();
    }

    public static void printStatus() {
        Branch.printBranch();
        Index.printIndex();
    }

    public static void printGlobalLog() {
        List<String> commitList = Utils.plainFilenamesIn(Repository.COMMIT);
        for (String commitName : commitList) {
            File commitFile = Utils.join(Repository.COMMIT, commitName);
            Commit commit = Utils.readObject(commitFile, Commit.class);
            commit.logPrint();
        }
    }

    public static void find(String msg) {
        boolean findFlag = false;
        List<String> commitList = Utils.plainFilenamesIn(Repository.COMMIT);
        for (String commitName : commitList) {
            File commitFile = Utils.join(Repository.COMMIT, commitName);
            Commit commit = Utils.readObject(commitFile, Commit.class);
            if (commit.getMessage().equals(msg)) {
                System.out.println(commit.getUid());
                findFlag = true;
            }
        }
        if (!findFlag) {
            MainMethods.exit("Found no commit with that message.");
        }
    }
}
