import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class LogWriter {
    //дописывание в файл лога
    static void write(String path, String text) {
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


}
