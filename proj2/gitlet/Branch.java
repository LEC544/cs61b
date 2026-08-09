package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import static gitlet.Utils.*;

public class Branch implements Serializable {
    private Ref ref2Commit;

    public Branch(Ref ref) {
        this.ref2Commit = ref;
    }

    public static void createBranch(String branchName, File commitName) {
        File branchFile = join(Repository.BRANCH, branchName);
        if (branchFile.exists()) {
            MainMethods.exit("branch already created");
        }
        try {
            branchFile.createNewFile();
        } catch (Exception ignore) {

        }
        Branch branch = new Branch(new Ref(commitName));
        writeObject(branchFile, branch);
    }

    public static void branchChange(String branchName, File commitFile) {
        File branchFile = join(Repository.BRANCH, branchName);
        Branch branch = new Branch(new Ref(commitFile));
        writeObject(branchFile, branch);
    }

    public static void printBranch() {
        System.out.println("=== Branches ===");
        List<String> branchList = Utils.plainFilenamesIn(Repository.BRANCH);
        String headBranchName = Ref.returnHeadBranch().getName();
        System.out.println("*"+headBranchName);
        for (String branchName : branchList) {
            if (!branchName.equals(headBranchName)) {
                System.out.println(branchName);
            }
        }
        System.out.println();
    }

    public Ref getRef2Commit() {
        return ref2Commit;
    }
}