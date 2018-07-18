import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.swing.*;

public class XMLxls {
    public static void main(String[] args) throws ParseException {

        int nextOrExit = JOptionPane.YES_OPTION;
        while(nextOrExit==JOptionPane.YES_OPTION) {
            String[] Args = new String[5];
            Args = paramReader(Args);
            //System.out.print(Args[0]+" "+Args[1]+" "+Args[2]+" "+Args[3]);

            List<SEM21> semList = new ArrayList<>();
            String newName;
            //открытие\выбор XML файла
            String fileName, filePath;
            JFileChooser fileopen = new JFileChooser(Args[0]);//"C:\\");

            String DOC_DATE="",DOC_NO="",SENDER_ID="",DOC_TYPE_ID="";
            String outP;

            int ret = fileopen.showDialog(null, "Выбрать XML файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                fileName = file.getName();
                filePath = file.getPath();


                if(isSEM21(fileName)||isSEM03(fileName)||isEQM06(fileName)){
                    newName = getNewName(fileName);
                    outP = outPath(Args,file);
                /*
                действия// начитка semList
                 */

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(filePath);

                        //инфа в первую строку
                        NodeList reqList = doc.getElementsByTagName("DOC_REQUISITES");
                        Node doc_req = reqList.item(0);
                        NamedNodeMap reqAttr = doc_req.getAttributes();
                        DOC_DATE = reqAttr.item(0).getNodeValue();
                        DOC_NO = reqAttr.item(1).getNodeValue();
                        SENDER_ID = reqAttr.item(4).getNodeValue();
                        DOC_TYPE_ID = reqAttr.item(2).getNodeValue();

                        //гон по RECORDS
                        doc.getElementsByTagName("RECORDS");
                        NodeList recordsList = doc.getElementsByTagName("RECORDS");

                        for(int i=0;i<recordsList.getLength();i++){
                            SEM21 sem = new SEM21();

                            Node record = recordsList.item(i);
                            NamedNodeMap recordsAttr = record.getAttributes();

                            Node security = record.getParentNode();
                            NamedNodeMap secAttr = security.getAttributes();

                            fWriter(secAttr,sem,fileName);
                            fWriter(recordsAttr,sem,fileName);

                            levelUp(security,sem,fileName);
/*
                System.out.print(levelUp(security));
*/
                            semList.add(sem);
                        }

                        //for(SEM21 s: semList)
                        //    System.out.println(s.getsTime()+" "+s.getNN()+" "+s.getAccInt());

                        if(fileName.contains("SEM03"))
                            semList = sortByTime(semList); //если SEM03 сортируем его по времени
                        //System.out.println();
                        //for(SEM21 s: semList)
                        //    System.out.println(s.getsTime()+" "+s.getNN() + " " + timeSec(s)+" "+s.getAccInt());

                    } catch(ParserConfigurationException e){
                        e.printStackTrace();
                    } catch (SAXException e) {
                        JOptionPane.showMessageDialog(null, "Внимание!!! SAXException - cообщите об этом инциденте разработчику.");
                        e.printStackTrace();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Внимание!!! IOException - cообщите об этом инциденте разработчику.");
                        e.printStackTrace();
                    }

                    // создание самого excel файла в памяти
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    // создание листа с названием
                    String nameList = newName;
                    if(fileName.contains("SEM03")) nameList = "Ставки РЕПО";
                    HSSFSheet sheet = workbook.createSheet(nameList);

                    // счетчик для строк
                    int rowNum = 0;

                    //SimpleDateFormat для dateConvert(String s, SimpleDateFormat format)
                    SimpleDateFormat sdf = new SimpleDateFormat(); //избыточно - избавься и перепиши
                    sdf.applyPattern("dd.MM.yyyy");

                    //формат даты
                    DataFormat format = sheet.getWorkbook().createDataFormat();
                    CellStyle dateStyle = sheet.getWorkbook().createCellStyle();
                    dateStyle.setDataFormat(format.getFormat("m/d/yy"));//это стиль эксель клетки poi, а выводится всёравно dd.MM.yyyy

                    //формат времени
                    DataFormat formatTime = sheet.getWorkbook().createDataFormat();
                    CellStyle timeStyle = sheet.getWorkbook().createCellStyle();
                    timeStyle.setDataFormat(formatTime.getFormat("[$-F400]h:mm:ss\\ AM/PM"));

                    // это будет первая строчка в листе Excel файла

                    if(!isEQM06(fileName)){  //кроме EQM06
                        Row row = sheet.createRow(rowNum);
                        row.createCell(0).setCellValue(dateConvert(DOC_DATE,sdf));row.getCell(0).setCellStyle(dateStyle);
                        row.createCell(1).setCellValue(DOC_NO);
                        row.createCell(2).setCellValue(SENDER_ID);
                        row.createCell(3).setCellValue(DOC_TYPE_ID);
                    }
                    else rowNum = -1;//для EQM06 не нужна первая строка, далее делается createRow(++rowNum), первый ++rowNum => rowNum = 0(первая строка) для EQM06 и rowNum = 1(вторая строка) для не EQM06

                    // создаем подписи к столбцам
                    List<String> head;
                    if(fileName.contains("SEM21"))
                        head = Arrays.asList("BoardId","BoardName","TradeDate","ShortName","SecurityId","Type","RegNumber","FaceValue","Volume","Value","CurrencyId","OpenPeriod","Open","Low","High","Close","LowOffer","HighBid","WAPrice","ClosePeriod","TrendClose","TrendWAP","Bid","Offer","Prev","YieldAtWAP","YieldClose","AccInt","MarketPrice","NumTrades","IssueSize","TrendClsPR","TrendWapPR","MatDate","MarketPrice2","AdmittedQuote","ListName","PrevLegalClosePrice","LegalOpenPrice","LegalClosePrice","OpenVal","CloseVal","EngBrdName","EngName","EngType","BoardType","Duration","MPValTrd","MP2ValTrd","AdmittedValue");
                    else if(fileName.contains("SEM03"))
                        head = Arrays.asList("NN","TradeNo","BoardId","BoardName","TradeDate","SettleDate","ShortName","SecurityId","BuySell","OrderNo","sTime","TrdType","Price","Quantity","Value","AccInt","Amount","Commission","CurrencyId","TrdAccId","CPFirmId","CPTrdAccId","Yield","Period","SettleCode","BrokerRef","ExtRef","UserId","Price2","AccInt2","RepoRate","RepoPeriod","FirmId","RepoValue","Discount","UpperDiscount","LowerDiscount","MatchRef","ClientCode","Details","SubDetails","CpFirmINN","FaceValue","RefundRate");
                        //иначе по EQM06:
                    else head = Arrays.asList("NN","TradeNo","BoardId","BoardName","TradeDate","SettleDate","ShortName","SecurityId","BuySell","SettleCode","Price","Quantity","Value","ExchComm","ClrComm","ITSComm","CurrencyId","AccInt","TrdAccId","ClientCode","ClientDetails","CPFirmId","Price2","ReportNo","ReportTime","FirmId","InfType","RepoPeriod","RepoPart","Sum1","Sum2","Amount","Balance","Session","ClearingType","RprtComm");
                    Row row = sheet.createRow(++rowNum);
                    int count = 0;
                    for(String h :head){
                        row.createCell(count).setCellValue(h);
                        count++;
                    }

                    // заполняем лист данными
                    for (SEM21 l : semList) {
                        if(fileName.contains("SEM21"))
                            createSheetHeader(sheet, ++rowNum, l, dateStyle, sdf);
                        else if(fileName.contains("SEM03"))
                            createSheetHeaderSEM03(sheet, ++rowNum, l, dateStyle, timeStyle, sdf);
                        else createSheetHeaderEQM06(sheet, ++rowNum, l, dateStyle, timeStyle, sdf);
                    }

                    for(int i = 0;i<head.size();i++)
                        sheet.autoSizeColumn(i);

                    // записываем созданный в памяти Excel документ в файл
                    try (FileOutputStream out = new FileOutputStream(new File(outP + newName+".xls"))) {//"C:\\JavaProj\\BackOfXMLtoXLS\\"+newName+".xls"
                        workbook.write(out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println("Excel файл успешно создан!");
                }
                else JOptionPane.showMessageDialog(null, "Попытка открыть неверный файл. Программа работает c XML для SEM21, SEM21A, SEM03, EQM06");
                //System.out.println("Обработать неверный выбор файла. ");

            }
            else JOptionPane.showMessageDialog(null, "Файл не выбран.");
            //System.out.println("обработать вариант отказа выбора файла вроде Файл не выбран, программа закрыта.");

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


        JOptionPane.showMessageDialog(null, "Работа с программой завершена");

    }

    public static List<SEM21> sortByTime(List<SEM21> l){ //переделать сортировку!!!
        List<Integer> timeList = new ArrayList<>();
        for(SEM21 s : l) timeList.add(timeSec(s));
        Collections.sort(timeList);

        //for(SEM21 s : l) System.out.println(s.getsTime() + " "+ s.getSecurityId()+ " " + s.getQuantity());  //просмотр сортировки при отладке (до сортировки)

        List<SEM21> newList = new ArrayList<>();
        for(int i=0; i<timeList.size(); i++){
            for(int j = 0; j<l.size();j++){
                if(timeList.get(i).equals(timeSec(l.get(j)))){
                    l.get(j).setNN(Integer.toString(i+1));
                    newList.add(l.get(j));
                    l.remove(j);//удалили из списка после учёта, на случай сделок с одинаковыми временами
                    //break b;    //и вышли из цикла
                }
            }
        }

        //System.out.println();//просмотр сортировки при отладке (после сортировки)
        //for(SEM21 s : newList) System.out.println(s.getsTime() + " " + s.getSecurityId()+ " " + s.getQuantity());  //просмотр сортировки при отладке (после сортировки)
        return newList; //сортированный
    }

    //во избежание дубляжа кода
    public static void fWriter(NamedNodeMap nodeAttr,SEM21 s,String fn){
        for(int k=0;k<nodeAttr.getLength();k++) {
            if(fn.contains("SEM21"))
                infWriterSEM21(s,nodeAttr.item(k).getNodeName(),nodeAttr.item(k).getNodeValue());
            else if(fn.contains("SEM03"))
                infWriterSEM03(s,nodeAttr.item(k).getNodeName(),nodeAttr.item(k).getNodeValue());
            else //для EQM06:
                infWriterEQM06(s,nodeAttr.item(k).getNodeName(),nodeAttr.item(k).getNodeValue());

        }
    }

    //класс для лазания по SEM21
    public static void levelUp(Node child,SEM21 s,String fn){
        Node node;
        NamedNodeMap nodeAttr;
        while (!child.getNodeName().equals("MICEX_DOC")){
            node = child.getParentNode();
            nodeAttr = node.getAttributes();
            fWriter(nodeAttr,s,fn);
            child = node;
        }
   }

    public static Integer timeSec(SEM21 s){
        String[] parts = s.getsTime().split(":");
        //System.out.println(parts[0]+" "+parts[1]+" "+parts[2]);
        return Integer.parseInt(parts[0])*60*60+Integer.parseInt(parts[1])*60+Integer.parseInt(parts[2]);
    }

    //класс проверки имени файла SEM21
    public static boolean isSEM21(String fileN){
        return (fileN.contains("SEM21") || fileN.contains("SEM03")) && fileN.toLowerCase().contains(".xml");
    }

    //класс проверки имени файла SEM03
    public static boolean isSEM03(String fileN){
        return fileN.contains("SEM03") && fileN.toLowerCase().contains(".xml");
    }

    //класс проверки имени файла EQM06
    public static boolean isEQM06(String fileN){
        return fileN.contains("EQM06") && fileN.toLowerCase().contains(".xml");
    }

    //класс создания нового имени файла(эксельки)
    public static String getNewName(String fileN){
        if(fileN.contains("SEM21A")){
            return fileN.substring(0,fileN.length()-4);
        }
        else if(fileN.contains("SEM03")){
            String[] nameParts = fileN.split("_");
            return nameParts[3];
        }
        else if(fileN.contains("EQM06")){
            return fileN.substring(0,fileN.length()-4);
        }
        else {
            String[] nameParts = fileN.split("_");
            return nameParts[0]+"_"+nameParts[1]+"_"+nameParts[2]+"_"+nameParts[3]+"_";
        }
    }

    /*переписывание даты из 2016-10-17 в 17.10.2016
    * и возврат данных в формате даты(как переменную Date, иначе надо было прощёлкивать вводом все ячейки, чтобы
    * строковые данные в них превращались экселем в данные типа ячейки, т.е. Дата)
    * при некорректном формате даты быдет возвращено 01.01.1900 и сообщение "Внимание!!! Произошла ошибка..."
    */
    public static Date dateConvert(String s, SimpleDateFormat format) {   //переписать!!!
        String[] a = s.split("-");
        if(a.length==3)
            s = a[2]+"."+a[1]+"."+a[0];

        try {
            return format.parse(s);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Произошла ошибка в обработке формата одной или нескольких дат. Если файл сформируется, ищите там даты 01.01.1900 и замените их на корректные вручную. И сообщите об этом инциденте разработчику.");
            return new Date(1,1,1900);
        }
    }

    /*класс начитки параметров, строки файла param
    * 0 - стартовый каталог(откуда начинает FileChooser)
    * 1 - путь куда копируются SEM21
    * 2 - путь куда копируются SEM21A
    * 3 - путь куда копируются SEM03
    * 4 - путь куда копируется EQM06
    */
    public static String[] paramReader(String[] s){

        try {
            BufferedReader fReader = new BufferedReader(new InputStreamReader(new FileInputStream("param.txt"),"Windows-1251"));
            for(int i = 0; i<5;i++)
                s[i] = fReader.readLine();
            fReader.close();
        } catch (UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Некорректная кодировка файла param.txt содержащего настройки. Запустите программу из корректного места или сообщите об этом разработчику.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Не найден param.txt содержащий настройки. Запустите программу из корректного места или сообщите об этом разработчику.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Ошибка считывания настроек из param.txt. Запустите программу из корректного места или сообщите об этом разработчику.");
            e.printStackTrace();
        }
        return s;
    }

    //класс определения пути выгрузки
    public static String outPath(String[] A, File f){
        if(f.getName().contains("SEM21A"))
            return A[2];
        else if(f.getName().contains("SEM21"))
            return A[1];
        else if(f.getName().contains("SEM03"))
            return A[3];
        else if(f.getName().contains("EQM06"))
            return A[4];
        else
            return "";
    }

    //класс по прописи информации в semList
    public static void infWriterSEM21(SEM21 s, String nodeName, String nodeValue){

        if(nodeName.equals("BoardId")) s.setBoardId(nodeValue);
        if(nodeName.equals("BoardName")) s.setBoardName(nodeValue);
        if(nodeName.equals("TradeDate")) s.setTradeDate(nodeValue);
        if(nodeName.equals("SecShortName")) s.setShortName(nodeValue);
        if(nodeName.equals("SecurityId")) s.setSecurityId(nodeValue);
        if(nodeName.equals("SecurityType")) s.setType_(nodeValue);
        if(nodeName.equals("RegNumber")) s.setRegNumber(nodeValue);
        if(nodeName.equals("FaceValue")) s.setFaceValue(nodeValue);
        if(nodeName.equals("Volume")) s.setVolume(nodeValue);
        if(nodeName.equals("Value")) s.setValue_(nodeValue);

        if(nodeName.equals("CurrencyId")) s.setCurrencyId(nodeValue);
        if(nodeName.equals("OpenPeriod")) s.setOpenPeriod(nodeValue);
        if(nodeName.equals("Open")) s.setOpen_(nodeValue);
        if(nodeName.equals("Low")) s.setLow_(nodeValue);
        if(nodeName.equals("High")) s.setHigh_(nodeValue);
        if(nodeName.equals("Close")) s.setClose_(nodeValue);
        if(nodeName.equals("LowOffer")) s.setLowOffer(nodeValue);
        if(nodeName.equals("HighBid")) s.setHighBid(nodeValue);
        if(nodeName.equals("WAPrice")) s.setWAPrice(nodeValue);
        if(nodeName.equals("ClosePeriod")) s.setClosePeriod(nodeValue);

        if(nodeName.equals("TrendClose")) s.setTrendClose(nodeValue);
        if(nodeName.equals("TrendWAP")) s.setTrendWAP(nodeValue);
        if(nodeName.equals("Bid")) s.setBid(nodeValue);
        if(nodeName.equals("Offer")) s.setOffer(nodeValue);
        if(nodeName.equals("Prev")) s.setPrev(nodeValue);
        if(nodeName.equals("YieldAtWAP")) s.setYieldAtWAP(nodeValue);
        if(nodeName.equals("YieldClose")) s.setYieldClose(nodeValue);
        if(nodeName.equals("AccInt")) s.setAccInt(nodeValue);
        if(nodeName.equals("MarketPrice")) s.setMarketPrice(nodeValue);
        if(nodeName.equals("NumTrades")) s.setNumTrades(nodeValue);

        if(nodeName.equals("IssueSize")) s.setIssueSize(nodeValue);
        if(nodeName.equals("TrendClsPR")) s.setTrendClsPR(nodeValue);
        if(nodeName.equals("TrendWapPR")) s.setTrendWapPR(nodeValue);
        if(nodeName.equals("MatDate")) s.setMatDate(nodeValue);
        if(nodeName.equals("MarketPrice2")) s.setMarketPrice2(nodeValue);
        if(nodeName.equals("AdmittedQuote")) s.setAdmittedQuote(nodeValue);
        if(nodeName.equals("ListName")) s.setListName(nodeValue);
        if(nodeName.equals("PrevLegalClosePrice")) s.setPrevLegalClosePrice(nodeValue);
        if(nodeName.equals("LegalOpenPrice")) s.setLegalOpenPrice(nodeValue);
        if(nodeName.equals("LegalClosePrice")) s.setLegalClosePrice(nodeValue);

        if(nodeName.equals("OpenVal")) s.setOpenVal(nodeValue);
        if(nodeName.equals("CloseVal")) s.setCloseVal(nodeValue);
        if(nodeName.equals("EngBoardName")) s.setEngBrdName(nodeValue);
        if(nodeName.equals("EngName")) s.setEngName(nodeValue);
        if(nodeName.equals("EngType")) s.setEngType(nodeValue);
        if(nodeName.equals("BoardType")) s.setBoardType(nodeValue);
        if(nodeName.equals("Duration")) s.setDuration(nodeValue);
        if(nodeName.equals("MPValTrd")) s.setMPValTrd(nodeValue);
        if(nodeName.equals("MP2ValTrd")) s.setMP2ValTrd(nodeValue);
        if(nodeName.equals("AdmittedValue")) s.setAdmittedValue(nodeValue);
    }

    //
    public static void infWriterSEM03(SEM03 s, String nodeName, String nodeValue){

        if(nodeName.equals("RecNo")) s.setNN(nodeValue);
        if(nodeName.equals("TradeNo")) s.setTradeNo(nodeValue);
        if(nodeName.equals("BoardId")) s.setBoardId(nodeValue);

        if(nodeName.equals("BoardName")) s.setBoardName(nodeValue);
        if(nodeName.equals("TradeDate")) s.setTradeDate(nodeValue);
        if(nodeName.equals("SettleDate")) s.setSettleDate(nodeValue);
        if(nodeName.equals("SecShortName")) s.setShortName(nodeValue);
        if(nodeName.equals("SecurityId")) s.setSecurityId(nodeValue);
        if(nodeName.equals("BuySell")) s.setBuySell(nodeValue);
        if(nodeName.equals("OrderNo")) s.setOrderNo(nodeValue);
        if(nodeName.equals("TradeTime")) s.setsTime(nodeValue);

        if(nodeName.equals("TradeType")) s.setTrdType(nodeValue);
        if(nodeName.equals("Price")) s.setPrice(nodeValue);
        if(nodeName.equals("Quantity")) s.setQuantity(nodeValue);
        if(nodeName.equals("Value")) s.setValue(nodeValue);
        if(nodeName.equals("AccInt")) s.setAccInt(nodeValue);
        if(nodeName.equals("Amount")) s.setAmount(nodeValue);
        if(nodeName.equals("ExchComm")) s.setCommission(nodeValue);
        if(nodeName.equals("CurrencyId")) s.setCurrencyId(nodeValue);
        if(nodeName.equals("TrdAccId")) s.setTrdAccId(nodeValue);
        if(nodeName.equals("CPFirmId")) s.setCPFirmId(nodeValue);

        if(nodeName.equals("CPTrdAccId")) s.setCPTrdAccId(nodeValue);
        if(nodeName.equals("Yield")) s.setYield(nodeValue);
        if(nodeName.equals("Period")) s.setPeriod(nodeValue);
        if(nodeName.equals("SettleCode")) s.setSettleCode(nodeValue);
        if(nodeName.equals("BrokerRef")) s.setBrokerRef(nodeValue);
        if(nodeName.equals("ExtRef")) s.setExtRef(nodeValue);
        if(nodeName.equals("UserId")) s.setUserId(nodeValue);
        if(nodeName.equals("Price2")) s.setPrice2(nodeValue);
        if(nodeName.equals("AccInt2")) s.setAccInt2(nodeValue);
        if(nodeName.equals("RepoRate")) s.setRepoRate(nodeValue);

        if(nodeName.equals("RepoPeriod")) s.setRepoPeriod(nodeValue);
        if(nodeName.equals("FirmId")) s.setFirmId(nodeValue);
        if(nodeName.equals("RepoValue")) s.setRepoValue(nodeValue);
        if(nodeName.equals("Discount")) s.setDiscount(nodeValue);
        if(nodeName.equals("UpperDiscount")) s.setUpperDiscount(nodeValue);
        if(nodeName.equals("LowerDiscount")) s.setLowerDiscount(nodeValue);
        if(nodeName.equals("MatchRef")) s.setMatchRef(nodeValue);
        if(nodeName.equals("ClientCode")) s.setClientCode(nodeValue);
        if(nodeName.equals("Details")) s.setDetails(nodeValue);
        if(nodeName.equals("SubDetails")) s.setSubDetails(nodeValue);

        if(nodeName.equals("CPfirmINN")) s.setCpFirmINN(nodeValue);
        if(nodeName.equals("FaceValue")) s.setFaceValue(nodeValue);
        if(nodeName.equals("RefundRate")) s.setRefundRate(nodeValue);

    }

    public static void infWriterEQM06(EQM06 s, String nodeName, String nodeValue){

        if(nodeName.equals("RecNo")) s.setNN(nodeValue);
        if(nodeName.equals("TradeNo")) s.setTradeNo(nodeValue);
        if(nodeName.equals("BoardId")) s.setBoardId(nodeValue);
        if(nodeName.equals("BoardName")) s.setBoardName(nodeValue);
        if(nodeName.equals("TradeDate")) s.setTradeDate(nodeValue);
        if(nodeName.equals("SettleDate")) s.setSettleDate(nodeValue);
        if(nodeName.equals("SecShortName")) s.setShortName(nodeValue);
        if(nodeName.equals("SecurityId")) s.setSecurityId(nodeValue);
        if(nodeName.equals("BuySell")) s.setBuySell(nodeValue);

        if(nodeName.equals("Price")) s.setPrice(nodeValue);
        if(nodeName.equals("Quantity")) s.setQuantity(nodeValue);
        if(nodeName.equals("Value")) s.setValue(nodeValue);
        if(nodeName.equals("ExchComm")) s.setExchComm(nodeValue);
        if(nodeName.equals("ClrComm")) s.setClrComm(nodeValue);
        if(nodeName.equals("ITSComm")) s.setITSComm(nodeValue);
        if(nodeName.equals("CurrencyId")) s.setCurrencyId(nodeValue);
        if(nodeName.equals("AccInt")) s.setAccInt(nodeValue);
        if(nodeName.equals("TrdAccId")) s.setTrdAccId(nodeValue);
        if(nodeName.equals("ClientCode")) s.setClientCode(nodeValue);

        if(nodeName.equals("ClientDetails")) s.setClientDetails(nodeValue);
        if(nodeName.equals("CPFirmId")) s.setCPFirmId(nodeValue);
        if(nodeName.equals("Price2")) s.setPrice2(nodeValue);
        if(nodeName.equals("ReportNo")) s.setReportNo(nodeValue);
        if(nodeName.equals("ReportTime")) s.setReportTime(nodeValue);
        if(nodeName.equals("FirmId")) s.setFirmId(nodeValue);
        if(nodeName.equals("InfType")) s.setInfType(nodeValue);
        if(nodeName.equals("RepoPeriod")) s.setRepoPeriod(nodeValue);
        if(nodeName.equals("RepoPart")) s.setRepoPart(nodeValue);
        if(nodeName.equals("Sum1")) s.setSum1(nodeValue);

        if(nodeName.equals("Sum2")) s.setSum2(nodeValue);
        if(nodeName.equals("Amount")) s.setAmount(nodeValue);
        if(nodeName.equals("Balance")) s.setBalance(nodeValue);
        if(nodeName.equals("Session")) s.setSession(nodeValue);
        if(nodeName.equals("ClearingType")) s.setClearingType(nodeValue);
        if(nodeName.equals("RprtComm")) s.setRprtComm(nodeValue);

    }

    // заполнение строки (rowNum) определенного листа (sheet)
    // данными  из созданного в памяти Excel файла
    private static void createSheetHeader(HSSFSheet sheet, int rowNum, SEM21 s, CellStyle dateSt, SimpleDateFormat f) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(s.getBoardId());
        row.createCell(1).setCellValue(s.getBoardName());
        row.createCell(2).setCellValue(dateConvert(s.getTradeDate(),f));row.getCell(2).setCellStyle(dateSt);
        row.createCell(3).setCellValue(s.getShortName());
        row.createCell(4).setCellValue(s.getSecurityId());
        row.createCell(5).setCellValue(s.getType_());
        row.createCell(6).setCellValue(s.getRegNumber());
        row.createCell(7).setCellValue(Double.parseDouble(s.getFaceValue()));
        row.createCell(8).setCellValue(Double.parseDouble(s.getVolume()));
        row.createCell(9).setCellValue(Double.parseDouble(s.getValue_()));

        row.createCell(10).setCellValue(s.getCurrencyId());
        row.createCell(11).setCellValue(Double.parseDouble(s.getOpenPeriod()));
        row.createCell(12).setCellValue(Double.parseDouble(s.getOpen_()));
        row.createCell(13).setCellValue(Double.parseDouble(s.getLow_()));
        row.createCell(14).setCellValue(Double.parseDouble(s.getHigh_()));
        row.createCell(15).setCellValue(Double.parseDouble(s.getClose_()));
        row.createCell(16).setCellValue(Double.parseDouble(s.getLowOffer()));
        row.createCell(17).setCellValue(Double.parseDouble(s.getHighBid()));
        row.createCell(18).setCellValue(Double.parseDouble(s.getWAPrice()));
        row.createCell(19).setCellValue(Double.parseDouble(s.getClosePeriod()));

        row.createCell(20).setCellValue(Double.parseDouble(s.getTrendClose()));
        row.createCell(21).setCellValue(Double.parseDouble(s.getTrendWAP()));
        row.createCell(22).setCellValue(Double.parseDouble(s.getBid()));
        row.createCell(23).setCellValue(Double.parseDouble(s.getOffer()));
        row.createCell(24).setCellValue(Double.parseDouble(s.getPrev()));
        row.createCell(25).setCellValue(Double.parseDouble(s.getYieldAtWAP()));
        row.createCell(26).setCellValue(Double.parseDouble(s.getYieldClose()));
        row.createCell(27).setCellValue(Double.parseDouble(s.getAccInt()));
        row.createCell(28).setCellValue(Double.parseDouble(s.getMarketPrice()));
        row.createCell(29).setCellValue(Double.parseDouble(s.getNumTrades()));

        row.createCell(30).setCellValue(Double.parseDouble(s.getIssueSize()));
        row.createCell(31).setCellValue(Double.parseDouble(s.getTrendClsPR()));
        row.createCell(32).setCellValue(Double.parseDouble(s.getTrendWapPR()));
        if(!s.getMatDate().equals("")){row.createCell(33).setCellValue(dateConvert(s.getMatDate(),f));row.getCell(33).setCellStyle(dateSt);}
        else row.createCell(33).setCellValue("");
        row.createCell(34).setCellValue(Double.parseDouble(s.getMarketPrice2()));
        row.createCell(35).setCellValue(Double.parseDouble(s.getAdmittedQuote()));
        row.createCell(36).setCellValue(Double.parseDouble(s.getListName()));
        row.createCell(37).setCellValue(Double.parseDouble(s.getPrevLegalClosePrice()));
        row.createCell(38).setCellValue(Double.parseDouble(s.getLegalOpenPrice()));
        row.createCell(39).setCellValue(Double.parseDouble(s.getLegalClosePrice()));

        row.createCell(40).setCellValue(Double.parseDouble(s.getOpenVal()));
        row.createCell(41).setCellValue(Double.parseDouble(s.getCloseVal()));
        row.createCell(42).setCellValue(s.getEngBrdName());
        row.createCell(43).setCellValue(s.getEngName());
        row.createCell(44).setCellValue(s.getEngType());
        row.createCell(45).setCellValue(s.getBoardType());
        row.createCell(46).setCellValue(Double.parseDouble(s.getDuration()));
        row.createCell(47).setCellValue(Double.parseDouble(s.getMPValTrd()));
        row.createCell(48).setCellValue(Double.parseDouble(s.getMP2ValTrd()));
        row.createCell(49).setCellValue(Double.parseDouble(s.getAdmittedValue()));

    }

    private static void createSheetHeaderSEM03(HSSFSheet sheet, int rowNum, SEM21 s, CellStyle dateSt, CellStyle dataStyleTime, SimpleDateFormat f) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(Double.parseDouble(s.getNN()));
        row.createCell(1).setCellValue(Double.parseDouble(s.getTradeNo()));
        row.createCell(2).setCellValue(s.getBoardId());
        row.createCell(3).setCellValue(s.getBoardName());
        row.createCell(4).setCellValue(dateConvert(s.getTradeDate(),f));row.getCell(4).setCellStyle(dateSt);
        row.createCell(5).setCellValue(dateConvert(s.getSettleDate(),f));row.getCell(5).setCellStyle(dateSt);
        row.createCell(6).setCellValue(s.getShortName());
        row.createCell(7).setCellValue(s.getSecurityId());
        row.createCell(8).setCellValue(s.getBuySell());

        row.createCell(9).setCellValue(Double.parseDouble(s.getOrderNo()));
        if(!s.getsTime().equals("")){row.createCell(10).setCellValue(timeMaker(s.getsTime()));row.getCell(10).setCellStyle(dataStyleTime);}
        else row.createCell(10).setCellValue("");
        row.createCell(11).setCellValue(s.getTrdType());
        row.createCell(12).setCellValue(Double.parseDouble(s.getPrice()));
        row.createCell(13).setCellValue(Double.parseDouble(s.getQuantity()));
        row.createCell(14).setCellValue(Double.parseDouble(s.getValue()));
        row.createCell(15).setCellValue(Double.parseDouble(s.getAccInt()));
        row.createCell(16).setCellValue(Double.parseDouble(s.getAmount()));
        row.createCell(17).setCellValue(Double.parseDouble(s.getCommission()));
        row.createCell(18).setCellValue(s.getCurrencyId());

        row.createCell(19).setCellValue(s.getTrdAccId());
        row.createCell(20).setCellValue(s.getCPFirmId());
        row.createCell(21).setCellValue(s.getCPTrdAccId());
        if(!s.getYield().equals(""))row.createCell(22).setCellValue(Double.parseDouble(s.getYield()));
        else row.createCell(22).setCellValue("");
        row.createCell(23).setCellValue(s.getPeriod());
        row.createCell(24).setCellValue(s.getSettleCode());
        row.createCell(25).setCellValue(s.getBrokerRef());
        row.createCell(26).setCellValue(s.getExtRef());
        row.createCell(27).setCellValue(s.getUserId());
        row.createCell(28).setCellValue(Double.parseDouble(s.getPrice2()));

        row.createCell(29).setCellValue(Double.parseDouble(s.getAccInt2()));
        row.createCell(30).setCellValue(Double.parseDouble(s.getRepoRate()));
        row.createCell(31).setCellValue(Double.parseDouble(s.getRepoPeriod()));
        row.createCell(32).setCellValue(s.getFirmId());
        row.createCell(33).setCellValue(Double.parseDouble(s.getRepoValue()));
        row.createCell(34).setCellValue(Double.parseDouble(s.getDiscount()));
        row.createCell(35).setCellValue(Double.parseDouble(s.getUpperDiscount()));
        row.createCell(36).setCellValue(Double.parseDouble(s.getLowerDiscount()));
        row.createCell(37).setCellValue(s.getMatchRef());
        row.createCell(38).setCellValue(s.getClientCode());

        row.createCell(39).setCellValue(s.getDetails());
        row.createCell(40).setCellValue(s.getSubDetails());
        row.createCell(41).setCellValue(Double.parseDouble(s.getCpFirmINN()));
        row.createCell(42).setCellValue(Double.parseDouble(s.getFaceValue()));
        row.createCell(43).setCellValue(Double.parseDouble(s.getRefundRate()));

    }

    private static void createSheetHeaderEQM06(HSSFSheet sheet, int rowNum, SEM21 s, CellStyle dateSt, CellStyle dataStyleTime, SimpleDateFormat f) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(Double.parseDouble(s.getNN()));
        row.createCell(1).setCellValue(Double.parseDouble(s.getTradeNo()));
        row.createCell(2).setCellValue(s.getBoardId());
        row.createCell(3).setCellValue(s.getBoardName());
        row.createCell(4).setCellValue(dateConvert(s.getTradeDate(),f));row.getCell(4).setCellStyle(dateSt);
        row.createCell(5).setCellValue(dateConvert(s.getSettleDate(),f));row.getCell(5).setCellStyle(dateSt);
        row.createCell(6).setCellValue(s.getShortName());
        row.createCell(7).setCellValue(s.getSecurityId());
        row.createCell(8).setCellValue(s.getBuySell());
        row.createCell(9).setCellValue(s.getSettleCode());

        row.createCell(10).setCellValue(Double.parseDouble(s.getPrice()));
        row.createCell(11).setCellValue(Double.parseDouble(s.getQuantity()));
        row.createCell(12).setCellValue(Double.parseDouble(s.getValue()));
        row.createCell(13).setCellValue(Double.parseDouble(s.getExchComm()));
        row.createCell(14).setCellValue(Double.parseDouble(s.getClrComm()));
        row.createCell(15).setCellValue(Double.parseDouble(s.getITSComm()));
        row.createCell(16).setCellValue(s.getCurrencyId());
        row.createCell(17).setCellValue(Double.parseDouble(s.getAccInt()));
        row.createCell(18).setCellValue(s.getTrdAccId());
        row.createCell(19).setCellValue(s.getClientCode());

        row.createCell(20).setCellValue(s.getClientDetails());
        row.createCell(21).setCellValue(s.getCPFirmId());
        row.createCell(22).setCellValue(Double.parseDouble(s.getPrice2()));
        row.createCell(23).setCellValue(Double.parseDouble(s.getReportNo()));

        if(!s.getReportTime().equals(""))row.createCell(24).setCellValue(timeMaker(s.getReportTime()));
        else row.createCell(24).setCellValue("");
        row.getCell(24).setCellStyle(dataStyleTime);

        row.createCell(25).setCellValue(s.getFirmId());
        row.createCell(26).setCellValue(Double.parseDouble(s.getInfType()));
        row.createCell(27).setCellValue(Double.parseDouble(s.getRepoPeriod()));
        row.createCell(28).setCellValue(Double.parseDouble(s.getRepoPart()));
        row.createCell(29).setCellValue(Double.parseDouble(s.getSum1()));

        row.createCell(30).setCellValue(Double.parseDouble(s.getSum2()));
        row.createCell(31).setCellValue(Double.parseDouble(s.getAmount()));
        row.createCell(32).setCellValue(Double.parseDouble(s.getBalance()));
        if(!s.getReportTime().equals("")) row.createCell(33).setCellValue(Integer.parseInt(s.getSession()));
        else row.createCell(33).setCellValue("");
        row.createCell(34).setCellValue(s.getClearingType());
        row.createCell(35).setCellValue(s.getRprtComm());

    }

    public static float timeMaker(String str) {

        String[] time = str.split(":");

        int h = Integer.parseInt(time[0]);
        int m = Integer.parseInt(time[1]);
        int s = Integer.parseInt(time[2]);
        //System.out.println(str + " " + h + " " + m + " " + s + " return: "+ ((float) (h*60*60 + m*60 + s))/86400);
        return ((float) (h*60*60 + m*60 + s))/86400;

        /*
        DateFormat formatSDF = new SimpleDateFormat("HH:mm:ss");
        Date time1 = null;
        try {
            time1 = formatSDF.parse(str);

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Внимание!!! Произошла ошибка конвертации времени. Обратитесь к разработчику.");
        }

        if (time1 != null) {
            System.out.println(time1.getTime());
        }
        else time1.setTime(0);



        Calendar cal = Calendar.getInstance();
        if (time1 != null) {
            cal.setTime(time1);
            System.out.println(cal.getTime().getTime());
            return cal.getTime().getTime();
        }
        else {
            Date errorTime = cal.getTime(); //момент сейчас, как я понимаю
            JOptionPane.showMessageDialog(null, "Внимание!!! Позорная ошибка! В коде не прописан обход null для какой-то из колонок времени. Обратитесь к разработчику. Закройте программу через Ctrl+Alt+Del.");
            return errorTime.getTime();//время ошибки
            //заменить это всё выбрасыванием Exception
        }


        return cal.getTime().getTime();
        */
    }
}
