import javax.swing.*;
import java.io.*;

public class fileCopier {
    public static void main(String[] args) throws IOException {
        
        int strAmount = 32;
        String[] pathFrom = new String[strAmount];
        String[] pathTo = new String[strAmount];
        String[] fileNames = new String[strAmount];

        pathsReader(strAmount,pathFrom,pathTo,fileNames);

        System.out.println("Обработано строк: "+strAmount);

        for(int i = 0; i<strAmount; i++){
            File from = new File(pathFrom[i] + fileNames[i]);
            File to = new File(pathTo[i] + fileNames[i]);

            File toPath = new File(pathTo[i]);
            if(!toPath.exists()){
                toPath.mkdirs();
                System.out.println("Создана новая папка: " + toPath);
            }

            if(!fileNames[i].equals(""))//если копируется файл, а не только папка
                copyFile(from,to);
        }
    }

    private static void copyFile(File src, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try{
            is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while((length = is.read(buffer))>0){
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
    }

    private static void pathsReader(int numFiles, String[] from, String[] to, String[] names){
        String[] temp;
        int i = 0;
        try {
            BufferedReader fReader = new BufferedReader(new InputStreamReader(new FileInputStream("param.txt"),"Windows-1251"));
            while(i < numFiles){
                temp = fReader.readLine().split("#");
                if(temp[0].equals("folder")) names[i] = "";
                   else names[i] = temp[0];
                from[i] = temp[1];
                to[i]= temp[2];
                i++;
            }

            fReader.close();
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Некорректная кодировка файла param.txt");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Не найден param.txt");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Ошибка считывания настроек из param.txt");
            e.printStackTrace();
        }
    }
}
