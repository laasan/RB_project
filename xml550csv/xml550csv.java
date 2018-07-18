import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class xml550csv {
    public static person tempPerson;
    public static List<person> personList;
    public static String fileName;
    public static boolean formatOne = true; //формат выгрузки true - в один файл, false - каждый в свой

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //String fileName = "CB_ES550P_20170627_002.XML";

        personList = new ArrayList<person>();
        /*
        String fileNameList[] =
                {"CB_ES550P_20170627_002.XML"};


        String fileNameList[] =
                {"CB_ES550P_20170626_001.XML","CB_ES550P_20170626_002.XML","CB_ES550P_20170626_003.XML","CB_ES550P_20170626_004.XML","CB_ES550P_20170626_005.XML",
                        "CB_ES550P_20170626_006.XML","CB_ES550P_20170626_007.XML","CB_ES550P_20170626_008.XML","CB_ES550P_20170626_009.XML","CB_ES550P_20170627_001.XML",
                        "CB_ES550P_20170627_002.XML","CB_ES550P_20170628_001.XML","CB_ES550P_20170628_002.XML","CB_ES550P_20170628_003.XML","CB_ES550P_20170629_001.XML",
                        "CB_ES550P_20170629_002.XML","CB_ES550P_20170629_003.XML","CB_ES550P_20170629_004.XML","CB_ES550P_20170629_005.XML","CB_ES550P_20170629_006.XML",
                        "CB_ES550P_20170629_007.XML","CB_ES550P_20170629_008.XML","CB_ES550P_20170629_009.XML","CB_ES550P_20170629_010.XML","CB_ES550P_20170629_011.XML",
                        "CB_ES550P_20170629_012.XML","CB_ES550P_20170629_013.XML","CB_ES550P_20170629_014.XML"};

        */

        File[] fileNameList = getFiles("C:/550P/input",".xml");
        String filePath;

        for(File file : fileNameList){

            fileName = file.getName();
            filePath = file.getPath();
            System.out.println(fileName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            if(!formatOne) personList = new ArrayList<person>(); //отрабатывает если каждый файл xml в отдельный файл
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            //person tempPerson;

            NodeList nodeList = doc.getElementsByTagName("Раздел1.1");
            if(nodeList.getLength()!=0) razdel1(nodeList,"11","ТипКлиента");

            nodeList = doc.getElementsByTagName("Раздел1.2");
            if(nodeList.getLength()!=0) razdel1(nodeList,"11","ТипУчастника");

            nodeList = doc.getElementsByTagName("Раздел2");
            if(nodeList.getLength()!=0)
                for(int i = 0; i < nodeList.getLength(); i++){
                    //tempPerson = new person();                     //для учёта нескольких участников создание new person в ноде про участников
                    //tempPerson.setRazdel("2");
                    String tempNodeId = "";

                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
                        NodeList nodeList1 = nodeList.item(i).getChildNodes();
                        for(int j = 0; j < nodeList1.getLength(); j++) {
                            Node node1 = nodeList1.item(j);
                            if (node1.getNodeType() == Node.ELEMENT_NODE) {
                                if (node1.getNodeName().equals("НомерЗаписи")){
                                    //System.out.println(node1.getTextContent());
                                    //tempPerson.setNoteId(node1.getTextContent());
                                    tempNodeId = node1.getTextContent();
                                }
                                if (node1.getNodeName().equals("Участник")){

                                    tempPerson = new person();        //создание new person здесь - это нюанс для ситуации с несколькими участниками
                                    tempPerson.setRazdel("2");        //вся добываемая выше информация должна заполняться в person здесь
                                    tempPerson.setNoteId(tempNodeId);

                                    NodeList nodeListUch = node1.getChildNodes();
                                    for(int m = 0; m < nodeListUch.getLength(); m++){
                                        Node node = nodeListUch.item(m);
                                        if (node.getNodeName().equals("СтатусУчастника")) tempPerson.setUchStatus(node.getTextContent());
                                        if (node.getNodeName().equals("ТипУчастника")) tempPerson.setClientType(node.getTextContent());

                                        personAdd(node,tempPerson,personList);
                                    }
                                }
                            }
                        }
                    }

                }

            if(!formatOne) CSVgen(personList,fileName,true); //отрабатывает если каждый файл xml в отдельный файл

        }

        if(formatOne) {                                      //отрабатывает если всё собирается в один файл
            CSVgen(personList,"all550_"+getCurrDate()+".XXX",true); //сегодняшнее сборище
            CSVgen(personList,"all550.xls",false);                  //допись к уже существующему all550.xls
        }

    }

    public static void razdel1(NodeList nodeList, String razdel, String ClyentTypeTag){
        for(int i = 0; i < nodeList.getLength(); i++){
            tempPerson = new person();
            tempPerson.setRazdel(razdel);
            //System.out.println(nodeList.item(i).getNodeName());

            NodeList nodeList1 = nodeList.item(i).getChildNodes();
            for(int j = 0; j < nodeList1.getLength(); j++){
                Node node1 = nodeList1.item(j);
                if(node1.getNodeType()==Node.ELEMENT_NODE){
                    if(node1.getNodeName().equals("НомерЗаписи"))tempPerson.setNoteId(node1.getTextContent());
                    if(node1.getNodeName().equals(ClyentTypeTag))tempPerson.setClientType(node1.getTextContent());

                    personAdd(node1,tempPerson,personList);

                    //nodeList1.item(j).getTextContent()
                    //System.out.println("  "+nodeList1.item(j).getNodeName()+" "+nodeList1.item(j).getTextContent());
                }
            }
        }
    }

    public static void personAdd(Node node, person tempPerson, List<person> personList){
        if(node.getNodeName().equals("СведФЛИП")) addFLIP(node,tempPerson, personList);
        else if(node.getNodeName().equals("СведЮЛ")) addUL(node, tempPerson, personList);
        else if(node.getNodeName().equals("СведИНБОЮЛ")) addINBOUL(node, tempPerson, personList);
    }

    public static void addFLIP(Node node1, person tempPerson, List<person> personList){
        NodeList nodeList2 = node1.getChildNodes();
        String tempFio = "";
        String tempDocNum = "";
        for(int k = 0; k < nodeList2.getLength();k++){
            Node node2 = nodeList2.item(k);
            if(node2.getNodeType()==Node.ELEMENT_NODE){
                if(node2.getNodeName().equals("ФИОФЛИП")){
                    NodeList nodeListFIO = node2.getChildNodes();
                    for (int n = 0; n < nodeListFIO.getLength();n++){
                        Node nodeFIO = nodeListFIO.item(n);
                        if (nodeFIO.getNodeType() == Node.ELEMENT_NODE) {
                            if(nodeFIO.getNodeName().equals("Фам"))tempFio = tempFio + nodeFIO.getTextContent();
                            if(nodeFIO.getNodeName().equals("Имя"))tempFio = tempFio + " " + nodeFIO.getTextContent();
                            if(nodeFIO.getNodeName().equals("Отч"))tempFio = tempFio + " " + nodeFIO.getTextContent();
                        }
                    }
                    tempPerson.setFio(tempFio);
                }
                if(node2.getNodeName().equals("ФИОСтрока")&&tempFio.equals(""))tempPerson.setFio(node2.getTextContent());
                if(node2.getNodeName().equals("ИННФЛИП"))tempPerson.setInn(node2.getTextContent());
                if(node2.getNodeName().equals("СведДокУдЛичн")){
                    NodeList nodeList3 = node2.getChildNodes();
                    for(int l = 0; l < nodeList3.getLength(); l++){
                        Node node3 = nodeList3.item(l);
                        if(node3.getNodeName().equals("СерияДок"))tempDocNum = tempDocNum + node3.getTextContent();
                        if(node3.getNodeName().equals("НомДок"))tempDocNum = tempDocNum + node3.getTextContent();
                    }
                    tempPerson.setDocumentNum(tempDocNum);
                }
            }
        }
        tempPerson.setSourceFileName(fileName);
        personList.add(tempPerson);
    }

    public static void addUL(Node node1, person tempPerson, List<person> personList){
        NodeList nodeList2 = node1.getChildNodes();
        for(int k = 0; k < nodeList2.getLength();k++){
            Node node2 = nodeList2.item(k);
            if(node2.getNodeType()==Node.ELEMENT_NODE){
                if(node2.getNodeName().equals("НаимЮЛ"))tempPerson.setPolnNaim(node2.getTextContent());
                if(node2.getNodeName().equals("ИННЮЛ"))tempPerson.setInn(node2.getTextContent());
            }
        }
        tempPerson.setSourceFileName(fileName);
        personList.add(tempPerson);
    }

    public static void addINBOUL(Node node1, person tempPerson, List<person> personList){
        NodeList nodeList2 = node1.getChildNodes();
        for(int k = 0; k < nodeList2.getLength();k++) {
            Node node2 = nodeList2.item(k);
            if (node2.getNodeType() == Node.ELEMENT_NODE) {
                if (node2.getNodeName().equals("ПолноеНаимИНБОЮЛ"))
                    tempPerson.setPolnNaim(node2.getTextContent());
            }
        }
        tempPerson.setSourceFileName(fileName);
        personList.add(tempPerson);
    }


    public static void CSVgen(List<person> personList,String fileName,boolean newOut){
        //newOut = true - создание нового файла на выходе, false - допись в all550.xls
        String HEADER = "NoteId;ClientType;Fio;Inn;PolnNaim;Razdel;UchStatus;DocumentNum;SourceFileName";
        String DELIMITER = ";";
        String NEW_LINE_SEPARATOR="\r\n";
        try {
            OutputStreamWriter fileWriter = null;
            if(newOut){
                fileWriter = new OutputStreamWriter(new FileOutputStream("C:/550P/output/"+fileName.substring(0,fileName.length()-4)+".xls"), "windows-1251"); //файл создаётся новый или затирается существующий
                fileWriter.append(HEADER);
            }
            else fileWriter = new OutputStreamWriter(new FileOutputStream("C:/550P/output/"+fileName,true), "windows-1251");//true в FileOutputStream() делает допись в конец файла

            for(person p : personList){
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(p.getNoteId());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getClientType());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getFio());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getInn());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getPolnNaim());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getRazdel());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getUchStatus());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getDocumentNum());
                fileWriter.append(DELIMITER);
                fileWriter.append(p.getSourceFileName());
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // метод сбора файлов из папки
    private static File[] getFiles(String dir, String ext) {
        File file = new File(dir);
        if(!file.exists()) System.out.println(dir + " папка не существует");
        File[] listFiles = file.listFiles(new MyFileNameFilter(ext));
        /*
        if(listFiles.length == 0){
            System.out.println(dir + " не содержит файлов с расширением " + ext);
        }else{
            for(File f : listFiles)
                System.out.println("Файл: " + dir + File.separator + f.getName());
        }
        */
        return listFiles;
    }

    // Реализация интерфейса FileNameFilter
    public static class MyFileNameFilter implements FilenameFilter {

        private String ext;

        public MyFileNameFilter(String ext){
            this.ext = ext.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(ext);
        }
    }

    public static String getCurrDate(){
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat("yyyyMMdd").format(curTime);
        return curStringDate;
    }

}
