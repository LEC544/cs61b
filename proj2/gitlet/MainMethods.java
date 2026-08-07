package gitlet;

import java.io.File;

public class MainMethods {
    public static void exit(String msg) {
        System.out.println(msg);
        java.lang.System.exit(1);
    }

    public static void corretArgumentNumber(int expect, int actual) {
        if (expect != actual) {
            exit("wrong argument numbers!!!");
        }
    }

    public static void argumentNumberRange(int min, int max, int actual) {
        if (actual < min || actual > max) {
            exit("wrong argument numbers!!!");
        }
    }

    public static boolean inited() {
        return Repository.GITLET_DIR.exists();
    }

    public static void init(String[] args) {
        int argLength = args.length;
        if (inited()) {
            exit("A Gitlet version-control system already exists in the current directory.");
        }
        corretArgumentNumber(1, argLength);
        Repository.createRepository();
        Index.initIndex();
        File commitFile = Commit.createCommit("initial commit", null);
        Branch.createBranch("master", commitFile);
        Ref.changeHeadRef(Utils.join(Repository.BRANCH, "master"));
    }

    public static void add(String[] args) {
        if (!inited()) {
            exit("must inited");
        }
        corretArgumentNumber(2, args.length);
        File f = Utils.join(Repository.CWD, args[1]);
        if (!f.exists()) {
            exit("file doesn't exist");
        }
        Blobs b = new Blobs(f);
        String blobFile = b.makeBlob();
        Index.add(args[1], blobFile);
    }

    public static void commit(String[] args) {
        if (!inited()) {
            exit("must inited");
        }
        corretArgumentNumber(2, args.length);
        File commitFile = Commit.createCommit(args[1], Utils.readObject(Ref.returnHeadCommit(), Commit.class).getUid());
        Branch.branchChange(Ref.returnHeadBranch().getName(), commitFile);
    }

    public static void checkout(String[] args) {
        if (!inited()) {
            exit("must inited");
        }
        argumentNumberRange(2, 4, args.length);
        switch (args.length) {
            case 2:
                String branchName = args[1];
                Branch branch = Repository.findBranch(branchName);
                Commit commit = Utils.readObject(branch.getRef2Commit().getPointer(), Commit.class);
                for (String blobName : commit.getMap2File().keySet()) {
                    Blobs blob = Repository.findBlob(commit.getMap2File().get(blobName));
                    File targetFile = Utils.join(Repository.CWD, blobName);
                    Utils.writeContents(targetFile, blob.getContent());
                }
                break;
            case 3:
                if (!args[1].equals("--")) {
                    exit("wrong statement");
                }
                String fileName = args[2];
                File file = Utils.join(Repository.CWD, fileName);
                File BlobFile = Ref.returnHeadCommit();
                Commit c = Utils.readObject(BlobFile, Commit.class);
                Blobs b = Repository.findBlob(c.getMap2File().get(fileName));
                Utils.writeContents(file, b.getContent());
                break;
            case 4:
                if (!args[2].equals("--")) {
                    exit("wrong statement");
                }
                String commitName = args[1];
                String target = args[3];
                Commit targetCommit = Repository.findCommit(commitName);
                String BlobId = targetCommit.getMap2File().get(target);
                Blobs targetBlob = Repository.findBlob(BlobId);
                File targetFile = Utils.join(Repository.CWD, target);
                Utils.writeContents(targetFile, targetBlob.getContent());
                break;
        }
    }

    public static void log(String[] args) {
        if (!inited()) {
            exit("must inited");
        }
        corretArgumentNumber(1, args.length);
        Commit c = Utils.readObject(Ref.returnHeadCommit(), Commit.class);
        while (c.getParent() != null) {
            c.logprint();
            c = Repository.findCommit(c.getParent());
        }
        c.logprint();
    }
}