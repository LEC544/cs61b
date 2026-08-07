package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date date;
    private String parent;
    private HashMap<String, String> map2File;


    public Commit(String msg, String parent) {
        this.message = msg;
        this.parent = parent;
        if (parent == null) {
            this.date = new Date(0);
            this.map2File = new HashMap<>();
        } else {
            this.date = new Date();
            this.map2File = Index.commitIndex();
        }
    }

    public static File createCommit(String msg, String parent) {
        Commit commit = new Commit(msg, parent);
        File commitFile = Repository.makeObject(commit.getMessage() + commit.getDate().toString() + commit.getParent());
        writeObject(commitFile, commit);
        return commitFile;
    }

    public String getUid() {
        return sha1(getMessage() + getDate().toString() + getParent());
    }

    public void logprint() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH); //WHAT CAN I SAY
        System.out.println("===");
        System.out.println("commit " + this.getUid());
        System.out.println("Date: " + formatter.format(this.getDate()));
        System.out.println(this.getMessage());
        System.out.println();
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

    /* TODO: fill in the rest of this class. */
}
