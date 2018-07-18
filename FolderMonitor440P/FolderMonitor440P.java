import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.EventHandler;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FolderMonitor440P extends Application {

    Stage window;
    TableView<Folder> tbl;
    Button button;
    Button btnArch;
    CheckBox checkBox = new CheckBox("Отображать с архивными");

    //String path = "G:\\OTCH_CB\\440-П\\testFold\\testForMonitor440\\rum";
    //String path = "C:\\JavaProj\\FolderSort440P\\testFold\\sorted\\jey";//"E:\\JavaProj\\FolderMonitor440P\\sorted\\jey";
    String path = "D:\\JavaProj\\FolderMonitor440P\\sorted\\jey";


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("FolderMon440P 1.0 (приложение для анализа содержимого папок по 440П)");
        window.setOnCloseRequest(e -> closeProgram());
        window.setHeight(600);

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

            HBox hBox = new HBox(100);
            hBox.getChildren().addAll(button,checkBox, btnArch);

            VBox vBox = new VBox(10);
            vBox.getChildren().addAll(hBox,tbl);
            vBox.setPadding(new Insets(10,10,10,10));

            Scene scene = new Scene(vBox);
            window.setScene(scene);
            window.show();
        });




        btnArch = new Button();
        btnArch.setText("Переименовать папку в архивную");
        btnArch.setOnAction(e -> btnArchAction());
        tbl.setOnKeyPressed(e -> {
            switch (e.getCode()){
                case A: System.out.println("КЛАВА!!!");
            }

        });

        checkBox = new CheckBox("Отображать с архивными");
        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(button,checkBox, btnArch);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox,tbl);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    private void btnArchAction(){
        ObservableList<Folder> selected;
        selected = tbl.getSelectionModel().getSelectedItems();
        String name =  "";

        if(!selected.isEmpty())
            name =  selected.get(0).getName();
        else
            AlertWindow.display("Ошибка!","Не выбрана строка.");

        if(!name.substring(0,1).equals("_")){//если не архивный
            selected.get(0).setName("_" + name);
            RenameDir.rename(path+"/"+name,"_" + name);
            //System.out.println(selected.get(0).getName());
        }
        else if(!selected.isEmpty())
            AlertWindow.display("Ошибка!","Папка " + selected.get(0).getName() + " уже архивная.");

    }

    //мониторинг папки
    private ObservableList<Folder> getFolder(){
        ObservableList<Folder> folders = FXCollections.observableArrayList();

       //String[] f = getFolderNamesList(path);
        List<String> f = getFolderNamesList(path);
        for(int i = 0; i < f.size(); i++){
            String tmpStr = "";
            if(f.get(i).substring(0,1).equals("_"))
                tmpStr = f.get(i).substring(1);
            else
                tmpStr = f.get(i);
            //System.out.println(f.get(i).substring(0,1));
            String[] nameParts = tmpStr.split("_");
            String content = getContent(path+"/"+f.get(i));
            String prim = kwtAnalizer(path,f.get(i),content);
            //System.out.println(f.get(i));

            String tmpDate; //обработка даты папки
            if(nameParts[1].length()==8)
                tmpDate = nameParts[1];
            else
                tmpDate = nameParts[1].substring(4);

            folders.add(new Folder(f.get(i),tmpDate,prim,content));
        }
        return folders;
    }

    private void closeProgram(){
        System.out.println("Program is closed.");
    }

    private String kwtAnalizer(String path, String folder, String content){
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
        if (listAllFiles != null) {
            for (File f: listAllFiles) {
                String fldrName;
                fldrName = folder.toLowerCase();
                if(fldrName.substring(0,1).equals("_"))
                    fldrName = fldrName.substring(1); //убираем архивное подчеркивание если есть
                if(!f.getName().toLowerCase().contains(fldrName))
                {
                    str = "Лишние файлы - проверяйте." + str;
                    break;
                }
            }
        }
        else
            System.out.println("Есть папки не содержащие xml");

        if(str.equals("")) str = "ПРОВЕРЬТЕ И ОТПРАВЬТЕ В АРХИВ";

        return str;
    }

    private String getContent(String path){
        //String[] filesList = getFolderNamesList(path);
        List<String> filesList = getFolderNamesList(path);
        String str = "";
        for(int j = 0; j < filesList.size(); j++){
            //str = str + filesList[j]+";";

            if(filesList.get(j).contains("xml")||filesList.get(j).contains("XML")){
                //str = str + filesList[j]+";";
                String[] partsFileName = filesList.get(j).split("_");
                //System.out.println(filesList.get(j));
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

    private List<String> getFolderNamesList(String path){
        File file = new File(path);
        File[] folders = file.listFiles();
        //String[] folderNames = new String[folders.length];
        List<String> folderNamesList = new ArrayList<>();


        for (int i = 0; i < folders.length; i++) {
            //folderNames[i] = folders[i].getName();
            if(checkBox.isSelected())
                folderNamesList.add(folders[i].getName());//все берём
            else{//если чекбокс не помечен
                if(!folders[i].getName().substring(0,1).equals("_"))//если не начинается с "_"
                    folderNamesList.add(folders[i].getName());
            }
        }
        return folderNamesList;
    }

    //выбирает xml с kwt
    public class MyFileNameFilter implements FilenameFilter {
        private String extention;
        private String word;

        private MyFileNameFilter(String extention, String word){
            this.extention = extention.toLowerCase();
            this.word = word.toLowerCase();
        }
        @Override
        public boolean accept(File dir, String name){
            return name.toLowerCase().contains(word) && name.toLowerCase().endsWith(extention);
        }
    }


}
