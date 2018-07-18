import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FolderSort440P {
    //public static String log = "G:\\OTCH_CB\\440-П\\testFold\\sorted\\logSort440P.log";
    public static String log = "C:/JavaProj/FolderSort440P/testFold/sorted/logSort440P.log";

    public static void main(String[] args){
        //String dirIn = "I:\\Local.mail\\-ABO-\\Lushkin\\testFold";
        //String dirIn = "G:\\OTCH_CB\\440-П\\testFold";
        String dirIn = "C:/JavaProj/FolderSort440P/testFold";

        String ext = ".xml";

        //String dirOut = "I:\\Local.mail\\-ABO-\\Lushkin\\testFold\\sorted";
        //String dirOut = "G:\\OTCH_CB\\440-П\\testFold\\sorted";
        String[] dirOut = new String[3];
        //dirOut[0]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\jey";
        dirOut[0]= "C:/JavaProj/FolderSort440P/testFold/sorted/jey";
        //dirOut[1]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\kramar";
        dirOut[1]= "C:/JavaProj/FolderSort440P/testFold/sorted/kramar";
        //dirOut[2]= "G:\\OTCH_CB\\440-П\\testFold\\sorted\\rum";
        dirOut[2]= "C:/JavaProj/FolderSort440P/testFold/sorted/rum";

        folderSort440P(dirIn,dirOut,ext);
    }

    //сама логика сортировки по названию
    public static void folderSort440P(String dir, String[] dirOut, String ext){
        int count = 0;
        int err = 0;
        int unsort = 0;
        int init = 0;//инициирующие запросы
        File file = new File(dir);
        logWriter(log,"Запуск " + getDayTimeNow());
        if(!file.exists()) logWriter(log,dir + " папка не существует");
        File[] listFiles = file.listFiles(new MyFileNameFilter(ext));
        if(listFiles.length == 0){
            logWriter(log,dir + " не содержит файлов с расширением" + ext);
        } else {
            for(File f : listFiles){
                count++;
                String[] fName =  f.getName().substring(0,f.getName().lastIndexOf(".")).split("_");
                switch (f.getName().split("_")[0].substring(0,3)){
                    /*
                    case "BNS": { //не ясно чей
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = dirOut+"/"+fName[2].substring(4)+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }*/
                    case "BVS": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = dirOut[1]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "BNP": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = dirOut[2]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "BOS": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = dirOut[0]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "BVD": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = dirOut[1]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "KWT": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[2]+"_"+fName[3]+"_"+fName[4]+"/");
                        String to = null;//если останется null(неясен хозяин), то в copyFile() будет проброшен IOException
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            to = dirOut[0]+"/"+fName[2]+"_"+fName[3]+"_"+fName[4]+"/";
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            to = dirOut[1]+"/"+fName[2]+"_"+fName[3]+"_"+fName[4]+"/";
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            to = dirOut[2]+"/"+fName[2]+"_"+fName[3]+"_"+fName[4]+"/";

                        //String to = dirOut+"/"+fName[3].substring(4)+"/"+fName[2]+"_"+fName[3]+"_"+fName[4]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "PB1": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = null;//если останется null(неясен хозяин), то в copyFile() будет проброшен IOException
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            to = dirOut[0]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            to = dirOut[1]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            to = dirOut[2]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";

                        //String to = dirOut+"/"+fName[2].substring(4)+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "PB2": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/");
                        String to = null;//если останется null(неясен хозяин), то в copyFile() будет проброшен IOException
                        if(f.getName().contains("ROO")||f.getName().contains("RPO"))
                            to = dirOut[0]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        else if(f.getName().contains("ZSN")||f.getName().contains("ZSO")||f.getName().contains("ZSV"))
                            to = dirOut[1]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        else if(f.getName().contains("PNO")||f.getName().contains("APZ")||f.getName().contains("PPD")||f.getName().contains("PKO")||f.getName().contains("APO")||f.getName().contains("APN"))
                            to = dirOut[2]+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";

                        //String to = dirOut+"/"+fName[2].substring(4)+"/"+fName[1]+"_"+fName[2]+"_"+fName[3]+"/";
                        try {
                            copyFile(f,to);
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "PNO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "RPO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[0]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "ROO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[0]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "PPD": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "PKO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "APN": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "APO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "APZ": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[2]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "ZSO": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[1]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "ZSN": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[1]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "ZSV": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut[1]+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    /* //TRB и TRG не сортируем
                    case "TRB": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    case "TRG": {
                        logWriter(log,f.getName()+ " " + fName[0].substring(0,3)+" подпапка:"+fName[1].substring(4)+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/");
                        String to = dirOut+"/"+fName[0]+"_"+fName[1]+"_"+fName[2]+"/";
                        try {
                            copyFile(f,to);
                            init++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        break;
                    }
                    */
                    default : {
                        logWriter(log,"UNSORTED: " + f.getName());
                        unsort++;
                        /*
                        String to = dirOut+"/unsorted/"; //вариант с копированием неотсортированных в другую папку
                        try {
                            copyFile(f,to);
                            unsort++;
                        } catch (IOException e) {
                            logWriter(log,"Ошибка с " + f);
                            err++;
                        }
                        */
                    }
                }
            }
            logWriter(log,"Готово "+ getDayTimeNow() +". Обработано файлов: " + count + ". Из них стартовых: " + init + "; ошибочных: " + err + "; не отсортированных: " + unsort + ".");
        }
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
            logWriter(log,"Дублирование: "+ dest + src.getName() +" - будет выдана ошибка. Обработайте вручную.");
            throw new IOException();
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
    private static void logWriter(String path, String text) {
        File file = new File(path);

        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Проблемы с лог файлом");
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
