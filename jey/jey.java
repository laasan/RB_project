import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class jey {
    public static void main(String[] args) throws IOException {
        int nextOrExit = JOptionPane.YES_OPTION;
        while(nextOrExit==JOptionPane.YES_OPTION) {
            ArrayList<String> acc = new ArrayList<>();
            ArrayList<String> newFileStr = new ArrayList<>();

            JFileChooser fileopen = new JFileChooser("C:/jey/");
            int ret = fileopen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                String fileName = file.getName();
                String filePath = file.getPath();
                //String filePath = "C:/JavaProj/jey/PB2_RPO14525294_470320170424_002184.txt";
                //String fileName = "PB2_RPO14525294_470320170424_002184.txt";
                String text = getText();//"Банк не предоставит вам чо надо ввиду ФЗ-ХЗ.";
                String accString = "";

                if (text!=null){
                    Boolean pb2 = false;
                    if(fileName.substring(0,3).equals("PB2")||fileName.substring(0,3).equals("pb2")){
                        System.out.println(fileName);
                        pb2 = true;
                    }

                    //считываем в дос-формате "cp866"
                    List<String> lines = Files.readAllLines(Paths.get(filePath), Charset.forName("cp866"));

                    for (String str: lines
                            ) {

                        System.out.println(str);
                        if(!pb2 && str.length()>6){
                            if(str.substring(0,6).equals("НомСч:")){
                                System.out.println(str.substring(6,str.length()));
                                acc.add(str.substring(6,str.length()));
                            }
                        }

                        if(pb2 && str.substring(0,3).equals("35;")){
                            String[] strings = str.split(" Счет ");
                            if(strings.length == 2)
                                accString = " Счет " + strings[1].substring(0,strings[1].length()-3);
                        }

                    }

                    //добавляем счета если есть для не PB2
                    if(!pb2){
                        for (String accStr:acc
                                ) {
                            accString = accString + accStr + ", ";
                        }
                        //если счета есть дописываем Счет
                        if(accString.length()>2)
                            accString = " Счет " + accString.substring(0,accString.length()-2);
                    }

                    //берем текущее дату и время в нужном формате
                    long curTime = System.currentTimeMillis();
                    String curStringDate = new SimpleDateFormat("yyyy-MM-dd").format(curTime);
                    String curStringTime = new SimpleDateFormat("HH:mm:ss").format(curTime);

                    //делаем строку с сообщением
                    text = "35;" + text + accString + "@@@";
/*
        StringBuilder sb = StrangeEncoder.escapeNonLatin(temp, new StringBuilder());
        temp = sb.toString();
*/


                    //выводим всю эту ботву
                    if(!pb2) newFileStr.add(fileName.substring(0,fileName.length()-4)+"###");
                    else newFileStr.add(fileName.substring(4,fileName.length()-4)+"###");
                    newFileStr.add(text);
                    newFileStr.add(curStringDate+"@@@");
                    newFileStr.add(curStringTime+"@@@");
                    newFileStr.add("===");

                    //формируем имя файла
                    String outFileName;
                    if(!pb2) outFileName = "PB2_"+fileName;
                    else outFileName = fileName;

                    //выводим в dos формате ("cp866")
                    BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName), "cp866"));

                    for (String s: newFileStr
                            ) {
                        System.out.println(s);
                        w.write(s+"\n");
                    }
                    w.flush();
                    w.close();
                }

                nextOrExit = JOptionPane.showOptionDialog(
                        null,
                        "Можно нажать \"Следующий файл\" и продолжить обработку другого файла.",
                        "Выйти или продолжить?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new Object[]{"Следующий файл","Выйти"},
                        "Выйти");
            }
        }

    }

    public static String getText() {
        // Диалоговое окно ввода данных
        String result = JOptionPane.showInputDialog(
                null,
                "Введите текст сообщения");
        return result;
    }
}
