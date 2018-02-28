import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

public class klikoMon {
    public static void main(String... args) throws IOException {
        System.out.println("Проверка ежедневки: папок 101D,123D,135D в kliko с G:/BALANCE.DAY/pak");
        specCheck("http://192.168.21.25/ftp/F101D/","G:/BALANCE.DAY/pak");
        specCheck("http://192.168.21.25/ftp/F123D/","none");
        specCheck("http://192.168.21.25/ftp/F135D/","none");

        System.out.println();
        System.out.println();
        System.out.println("Проверка папки !!!new в kliko");
        Document doc;

        ArrayList<String[]> pathDates = new ArrayList<>();
        ArrayList<String[]> realData = new ArrayList<>();

        try {
            doc = Jsoup.connect("http://192.168.21.25/ftp/$new.htm").get();
            Elements dates= doc.select("td");
/*
            Elements urls = doc.select("a"); //парсим маяк "а"
            for(Element url : urls){ //перебираем все ссылки
                //... и вытаскиваем их название...
                System.out.println(url.attr("href"));
            }

            System.out.println();
*/
            for(int i = 0; i<dates.size(); i=i+2){
                String[] str = new String[2];
                //System.out.print(dates.get(i).text());
                //System.out.println(" " + dates.get(i+1).text());
                str[0] = dates.get(i).text();//путь
                str[1] = dates.get(i+1).text();//дата
                pathDates.add(str);
            }

            //System.out.println();
            //System.out.println("urls.size() = " + urls.size());
            //System.out.println("dates.size() = " + dates.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println();

        for(int i = 0; i < pathDates.size(); i++){
            String path = pathDates.get(i)[0];
            String strDate = pathDates.get(i)[1];
            String[] temp = new String[2];

            if(!path.substring(0,3).equals("!!!")&&!path.substring(0,3).equals("Пос")){
                //System.out.print(path+" ");
                //System.out.println(strDate.substring(1,strDate.length()-1));
                temp[0] = path.replace("\\","/");
                temp[1] = strDate.substring(7,11) + strDate.substring(4,6) + strDate.substring(1,3);
                realData.add(temp);
            }
        }

        System.out.println();
        for(String[] s:realData){
            System.out.print(s[0]+" ");
            System.out.println(s[1]);
        }

        //System.out.println(getDayToday());

        //для закачки:
        String downloadDay = JOptionPane.showInputDialog(
                null,
                "Введите дату скачиваемых файлов в формате yyyymmdd или none");
        if(!downloadDay.equals("none"))
            downloadDay(downloadDay,realData);

        JOptionPane.showMessageDialog(null, "Работа программы завершена");
    }

    public static void downloadDay(String date, ArrayList<String[]> pathDates) throws IOException {
        for (String[] str:pathDates) {
            if(str[1].equals(date)){
                copyFileUsingStream("http://192.168.21.25/ftp/"+str[0],"C:/!KLIKO_temp/kliko"+date);
                System.out.println("downloaded "+str[0]+" to C:/!KLIKO_temp/kliko"+date);
            }
        }
    }

    public static String getDayToday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(cal.getTime());
    }

    private static void copyFileUsingStream(String ADDRESS, String SAVE_DIR) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(ADDRESS);
            URLConnection conn = url.openConnection();
            is = conn.getInputStream();
            String fileName = new File(url.getPath()).getName();
            File parent = new File(SAVE_DIR);
            if (parent.isDirectory() || parent.mkdirs()) {
                os = new FileOutputStream(SAVE_DIR + File.separatorChar + fileName);
                int b;
                while ((b = is.read()) != -1) {
                    os.write(b);
                }
            } else {
                throw new IOException("Cannot create " + SAVE_DIR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    private static void specCheck(String path,String folder){
        if(!folder.equals("none"))
            try{
                printFileFolderList(folder);
            }catch(NullPointerException e){
                System.out.println("Папка не найдена: "+folder);
            }

        Document doc;
        try {
            doc = Jsoup.connect(path).get();
            Elements urls = doc.select("a");
            System.out.println();
            for(Element url : urls){
                System.out.print(url.text()+" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printFileFolderList(String s){
        File folder = new File(s);

        final String[] mask = { ".exe", ".pak" };//exe - для примера если хочется других расширений проверить
        String[] files = folder.list(new FilenameFilter() {

            @Override public boolean accept(File folder, String name) {
                for(String s : mask)
                    if(name.toLowerCase().endsWith(s)) return true;
                return false;
            }
        });

        for(String fileName : files)
            System.out.print(fileName+" ");
        //return files;
    }
}
