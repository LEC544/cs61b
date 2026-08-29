package gitlet;

public class MainMethods {
    /** exit and print msg **/
    public static void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    /** exit if actual number don't match expect **/
    public static void correctArgumentNumber(int expect, int actual) {
        if (expect != actual) {
            exit("Incorrect operands.");
        }
    }

    /** exit if actual number is out of the range **/
    public static void correctArgumentNumber(int min, int max, int actual) {
        if (actual < min || actual > max) {
            exit("Incorrect operands.");
        }
    }

    public static boolean inited() {
        return Repository.GITLET_DIR.exists();
    }

    public static void shouldInited() {
        if (!inited()) {
            exit("Not in an initialized Gitlet directory.");
        }
    }

    public static void shouldNotInited() {
        if (inited()) {
            exit("A Gitlet version-control system already exists in the current directory.");
        }
    }

    /**judge that is the dir inited and is the arg number
     * in the correct range,then init it if pass**/
    public static void init(String[] args) {
        shouldNotInited();
        correctArgumentNumber(1, args.length);
        gitletUtils.init();
    }

    /**add the file to index**/
    public static void add(String[] args) {
        shouldInited();
        correctArgumentNumber(2, args.length);
        String fileName = args[1];
        gitletUtils.addFile(fileName);
    }

    /**remove the file in index if the file is added or remove
     * the file in the commit if the file is tracked in the
     * current commit**/
    public static void rm(String[] args) {
        shouldInited();
        correctArgumentNumber(2, args.length);
        String fileName = args[1];
        gitletUtils.rmFile(fileName);
    }

    /**commit**/
    public static void commit(String[] args) {
        shouldInited();
        if (args.length < 2 || args[1].isEmpty()) {
            exit("Please enter a commit message.");
        }
        correctArgumentNumber(2, args.length);
        if (!Index.isChanged()) {
            exit("No changes added to the commit.");
        }
        String msg = args[1];
        gitletUtils.commit(msg);
    }

    public static void checkout(String[] args) {
        shouldInited();
        correctArgumentNumber(2, 4, args.length);
        switch (args.length) {
            case 2:
                // checkout branch
                String branchName = args[1];
                gitletUtils.checkoutBranch(branchName);
                break;
            case 3:
                // checkout file
                if (!args[1].equals("--")) {
                    exit("Incorrect operands.");
                }
                String fileName = args[2];
                gitletUtils.checkoutFile(fileName);
                break;
            case 4:
                // checkout commit file
                if (!args[2].equals("--")) {
                    exit("Incorrect operands.");
                }
                String commitName = args[1];
                String target = args[3];
                gitletUtils.checkoutFileInCommit(commitName, target);
                break;
        }
    }

    public static void log(String[] args) {
        shouldInited();
        correctArgumentNumber(1, args.length);
        gitletUtils.printLog();
    }

    public static void status(String[] args) {
        shouldInited();
        correctArgumentNumber(1, args.length);
        gitletUtils.printStatus();
    }
}
