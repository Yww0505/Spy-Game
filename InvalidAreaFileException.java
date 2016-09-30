
public class InvalidAreaFileException extends Exception {
    public static final String example =
"NODES\na\nb\nc\nd\ne\nf\n"+
"EDGES\n"+"a b 1\n"+"b c 2\n"+"c d 1\n"+"d e 3\n"+"e f 1\n"+"f a 1\n"+"a c 4\n"+"a d 20\n";
    public InvalidAreaFileException(String badLine) {
        super("InvalidAreaFilenameException\n"+
        "Example Format for 6 vertices and 8 edges:\n"+example);
        System.out.println(badLine+ " is invalid as vertex label or edge descriptor");
    }
}
