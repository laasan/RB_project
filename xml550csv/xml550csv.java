import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class xml550csv {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        //String fileName = "CB_ES550P_20170627_002.XML";
        List<person> personList = new ArrayList<person>();
        String fileName = "";
        String fileNameList[] =
                {"CB_ES550P_20170626_001.XML","CB_ES550P_20170626_002.XML","CB_ES550P_20170626_003.XML","CB_ES550P_20170626_004.XML","CB_ES550P_20170626_005.XML",
                        "CB_ES550P_20170626_006.XML","CB_ES550P_20170626_007.XML","CB_ES550P_20170626_008.XML","CB_ES550P_20170626_009.XML","CB_ES550P_20170627_001.XML",
                        "CB_ES550P_20170627_002.XML","CB_ES550P_20170628_001.XML","CB_ES550P_20170628_002.XML","CB_ES550P_20170628_003.XML","CB_ES550P_20170629_001.XML",
                        "CB_ES550P_20170629_002.XML","CB_ES550P_20170629_003.XML","CB_ES550P_20170629_004.XML","CB_ES550P_20170629_005.XML","CB_ES550P_20170629_006.XML",
                        "CB_ES550P_20170629_007.XML","CB_ES550P_20170629_008.XML","CB_ES550P_20170629_009.XML","CB_ES550P_20170629_010.XML","CB_ES550P_20170629_011.XML",
                        "CB_ES550P_20170629_012.XML","CB_ES550P_20170629_013.XML","CB_ES550P_20170629_014.XML"};

        for(int filesNum = 0; filesNum < fileNameList.length; filesNum++){
            fileName = fileNameList[filesNum];
            System.out.println(fileName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //personList = new ArrayList<person>(); //раскоментить если каждый файл xml в отдельный файл
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fileName);

            person tempPerson;

            NodeList nodeList = doc.getElementsByTagName("Раздел1.1");
            if(nodeList.getLength()!=0)
                for(int i = 0; i < nodeList.getLength(); i++){
                    tempPerson = new person();
                    tempPerson.setRazdel("11");
                    //System.out.println(nodeList.item(i).getNodeName());

                    NodeList nodeList1 = nodeList.item(i).getChildNodes();
                    for(int j = 0; j < nodeList1.getLength(); j++){
                        Node node1 = nodeList1.item(j);
                        if(node1.getNodeType()==Node.ELEMENT_NODE){
                            if(node1.getNodeName().equals("НомерЗаписи"))tempPerson.setNoteId(node1.getTextContent());
                            if(node1.getNodeName().equals("ТипКлиента"))tempPerson.setClientType(node1.getTextContent());

                            if(node1.getNodeName().equals("СведФЛИП")) addFLIP(node1,tempPerson, personList);
                            else if(node1.getNodeName().equals("СведЮЛ")) addUL(node1, tempPerson, personList);
                            else if(node1.getNodeName().equals("СведИНБОЮЛ")) addINBOUL(node1, tempPerson, personList);

                            //nodeList1.item(j).getTextContent()
                            //System.out.println("  "+nodeList1.item(j).getNodeName()+" "+nodeList1.item(j).getTextContent());
                        }
                    }
                }

            nodeList = doc.getElementsByTagName("Раздел1.2");
            if(nodeList.getLength()!=0)
                for(int i = 0; i < nodeList.getLength(); i++){
                    tempPerson = new person();
                    tempPerson.setRazdel("12");

                    NodeList nodeList1 = nodeList.item(i).getChildNodes();
                    for(int j = 0; j < nodeList1.getLength(); j++) {
                        Node node1 = nodeList1.item(j);
                        if (node1.getNodeType() == Node.ELEMENT_NODE) {
                            if (node1.getNodeName().equals("НомерЗаписи")) tempPerson.setNoteId(node1.getTextContent());
                            if (node1.getNodeName().equals("ТипУчастника")) tempPerson.setClientType(node1.getTextContent());

                            if(node1.getNodeName().equals("СведФЛИП")) addFLIP(node1,tempPerson, personList);
                            else if(node1.getNodeName().equals("СведЮЛ")) addUL(node1, tempPerson, personList);
                            else if(node1.getNodeName().equals("СведИНБОЮЛ")) addINBOUL(node1, tempPerson, personList);
                        }
                    }
                }

            nodeList = doc.getElementsByTagName("Раздел2");
            if(nodeList.getLength()!=0)
                for(int i = 0; i < nodeList.getLength(); i++){
                    tempPerson = new person();
                    tempPerson.setRazdel("2");

                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE){
                        NodeList nodeList1 = nodeList.item(i).getChildNodes();
                        for(int j = 0; j < nodeList1.getLength(); j++) {
                            Node node1 = nodeList1.item(j);
                            if (node1.getNodeType() == Node.ELEMENT_NODE) {
                                if (node1.getNodeName().equals("НомерЗаписи")) tempPerson.setNoteId(node1.getTextContent());
                                if (node1.getNodeName().equals("Участник")){
                                    NodeList nodeListUch = node1.getChildNodes();
                                    for(int m = 0; m < nodeListUch.getLength(); m++){
                                        Node node = nodeListUch.item(m);
                                        if (node.getNodeName().equals("СтатусУчастника")){
                                            //System.out.println(node.getTextContent());
                                            tempPerson.setUchStatus(node.getTextContent());
                                        }
                                        if (node.getNodeName().equals("ТипУчастника")) tempPerson.setClientType(node.getTextContent());

                                        if(node.getNodeName().equals("СведФЛИП")) addFLIP(node,tempPerson, personList);
                                        if(node.getNodeName().equals("СведЮЛ")) addUL(node, tempPerson, personList);
                                        if(node.getNodeName().equals("СведИНБОЮЛ")) addINBOUL(node, tempPerson, personList);
                                    }
                                }
                            }
                        }
                    }

                }

            //CSVgen(personList,fileName); //раскоментить если каждый файл xml в отдельный файл

        }

        CSVgen(personList,"all550.XXX"); //закоментить если каждый файл xml в отдельный файл, раскоментить для единого сборища

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
        personList.add(tempPerson);
    }


    public static void CSVgen(List<person> personList,String fileName){
        String HEADER = "NoteId;ClientType;Fio;Inn;PolnNaim;Razdel;UchStatus;DocumentNum";
        String DELIMITER = ";";
        String NEW_LINE_SEPARATOR="\r\n";
        try {
            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(fileName.substring(0,fileName.length()-4)+".xls"), "windows-1251");
            fileWriter.append(HEADER);
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
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
