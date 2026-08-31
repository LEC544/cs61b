package gitlet;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class GitletUtils {
    private static final int SHORTCOMMITID = 8;
    private static final int LONGCOMMITID = 40;

    private static String getCommitID(String commitID) {
        String realCommitId = "";
        if (commitID.length() == LONGCOMMITID) {
            realCommitId = commitID;
        } else if (commitID.length() == SHORTCOMMITID) {
            List<String> commitList = Utils.plainFilenamesIn(Repository.COMMIT);
            for (String commit : commitList) {
                if (commit.substring(0, 8).equals(commitID)) {
                    realCommitId = commit;
                }
            }
        } else {
            realCommitId = "";
        }
        return realCommitId;
    }

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
            String blobUid = blob.getUid();
            String currentUid = Ref.returnHeadCommit().getMap2File().get(fileName);
            if (!currentUid.equals(blobUid)) {
                Index.add(fileName, blobUid);
            }
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
        File headCommitFile = Ref.returnHeadCommitFile();
        String parentUid = Utils.readObject(headCommitFile, Commit.class).getUid();
        File commitFile = Commit.createCommit(msg, parentUid, null);
        File headBranch = Ref.returnHeadBranchFile();
        Branch.branchRepoint(headBranch, commitFile);
    }

    public static void mergeCommit(String branchName) {
        String firstUid = Ref.returnHeadCommit().getUid();
        String secondeUid = Repository.findBranch(branchName).getCommit().getUid();
        String currentName = Ref.returnHeadBranchFile().getName();
        String msg = "Merged " + branchName + " into " + currentName + ".";
        File commitFile = Commit.createCommit(msg, firstUid, secondeUid);
        File headBranch = Ref.returnHeadBranchFile();
        Branch.branchRepoint(headBranch, commitFile);
    }

    public static boolean isTrucked(File file) {
        Commit currentCommit = Ref.returnHeadCommit();
        return currentCommit.getMap2File().containsKey(file.getName());
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
        checkUntrack(commit);
    }

    public static void checkUntrack(Commit commit) {
        for (String fileName : commit.getMap2File().keySet()) {
            File file = Utils.join(Repository.CWD, fileName);
            if (file.exists()) {
                Blobs fileBlob = new Blobs(file);
                checkUntrack(fileBlob);
            }
        }
    }

    public static void checkoutBranch(String branchName) {
        if (Ref.returnHeadBranchFile().getName().equals(branchName)) {
            MainMethods.exit("No need to checkout the current branch.");
        }
        Branch branch = Repository.findBranch(branchName);
        checkUntrack(branch);
        Commit commit = branch.getCommit();
        commit.replace();
        Ref.changeHeadRef(Utils.join(Repository.BRANCH, branchName));
    }

    public static void checkoutFile(String fileName) {
        File blobFile = Ref.returnHeadCommitFile();
        Commit commit = Utils.readObject(blobFile, Commit.class);
        Blobs blob = Repository.findBlob(commit.getMap2File().get(fileName));
        File file = Utils.join(Repository.CWD, fileName);
        Utils.writeContents(file, blob.getContent());
    }

    public static void checkoutFileInCommit(String commitName, String fileName) {
        String realCommitName = getCommitID(commitName);
        Commit targetCommit = Repository.findCommit(realCommitName);
        String blobId = targetCommit.getMap2File().get(fileName);
        Blobs targetBlob = Repository.findBlob(blobId);
        File targetFile = Utils.join(Repository.CWD, fileName);
        Utils.writeContents(targetFile, targetBlob.getContent());
    }

    public static void printLog() {
        Commit commit = Ref.returnHeadCommit();
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

    public static void createBranch(String branchName) {
        File commitFile = Ref.returnHeadCommitFile();
        Branch.createBranch(branchName, commitFile);
    }

    public static void removeBranch(String branchName) {
        Branch.removeBranch(branchName);
    }

    public static void resetCommit(String commitId) {
        String realCommit = getCommitID(commitId);
        Commit commit = Repository.findCommit(realCommit);
        File commitFile = Utils.join(Repository.COMMIT, realCommit);
        checkUntrack(commit);
        commit.replace();
        Branch.branchRepoint(Ref.returnHeadBranchFile(), commitFile);
        Index.initIndex();
    }

    public static void mergeBranch(String branchName) {
        if (Index.isChanged()) {
            MainMethods.exit("You have uncommitted changes.");
        }
        if (!Utils.join(Repository.BRANCH, branchName).exists()) {
            MainMethods.exit("A branch with that name does not exist.");
        }
        if (Ref.returnHeadBranchFile().getName().equals(branchName)) {
            MainMethods.exit("Cannot merge a branch with itself.");
        }
        Branch branch = Repository.findBranch(branchName);
        Commit branchCommit = branch.getCommit();
        Commit currentCommit = Ref.returnHeadCommit();
        Commit splitPoint = Commit.findAncestor(currentCommit, branchCommit);
        List<String> fileList = Utils.plainFilenamesIn(Repository.CWD);
        for (String fileName : fileList) {
            File file = Utils.join(Repository.CWD, fileName);
            if (!isTrucked(file)) {
                String branchBlobUid = branchCommit.getMap2File().get(fileName);
                String spiltPointBlobUid = splitPoint.getMap2File().get(fileName);
                if (branchBlobUid == null) {
                    continue;
                } else if (branchBlobUid.equals(spiltPointBlobUid)) {
                    continue;
                } else {
                    MainMethods.exit("There is an untracked file in the way;"
                                     + " delete it, or add and commit it first.");
                }
            }
        }
        if (splitPoint == null) {
            MainMethods.exit("can't find the ancestor commit");
        }
        if (splitPoint.getUid().equals(branchCommit.getUid())) {
            Utils.message("Given branch is an ancestor of the current branch.");
        } else if (splitPoint.getUid().equals(currentCommit.getUid())) {
            checkoutBranch(branchName);
            Utils.message("Current branch fast-forwarded.");
        } else {
            HashSet<String> totalFileName = new HashSet<>();
            totalFileName.addAll(currentCommit.getMap2File().keySet());
            totalFileName.addAll(branchCommit.getMap2File().keySet());
            totalFileName.addAll(splitPoint.getMap2File().keySet());
            for (String file : totalFileName) {
                String currentBlobUid = currentCommit.getMap2File().get(file);
                String branchBlobUid = branchCommit.getMap2File().get(file);
                String splitBlobUid = splitPoint.getMap2File().get(file);
                mergeWithUid(currentBlobUid, branchBlobUid, splitBlobUid, file);
            }
            mergeCommit(branchName);
        }
    }

    private static void mergeWithUid(String current, String branch, String split, String file) {
        if (current != null & branch != null & split != null) {
            if (!current.equals(branch)
                    & !branch.equals(split)
                    & !split.equals(current)) {
                conflict(current, branch, file);
            } else if (current.equals(branch)) {
                return;
            } else if (branch.equals(split)) {
                return;
            } else {
                String content = Repository.findBlob(branch).getContent();
                Utils.writeContents(Utils.join(Repository.CWD, file), content);
                addFile(file);
            }
        } else {
            if (current == null) {
                if (branch == null) {
                    return;
                } else if (split == null) {
                    String content = Repository.findBlob(branch).getContent();
                    Utils.writeContents(Utils.join(Repository.CWD, file), content);
                    addFile(file);
                } else {
                    if (branch.equals(split)) {
                        return;
                    } else {
                        conflict(current, branch, file);
                    }
                }
            } else if (branch == null) {
                if (split == null) {
                    return;
                } else {
                    if (split.equals(current)) {
                        rmFile(file);
                    } else {
                        conflict(current, branch, file);
                    }
                }
            } else {
                if (current.equals(branch)) {
                    return;
                } else {
                    conflict(current, branch, file);
                }
            }
        }
    }

    private static void conflict(String current, String branch, String file) {
        Utils.message("Encountered a merge conflict.");
        String currentContent = "";
        String branchContent = "";
        if (current != null) {
            Blobs blob = Repository.findBlob(current);
            currentContent = blob.getContent();
        }
        if (branch != null) {
            Blobs blob = Repository.findBlob(branch);
            branchContent = blob.getContent();
        }
        String fileContent = "<<<<<<< HEAD\n"
                + currentContent
                + "=======\n"
                + branchContent
                + ">>>>>>>\n";
        Utils.writeContents(Utils.join(Repository.CWD, file), fileContent);
        addFile(file);
    }
}
