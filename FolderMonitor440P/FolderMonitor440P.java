import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;


public class FolderMonitor440P extends Application {

    Stage window;
    TableView<Folder> tbl;
    Button button;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("FolderMon440P 1.0 (приложение для анализа содержимого папок по 440П)");
        window.setOnCloseRequest(e -> closeProgram());
        window.setHeight(800);

        TableColumn<Folder, String> foldName = new TableColumn<>("Имя папки");
        foldName.setMinWidth(220);
        foldName.setCellValueFactory(new PropertyValueFactory<Folder, String>("name"));
        TableColumn<Folder, String> startDate = new TableColumn<>("Дата папки");
        startDate.setMinWidth(100);
        startDate.setCellValueFactory(new PropertyValueFactory<Folder, String>("dateStart"));
        TableColumn<Folder, String> state = new TableColumn<>("Предупреждения");
        state.setMinWidth(400);
        state.setCellValueFactory(new PropertyValueFactory<Folder, String>("state"));
        TableColumn<Folder, String> filesSymb = new TableColumn<>("Содержимое");
        filesSymb.setMinWidth(500);
        filesSymb.setCellValueFactory(new PropertyValueFactory<Folder, String>("filesSymb"));

        tbl = new TableView<>();
        tbl.setPrefHeight(800);
        tbl.setItems(getFolder());
        tbl.getColumns().addAll(foldName,startDate,state,filesSymb);


        button = new Button();
        button.setText("Обновить");
        button.setOnAction(e -> {
            tbl = new TableView<>();
            tbl.setPrefHeight(800);
            tbl.setItems(getFolder());
            tbl.getColumns().addAll(foldName,startDate,state,filesSymb);

            VBox vBox = new VBox();
            vBox.getChildren().addAll(tbl,button);

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(tbl,button);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();

    }

    //мониторинг папки
    public ObservableList<Folder> getFolder(){
        ObservableList<Folder> folders = FXCollections.observableArrayList();

        String path = "C:\\JavaProj\\FolderSort440P\\testFold\\sorted\\jey";//"E:\\JavaProj\\FolderMonitor440P\\sorted\\jey";
        String[] f = getFolderNamesList(path);
        for(int i = 0; i < f.length; i++){
            String[] nameParts = f[i].split("_");
            String content = getContent(path+"/"+f[i]);
            String prim = kwtAnalizer(path,f[i],content);
            //System.out.println(f[i]);

            String tmpDate; //обработка даты папки
            if(nameParts[1].length()==8)
                tmpDate = nameParts[1];
            else
                tmpDate = nameParts[1].substring(4);

            folders.add(new Folder(f[i],tmpDate,prim,content));
        }
        /*
        folders.add(new Folder("RPO_fhfhf_262622","20171211","err","RPO, KWT"));
        folders.add(new Folder("ZSV_fhfhf_262622","20171203","finished","ZSV, KWT_ZSV"));
        folders.add(new Folder("BOS_fhfhf_262622","20171225","err","PB1_BOS, KWT"));
        folders.add(new Folder("AZS_fhfhf_262622","20171202","NaN","AZS, KWT"));
        folders.add(new Folder("ROO_fhfhf_262622","20171223","waiting kwt","ROO, PB1_ROO, KWT_PB1"));
        */
        return folders;
    }

    private void closeProgram(){
        System.out.println("Program is closed.");
    }

    public String kwtAnalizer(String path, String folder, String content){
        //System.out.println(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String str = "";

        File file = new File(path+"/"+folder);
        File[] listKwtFiles = file.listFiles(new MyFileNameFilter(".xml","kwt"));
        boolean approved = true;
        for (File f: listKwtFiles
             ) {
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(f);
                NodeList nodeList = doc.getElementsByTagName("Результат");
                String codRezProver = nodeList.item(0).getAttributes().getNamedItem("КодРезПроверки").getTextContent();
                //System.out.println(str);
                if(approved) approved = codRezProver.equals("01");
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        if(!approved) str = " ВНИМАНИЕ: негативная KWT.";

        //проверка на достаточность по content
        String[] cnt = content.split("; ");
        String start = "";
        for(int k = 0; k < cnt.length; k++){
            if(cnt[k].length()==3){
                if(folder.contains(cnt[k])) start = cnt[k];//символ начального запроса
                cnt[k]=""; //убираем начальный запрос RPO,ROO,... и.т.д.
            }
            if(cnt[k].toLowerCase().contains("kwt")){
                String[] kwt = cnt[k].split("_");
                A: for(int l = 0; l < cnt.length; l++){
                    if(cnt[l].toLowerCase().contains(kwt[1].toLowerCase()) && cnt[l].toLowerCase().contains(kwt[2].toLowerCase()) &&l!=k){
                        cnt[l]="";
                        cnt[k]="";
                        break A;
                    }
                }
            }
        }
        for (String s: cnt) cnt[0] = cnt[0] + s; //будет "" если нашлись все соответствия
        //System.out.println("|"+cnt[0]+"|");
        if(!cnt[0].equals("")||start.equals(""))
            str = str + " Нехватка файлов - проверяйте.";

        File[] listAllFiles = file.listFiles(new MyFileNameFilter(".xml",""));
        for (File f: listAllFiles) {
            if(!f.getName().toLowerCase().contains(folder.toLowerCase()))
            {
                str = "Лишние файлы - проверяйте." + str;
                break;
            }
        }

        return str;
    }

    public String getContent(String path){
        String[] filesList = getFolderNamesList(path);
        String str = "";
        for(int j = 0; j < filesList.length; j++){
            //str = str + filesList[j]+";";

            if(filesList[j].contains("xml")||filesList[j].contains("XML")){
                //str = str + filesList[j]+";";
                String[] partsFileName = filesList[j].split("_");
                //System.out.println(filesList[j]);
                if(partsFileName[0].toLowerCase().equals("kwtfcb"))
                    str = str + partsFileName[0]+"_"+partsFileName[1]+"_"+partsFileName[2].substring(0,3)+"; ";
                else if(partsFileName.length==3)
                    str = str + partsFileName[0].substring(0,3)+"; ";
                else
                    str = str + partsFileName[0]+"_"+partsFileName[1].substring(0,3)+"; ";
            }

        }
        return str;
    }

    public String[] getFolderNamesList(String path){
        File file = new File(path);
        File[] folders = file.listFiles();
        String[] folderNames = new String[folders.length];

        for (int i = 0; i < folderNames.length; i++) {
            folderNames[i] = folders[i].getName();
        }
        return folderNames;
    }

    //выбирает xml с kwt
    public class MyFileNameFilter implements FilenameFilter {
        private String extention;
        private String word;

        public MyFileNameFilter(String extention, String word){
            this.extention = extention.toLowerCase();
            this.word = word.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name){
            return name.toLowerCase().contains(word) && name.toLowerCase().endsWith(extention);
        }
    }


}
