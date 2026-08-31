package gitlet;


import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Commit currentCommit = Ref.returnHeadCommit();
        return currentCommit.getMap2File().containsKey(fileName);
    }

    public void replace() {
        List<String> fileList = Utils.plainFilenamesIn(Repository.CWD);
        for (String fileName : fileList) {
            if (!this.map2File.containsKey(fileName)) {
                File file = Utils.join(Repository.CWD, fileName);
                Utils.restrictedDelete(file);
            }
        }
        for (String blobName : this.map2File.keySet()) {
            Blobs blob = Repository.findBlob(this.map2File.get(blobName));
            File targetFile = Utils.join(Repository.CWD, blobName);
            Utils.writeContents(targetFile, blob.getContent());
        }
    }

    public static Commit findAncestor(Commit current, Commit given) {
        HashSet<String> currentAncestor = new HashSet<>();
        Deque<Commit> ancestorQue = new ArrayDeque<>();
        ancestorQue.addLast(current);
        while (!ancestorQue.isEmpty()) {
            Commit sentinel = ancestorQue.removeFirst();
            currentAncestor.add(sentinel.getUid());
            if (sentinel.getParent() != null) {
                ancestorQue.addLast(Repository.findCommit(sentinel.getParent()));
            }
            if (sentinel.getSecondParent() != null) {
                ancestorQue.addLast(Repository.findCommit(sentinel.getSecondParent()));
            }
        }
        ancestorQue.addLast(given);
        while (!ancestorQue.isEmpty()) {
            Commit sentinel = ancestorQue.removeFirst();
            if (currentAncestor.contains(sentinel.getUid())) {
                return sentinel;
            }
            if (sentinel.getParent() != null) {
                ancestorQue.addLast(Repository.findCommit(sentinel.getParent()));
            }
            if (sentinel.getSecondParent() != null) {
                ancestorQue.addLast(Repository.findCommit(sentinel.getSecondParent()));
            }
        }
        return null;
    }

    public void logPrint() {
        SimpleDateFormat formatter =
                new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
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
