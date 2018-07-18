import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RenameDir {
    private static String path = "D:\\JavaProj\\FolderMonitor440P\\sorted\\jey";
    private static String log = path + "\\logMonitor440P";

    public static void rename(String path, String newName) {
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter the path of directory to rename.");
        String dirPath = path;
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            System.err.println("There is no directory @ given path");
            //System.exit(0);
            //алерт и в лог "попытка переименовнаия не дирреткории, а файла"
        }
        String newDirName = newName;
        File newDir = new File(dir.getParent() + "/" + newDirName);
        boolean isRenamed = dir.renameTo(newDir);
        //System.out.println(isRenamed);
        if(isRenamed){
            //отписываем в лог
            logWriter(log,getDayTimeNow()+ " " + dirPath + " переименован в архивную папку");

        }
        else{
            //вызываем алерт, что не получилось
            AlertWindow.display("Ошибка переименования","Не получилось переименовать. Проверьте существует ли папка.");
            //и отписать в лог
            logWriter(log,getDayTimeNow()+ " " + dirPath + " не получилось переименовать");

        }
    }

    //дописывание в файл лога
    private static void logWriter(String path, String text) {
        File file = new File(path);

        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Проблемы с лог файлом");
            AlertWindow.display("Сообщите разработчику!","Проблемы с лог файлом при создании.");
            e.printStackTrace();
        }

        FileWriter fr = null;
        try {
            fr = new FileWriter(file,true);
            fr.write(text+"\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
                AlertWindow.display("Сообщите разработчику!","Проблемы с лог файлом при закрытии.");
            }
        }
    }

    //текущая дата и время в формате yyyyMMdd HH:mm:ss
    public static String getDayTimeNow(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss.S");
        return format1.format(cal.getTime());
    }
}
