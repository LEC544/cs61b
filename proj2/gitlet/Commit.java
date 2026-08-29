package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author joshua
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */
    private String message;
    private Date date;
    private String parent;
    private String secondParent;
    private String uid;
    private HashMap<String, String> map2File;

    /** The message of this Commit. */

    public Commit(String msg, String parent, String secondParent) {
        this.message = msg;
        this.parent = parent;
        if (parent == null) {
            this.date = new Date(0);
            this.secondParent = null;
            this.map2File = new HashMap<>();
        } else {
            this.date = new Date();
            this.secondParent = secondParent;
            this.map2File = Index.commitIndex();
        }
        this.uid = sha1(getMessage() + getDate().toString() + getParent() + getSecondParent());
    }

    /** create a commit and return the file **/
    public static File createCommit(String msg, String parent, String secondParent) {
        Commit commit = new Commit(msg, parent, secondParent);
        File commitFile = Repository.makeCommit(commit.getUid());
        writeObject(commitFile, commit);
        return commitFile;
    }

    /** first commit **/
    public static File createFirstCommit() {
        return createCommit("initial commit", null, null);
    }

    public static boolean inCurrentCommit(String fileName) {
        Commit currentCommit = readObject(Ref.returnHeadCommit(), Commit.class);
        return currentCommit.getMap2File().containsKey(fileName);
    }

    public void logPrint() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        System.out.println("===");
        System.out.println("commit " + this.getUid());
        if (!(this.getSecondParent() == null)) {
            System.out.println("Merge: "
                    + Repository.findCommit(this.getParent()).getShort7Uid()
                    + " "
                    + Repository.findCommit(this.getSecondParent()).getShort7Uid());
        }
        System.out.println("Date: " + formatter.format(this.getDate()));
        System.out.println(this.getMessage());
        System.out.println();
    }

    public String getShort7Uid() {
        return getUid().substring(0, 7);
    }

    public String getSecondParent() {
        return secondParent;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getParent() {
        return parent;
    }

    public HashMap<String, String> getMap2File() {
        return map2File;
    }

    public String getUid() {
        return uid;
    }

}
