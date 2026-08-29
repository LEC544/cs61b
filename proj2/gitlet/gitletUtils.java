package gitlet;

import java.io.File;
import java.util.HashMap;

public class gitletUtils {
    public static void init() {
        Repository.createRepository();
        Index.initIndex();
        File firstCommit = Commit.createFirstCommit();
        File branch = Branch.createBranch("master", firstCommit);
        Ref.changeHeadRef(branch);
    }

    public static void addFile(String fileName) {
        if(!Repository.isFileExists(fileName)) {
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

    public static void checkoutBranch(String branchName) {
        Branch branch = Repository.findBranch(branchName);
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
        File file = Utils.join(Repository.CWD, fileName);
        File BlobFile = Ref.returnHeadCommit();
        Commit c = Utils.readObject(BlobFile, Commit.class);
        Blobs b = Repository.findBlob(c.getMap2File().get(fileName));
        Utils.writeContents(file, b.getContent());
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
}