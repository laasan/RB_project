import java.io.File;

class RenameDir {
    private static String log = FolderMonitor440P.log;

    static void rename(String path, String newName) {
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Enter the path of directory to rename.");
        File dir = new File(path);
        if (!dir.isDirectory()) {
            System.err.println("There is no directory @ given path");
            //System.exit(0);
            //алерт и в лог "попытка переименовнаия не дирреткории, а файла"
        }
        File newDir = new File(dir.getParent() + "/" + newName);
        boolean isRenamed = dir.renameTo(newDir);
        //System.out.println(isRenamed);
        if(isRenamed){
            //отписываем в лог
            LogWriter.write(log,GetDayTime.now()+ " " + path + " переименован в архивную папку");

        }
        else{
            //вызываем алерт, что не получилось
            AlertWindow.display("Ошибка переименования","Не получилось переименовать. Проверьте существует ли папка\r\n(нажав Обновить) и нет ли уже такой архивной.\r\nПри наличии архивной обработайте ситуацию вручную.\r\n"+FolderMonitor440P.path);
            //и отписать в лог
            LogWriter.write(log,GetDayTime.now()+ " " + path + " не получилось переименовать");

        }
    }
}
