import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FolderSort440P {
    //public static String log = "G:\\OTCH_CB\\440-П\\testFold\\sorted\\logSort440P.log";
    //public static String log = "G:\\OTCH_CB\\440-П\\testFold\\testJey\\logSort440P.log";
    private static String log = "D:/JavaProj/FolderSort440P/testFold/sorted/logSort440P.log";
    private static String shortLog = "";
    private static String dateSort = "";//getDayTimeNow().substring(0,8);

    private static List<String> fldrList0 = null;
    private static List<String> fldrList1 = null;
    private static List<String> fldrList2 = null;

    private static int count = 0;
    private static int err = 0;
    private static int unsort = 0;
    private static int init = 0;//инициирующие запросы

    public static void main(String[] args){
        dateSort = JOptionPane.showInputDialog(
                new JOptionPane(),
                "Какой датой сортировать?",
                getDayTimeNow().substring(0,8));

        if(dateSort!=null&&dateSort.length()==8){
            //String dirIn = "I:\\Local.mail\\-ABO-\\Lushkin\\testFold";
            //String dirIn = "G:\\OTCH_CB\\440-П\\testFold";
            //String dirIn = "G:\\OTCH_CB\\440-П\\testFold\\testJey\\inSort";
            String dirIn = "D:/JavaProj/FolderSort440P/testFold";
            String ext = ".xml";

            //String dirOut = "I:\\Local.mail\\-ABO-\\Lushkin\\testFold\\sorted";
            //String dirOut = "G:\\OTCH_CB\\440-П\\testFold\\sorted";
            String[] dirOut = new String[3];
            //dirOut[0]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\jey";
            //dirOut[0]= "G:\\OTCH_CB\\440-П\\testFold\\testJey\\jey";
            dirOut[0]= "D:/JavaProj/FolderSort440P/testFold/sorted/jey";
            //dirOut[1]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\kramar";
            //dirOut[1]= "G:\\OTCH_CB\\440-П\\testFold\\testJey\\kramar";
            dirOut[1]= "D:/JavaProj/FolderSort440P/testFold/sorted/kramar";
            //dirOut[2]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\rum";
            //dirOut[2]= "G:\\OTCH_CB\\440-П\\testFold\\testJey\\rum";
            dirOut[2]= "D:/JavaProj/FolderSort440P/testFold/sorted/rum";

            if(args.length==5){
                log = args[0];
                dirIn = args[1];
                dirOut[0] = args[2];
                dirOut[1] = args[3];
                dirOut[2] = args[4];
            }

            //чтобы лишний раз не запускать getFolderNamesList()
            fldrList0 = getFolderNamesList(dirOut[0]);
            fldrList1 = getFolderNamesList(dirOut[1]);
            fldrList2 = getFolderNamesList(dirOut[2]);

            folderSort440P(dirIn,dirOut,ext);
            new TxtWin(shortLog);
        }
        else
            System.out.println("Отмена ввода даты сортировки или дата сортировки неправильного формата");

    }

    //сама логика сортировки по названию
    public static void folderSort440P(String dir, String[] dirOut, String ext){
        File file = new File(dir);
        logOrInfWriter(log,true,"Запуск " + getDayTimeNow()+". Датой сортировки: " + dateSort);
        shortLog = shortLog + "Запуск " + getDayTimeNow().substring(0,14)+". Датой сортировки: " + dateSort + "\r\n";
        if(!file.exists()) logOrInfWriter(log,true,dir + " папка не существует");
        File[] listFiles = file.listFiles(new MyFileNameFilter(ext));

        if(listFiles.length == 0){
            logOrInfWriter(log, true,dir + " не содержит файлов с расширением" + ext);
            shortLog = shortLog + dir + " не содержит файлов с расширением" + ext;
        } else {
            //первый цикл по стартовым, чтобы сначала создались новые init-папки и туда пошли вторичные из текущего запуска вторым циклом
            for(File f : listFiles){
                count++;
                String[] fName =  f.getName().substring(0,f.getName().lastIndexOf(".")).split("_");

                switch (f.getName().split("_")[0].substring(0,3)){
                    case "PNO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "RPO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[0],fldrList0);
                        break;
                    }
                    case "ROO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[0],fldrList0);
                        break;
                    }
                    case "PPD": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "PKO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "APN": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "APO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "APZ": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "ZSO": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[1],fldrList1);
                        break;
                    }
                    case "ZSN": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[1],fldrList1);
                        break;
                    }
                    case "ZSV": {
                        startFolder(fName[0]+"_"+fName[1]+"_"+fName[2], f, dirOut[1],fldrList1);
                        break;
                    }
                }
            }

            //обход вторичных и подсчёт несортированных
            for(File f : listFiles){
                //count++;
                String[] fName =  f.getName().substring(0,f.getName().lastIndexOf(".")).split("_");

                String nik = f.getName().split("_")[0].substring(0,3);
                switch (nik){
                    case "BVS": {
                        notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[1],fldrList1);
                        break;
                    }
                    case "BNP": {
                        notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[2],fldrList2);
                        break;
                    }
                    case "BOS": {
                        notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[0],fldrList0);
                        break;
                    }
                    case "BZ1": {
                        notStartFolder(fName[2]+"_"+fName[3]+"_"+fName[4], f, dirOut[0],fldrList0);
                        break;
                    }
                    case "BVD": {
                        notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[1],fldrList1);
                        break;
                    }
                    case "KWT": {
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            notStartFolder(fName[2]+"_"+fName[3]+"_"+fName[4], f, dirOut[0],fldrList0);
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            notStartFolder(fName[2]+"_"+fName[3]+"_"+fName[4], f, dirOut[1],fldrList1);
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            notStartFolder(fName[2]+"_"+fName[3]+"_"+fName[4], f, dirOut[2],fldrList2);
                        else{
                            logOrInfWriter(log, true,"Ошибка: "+f.getName()+ " не ясен ответственный.");
                            err++;
                        }
                        break;
                    }
                    case "PB1":
                    case "PB2": {
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[0],fldrList0);
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[1],fldrList1);
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[2],fldrList2);
                        else{
                            logOrInfWriter(log, true,f.getName()+ " не ясен ответственный.");
                            err++;
                        }
                        break;
                    }
                 /*   case "PB2": {
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[0]);
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[1]);
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            notStartFolder(fName[1]+"_"+fName[2]+"_"+fName[3], f, dirOut[2]);
                        else{
                            logOrInfWriter(log, true,f.getName()+ " не ясен ответственный.");
                            err++;
                        }break;
                    }*/

                    default : {
                        //если не init-запросы то в unsort идёт
                        if(!nik.equals("PNO")&&!nik.equals("RPO")&&!nik.equals("ROO")&&!nik.equals("PPD")
                                &&!nik.equals("PKO")&&!nik.equals("APN")&&!nik.equals("APO")&&!nik.equals("APZ")
                                &&!nik.equals("ZSO")&&!nik.equals("ZSN")&&!nik.equals("ZSV")){
                            logOrInfWriter(log,true,"UNSORTED: " + f.getName());
                            unsort++;
                        }
                        //else System.out.println("Теоретически обработаны первым циклом: "+f.getName());
                    }
                }
            }
        }
        logOrInfWriter(log,true,"Готово "+ getDayTimeNow() +". Обработано файлов: " + count + ". Из них стартовых: " + init + "; ошибочных: " + err + "; не отсортированных: " + unsort + ".");
        shortLog = shortLog + "Готово "+ getDayTimeNow().substring(0,14) +". Обработано файлов: " + count + ". Из них стартовых: " + init + "; ошибочных: " + err + "; не отсортированных: " + unsort + "."+"\r\n";
    }

    public static void startFolder(String name,File f, String out, List<String> fList){
        logOrInfWriter(log,true,f.getName()+ " " + " подпапка:"+dateSort+"_"+name+"/");
        String to = out+"/"+dateSort+"_"+name+"/";
        //List<String> fList = getFolderNamesList(out);
        try {
            copyFile(f,to);
            init++;
            //System.out.println("fList.size(): " + fList.size() + " fldrList0.size(): " + fldrList0.size());
            fList.add(dateSort+"_"+name); //пополняем список свежесозданной папкой
            //System.out.println("fList.size(): " + fList.size()+" fldrList0.size(): " + fldrList0.size());
            logOrInfWriter(to+"inf",false,dateSort);//inf файл с датой последнего изменения содержимого папки
        } catch (IOException e) {
            logOrInfWriter(log,true,"Ошибка с " + f);
            err++;
        }
    }

    public static void notStartFolder(String name, File f, String out, List<String> fList){
        //List<String> fList = getFolderNamesList(out);
        String fldr = getFolderAlike(name,fList);
        //System.out.println("notStartFolder: "+fldr);
        if(fldr.equals("")) {
            //logOrInfWriter(log,true,"Под файл "+f.getName()+" нет стартовой папки. Обрабатывайте вручную.");
            //unsort++;
            fldr = "unknown_" + name;
            logOrInfWriter(log,true,f.getName()+" подпапка:"+fldr+"/");

            String to = out+"/"+fldr+"/";
            try {
                copyFile(f,to);
                logOrInfWriter(to+"inf",false,dateSort);//inf файл с датой последнего изменения содержимого папки
            } catch (IOException e) {
                logOrInfWriter(log,true,"Ошибка с " + f);
                err++;
            }
        }
        else{
            logOrInfWriter(log,true,f.getName()+" подпапка:"+fldr+"/");

            String to = out+"/"+fldr+"/";
            try {
                copyFile(f,to);
                logOrInfWriter(to+"inf",false,dateSort);//inf файл с датой последнего изменения содержимого папки
            } catch (IOException e) {
                logOrInfWriter(log,true,"Ошибка с " + f);
                err++;
            }
        }
    }

    public static List<String> getFolderNamesList(String path){
        File file = new File(path);
        File[] folders = file.listFiles();
        //System.out.println("getFolderNamesList "+folders.length+" path: "+ path);
        //String[] folderNames = new String[folders.length];
        List<String> folderNamesList = new ArrayList<>();

        for (int i = 0; i < folders.length; i++) {
            //System.out.println("folders[i].isDirectory(): "+folders[i].isDirectory()+" folders[i].getPath():"+folders[i].getPath()+" path+folders[i].getName():"+path+"/"+folders[i].getName());
            if(folders[i].isDirectory()&&folders[i].getPath().replace("\\","/").equals(path.replace("\\","/")+"/"+folders[i].getName())){//что папка и чтобы в подпапки не лез
                //if(checkBox.isSelected())
                //    folderNamesList.add(folders[i].getName());//все берём
                //else{//если чекбокс не помечен
                    if(!folders[i].getName().substring(0,1).equals("_")&&!folders[i].getName().substring(0,8).equals("unknown_"))//если не начинается с "_" или unknown
                        folderNamesList.add(folders[i].getName());
                //}
                //System.out.println(folders[i].getName()+" "+folders[i].getPath());
            }
        }
        return folderNamesList;
    }

    public static String getFolderAlike(String startFileName, List<String> fList){
        String tempName = "";
        //List<String> fList = getFolderNamesList(out);
        //System.out.println("getFolderAlike: "+fList.size()/*+" "+out*/);
        for (String fldr: fList
             ) {
            //System.out.println(fldr);
            //папка содержит название файла и не является архивной(первый символ не "_")
            //выбирается последний в списке(если их несколько)
            if(fldr.contains(startFileName)&&!fldr.substring(0,1).equals("_")) {
                tempName = fldr;
                //System.out.println(fldr);
            }

        }
        //System.out.println(startFileName);
        return tempName;
    }

    //реализация интерфейса FilenameFilter (с переопределением метода суперкласса для отбора xml и XML)
    public static class MyFileNameFilter implements FilenameFilter{
        private String extention;

        public MyFileNameFilter(String extention){
            this.extention = extention.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name){
            return name.toLowerCase().endsWith(extention);
        }
    }

    //метод по копированию файлов
    private static void copyFile(File src, String dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        if(dest.equals(null))throw new IOException();//когда не найден хозяин kwt, например
        File toPath = new File(dest);
        if(!toPath.exists()) toPath.mkdirs();

        File to = new File(dest + src.getName());
        if(to.exists()){ //обработка ситуации дублирования
            String tempTime = getDayTimeNow();
            logOrInfWriter(log,true,"Дублирование: "+ dest + src.getName() +" Файл заменён, старая копия в папке: " + "copyOld_"+src.getName().substring(0,3)+"_"+tempTime);
            //System.out.println(dest+"copyOld_"+src.getName().substring(0,3)+"_"+tempTime+"/");
            //System.out.println(dest+src.getName());
            File oldFile = new File(dest+src.getName());
            copyFile(oldFile, dest+"copyOld_"+src.getName().substring(0,3)+"_"+tempTime+"/");//копирование старого файла в новую папку oldCopy-типа
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

    //дописывание в файл лога
    private static void logOrInfWriter(String path, Boolean append, String text){
        File file = new File(path);

        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Проблемы с лог файлом");
            e.printStackTrace();
        }

        FileWriter fr = null;
        try {
            fr = new FileWriter(file,append);
            fr.write(text+"\r\n");
            //shortLog = shortLog + text+"\r\n";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //текущая дата и время в формате yyyyMMdd_HH_mm_ss_S
    public static String getDayTimeNow(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd_HH_mm_ss_S");
        return format1.format(cal.getTime());
    }


}
