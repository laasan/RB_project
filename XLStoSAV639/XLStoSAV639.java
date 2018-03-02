import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;


public class XLStoSAV639 {
    public static void main(String[] args) throws IOException {
        JFileChooser fileopen = new JFileChooser(new File("").getAbsolutePath());//в папке приложения

        int choose = fileopen.showDialog(null,"Выбрать XLS файл");
        if(choose == JFileChooser.APPROVE_OPTION){
            File file = fileopen.getSelectedFile();
            readFromExcel(file);
        }

        JOptionPane.showMessageDialog(null, "Работа программы завершена");
    }

    public static void readFromExcel(File file) throws IOException {
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = myExcelBook.getSheetAt(0);
        String filePath = file.getPath();
        String[] cell = new String[8];
        Iterator<Row> it = sheet.iterator();
        int count = 0;
        //пропуск первых двух строк
        it.next();
        it.next();

        OutputStreamWriter fileWriter = null;
        try {
            fileWriter = new OutputStreamWriter(new FileOutputStream("F639_razd1_"+getDayToday()+".sav"), "cp866"); //файл создаётся новый или затирается существующий

            try {
                //Записываем текст у файл
                System.out.println("<F639_1>");
                fileWriter.append("<F639_1>\n\r");//без \r даёт в клико несовпадение форматов
                //проходим по всему листу
                while (it.hasNext()) {
                    Row row = it.next();

                    for(int i = 0; i<8; i++){
                        try{
                            if(row.getCell(i).getCellType() == HSSFCell.CELL_TYPE_STRING){
                                cell[i] = row.getCell(i).getStringCellValue();
                                if (cell[i].contains("\"")) cell[i] = cell[i].replace("\"","~^");

                            }

                            else if(row.getCell(i).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
                                cell[i] = String.valueOf(row.getCell(i).getNumericCellValue());
                                if (cell[i].contains(".0")) cell[i] = cell[i].replace(".0","");
                                if (cell[i].equals("12.1")&& count>23) cell[i] = "12.10";//отлов пропуска 12.1 вместо 12.10
                            }

                            else cell[i] = "";
                        }
                        catch (NullPointerException e){
                            cell[i]="";
                        }

                        System.out.print("\""+cell[i]+"\""+",");
                        fileWriter.append("\""+cell[i]+"\""+",");
                    }
                    if (cell[0].equals("")){
                        System.out.print("\"1\","+"\"0\","+"\""+count+"\"");
                        fileWriter.append("\"1\","+"\"0\","+"\""+count+"\"");
                    }
                    else {
                        count++;
                        System.out.print("\"1\","+"\""+count+"\""+",\"\"");
                        fileWriter.append("\"1\","+"\""+count+"\""+",\"\"");
                    }
                    System.out.println();
                    fileWriter.append("\n\r");
                }
            } finally {
                fileWriter.flush();
                fileWriter.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        myExcelBook.close();
    }

    public static String getDayToday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        return format1.format(cal.getTime());
    }

}
