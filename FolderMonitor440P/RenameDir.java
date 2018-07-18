import java.io.*;
import java.util.List;

class RenameDir {
    private static String log = FolderMonitor440P.log;

    static boolean rename(String path, String newName) {
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
            LogWriter.write(log,path + " переименован в архивную папку. "+GetDayTime.now() );
        }
        else{
            if(newDir.exists()){//такая архивная уже существует
                //System.out.println("dir.getName(): " + dir.getName() + " dir.getPath(): " + dir.getPath());
                List<String> copiedFilesList = FolderMonitor440P.getFileNamesList(dir.getPath());
                boolean noErr = true;
                for (String s: copiedFilesList
                        ) {
                    //System.out.println(s);
                    try {
                        removeFile(new File(dir.getPath()+"/"+s),dir.getParent() + "/" + newName+"/");
                    } catch (IOException e) {
                        LogWriter.write(log,"Ошибка слияния по "+dir.getParent() + "/" + newName+" "+GetDayTime.now());
                        noErr = false;
                    }
                }

                if(noErr){
                    isRenamed = true;
                    File fDel = new File(dir.getPath()); //удаление исходной папки, если не было ошибок
                    fDel.delete();
                    LogWriter.write(log,path + " слияние архивных в " +newName+ " " +GetDayTime.now());
                }
                else{//какая-то ошибка, надо разибраться
                    AlertWindow.display("Ошибка переименования","Не получилось переименовать. Проверьте вручную.\r\n"+FolderMonitor440P.path);
                    LogWriter.write(log,path + " не получилось переименовать, при дублировании"+" "+GetDayTime.now());
                }
            }
            else{//скорее всего уже нет переименовываемой папки
                AlertWindow.display("Ошибка переименования","Не получилось переименовать. Проверьте существует ли папка\r\n(нажав Обновить)");
                LogWriter.write(log,path + " не получилось переименовать, дублирования папки не было"+" "+GetDayTime.now());
            }
        }
        return isRenamed;
    }

    //метод по переносу файлов
    private static void removeFile(File src, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        if(dest.equals(null))throw new IOException();//когда не найден хозяин kwt, например
        File toPath = new File(dest);
        if(!toPath.exists()) toPath.mkdirs();

        File to = new File(dest + src.getName());
        if(to.exists()){ //обработка ситуации дублирования
            String tempTime = GetDayTime.now().replace(":","_").replace(".","_");
            LogWriter.write(log,"Дублирование: "+ dest + src.getName() +" Файл заменён, старая копия в папке: " + "copyOld_"+src.getName().substring(0,3)+"_"+tempTime);
            //System.out.println(dest+"copyOld_"+src.getName().substring(0,3)+"_"+tempTime+"/");
            //System.out.println(dest+src.getName());
            File oldFile = new File(dest+src.getName());
            removeFile(oldFile, dest+"copyOldMonMerge_"+src.getName().substring(0,3)+"_"+tempTime+"/");//копирование старого файла в новую папку oldCopyMonMerge-типа
            //перепеши изменение tempTime через RegEx
            //throw new IOException();//раньше пробрасывал эксепшен, давал в лог сообщение "обрабатывай вручную" и переноса не происходило
        }
        try{
            //if(src.getName().contains("BNP")) throw new IOException(); //test exception
            is = new FileInputStream(src);
            os = new FileOutputStream(to);
            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer))>0){
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }

        File fDel = new File(src.toString()); //удаление исходного если не возникло Exception
        fDel.delete();

    }
}
