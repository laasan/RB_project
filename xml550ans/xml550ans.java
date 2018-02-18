import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class xml550ans {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        int nextOrExit = JOptionPane.YES_OPTION;
        while(nextOrExit== JOptionPane.YES_OPTION) {
            JFileChooser fileopen = new JFileChooser("C:/JavaProj/xml550ans");
            int ret = fileopen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                String fileName = file.getName();
                //String filePath = file.getPath();
                //System.out.print(filePath);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                //String fileName = "CB_ES550P_20170627_002.XML";
                String IdnOR = "2313_0000";
                String OPER = "Субботина Т.В.";
                String TEL_OPER = "8(499) 241-63-90";
                long curTime = System.currentTimeMillis();
                String curStringDate = new SimpleDateFormat("dd/MM/yyyy").format(curTime);
                String curStringTime = new SimpleDateFormat("HH:mm:ss").format(curTime);

                //File file = new File(fileName);
                long fileSize = file.length();

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(fileName);

                Node dateMsgNode = doc.getElementsByTagName("ДатаСообщения").item(0);
                String dateMsg = dateMsgNode.getTextContent();

                NodeList numZapList = doc.getElementsByTagName("НомерЗаписи");
                /*
                for (int i = 0; i < numZapList.getLength(); i++) {
                    System.out.println(numZapList.item(i).getTextContent());
                }
                */
                FileWriter writeFile = null;
                try {
                    File xmlAns = new File("UV_"+IdnOR+"_"+ fileName.substring(0,fileName.length()-4) +".xml");
                    writeFile = new FileWriter(xmlAns);

                    writeFile.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    writeFile.append("<KVIT>\n");
                    writeFile.append("<IDNOR>"+IdnOR+"</IDNOR>\n");
                    writeFile.append("<ES>"+fileName+"</ES>\n");
                    writeFile.append("<SIZE_ES>"+fileSize+"</SIZE_ES>\n");
                    writeFile.append("<DATE_ES>"+dateMsg+"</DATE_ES>\n");
                    writeFile.append("<RECNO_ES nRec=\""+numZapList.getLength()+"\">\n");
                    for (int i = 0; i < numZapList.getLength(); i++) {
                        writeFile.append("<ES_REC IdInfoOR=\""+numZapList.item(i).getTextContent()+"\">\n");
                        writeFile.append("<REZ_ES>0</REZ_ES>\n");
                        writeFile.append("</ES_REC>\n");
                    }
                    writeFile.append("</RECNO_ES>\n");
                    writeFile.append("<DATE_KVIT>"+curStringDate+"</DATE_KVIT>\n");
                    writeFile.append("<TIME_KVIT>"+curStringTime+"</TIME_KVIT>\n");
                    writeFile.append("<OPER>"+OPER+"</OPER>\n");
                    writeFile.append("<TEL_OPER>"+TEL_OPER+"</TEL_OPER>\n");
                    writeFile.append("</KVIT>\n");

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(writeFile != null) {
                        try {
                            writeFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
