import java.io.File;

public class RenameDir {
    public static void rename(String path, String newName) {
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter the path of directory to rename.");
        String dirPath = path;
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            System.err.println("There is no directory @ given path");
            System.exit(0);
        }
        String newDirName = newName;
        File newDir = new File(dir.getParent() + "/" + newDirName);
        dir.renameTo(newDir);
        //System.out.println("Done");
    }
}
