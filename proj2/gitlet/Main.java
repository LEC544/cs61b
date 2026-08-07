package gitlet;

import static gitlet.MainMethods.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            exit("at least one argument");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                init(args);
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                add(args);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                commit(args);
                break;
            case "checkout":
                checkout(args);
                break;
            case "log":
                log(args);
                break;
        }
    }
}
