import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;


public class FolderMonitor440P extends Application {

    private Stage window;
    private TableView<Folder> tbl;
    private Button button;
    private CheckBox checkBox = new CheckBox("При обновлении отображать с архивными");
    //private ProgressBar pb = new ProgressBar();

    //private static String path = "G:\\OTCH_CB\\440-П\\testFold\\testForMonitor440\\rum";
    //private static String path = "G:\\OTCH_CB\\440-П\\jey";
    //private static String path = "G:\\OTCH_CB\\440-П\\kramar";
    //private static String path = "G:\\OTCH_CB\\440-П\\rum";
    static String path = "D:\\JavaProj\\FolderSort440P\\testFold\\sorted\\jey";
    //private static String path = "G:\\OTCH_CB\\440-П\\rum";
    //private static String path = "D:\\JavaProj\\FolderMonitor440P\\sorted\\jey";
    static String log = ""; //если сделать private, то RenameDir не возьмёт, по дефолту он package-private
    static boolean archAvailable = true;
    static boolean admin = false;

    public static void main(String[] args){//args[0] - путь, args[1] - noarch или пустота
       // try{
            if(args.length!=0) path = "G:\\OTCH_CB\\440-П\\" + args[0];
            if(args.length>1&&args[1].equals("noarch")) archAvailable = false;

            log = path + "\\logMonitor440P";

            if(archAvailable&&!admin)
                LogWriter.write(log,GetDayTime.now() + " START prog");
            if(admin)
                LogWriter.write(log,GetDayTime.now() + " START prog Admin");

            //System.out.println("*"+getInf(path+"/"+"20171214_ROO14525294_502720171213_000667"/*f.get(i)*/+"/inf")+"*");
            launch(args);
        /*
        } catch (IllegalStateException ex){
            AlertWindow.display("Ошибка","Ошибка на входе в программу. Error 3.");
        }
        */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("FolderMon440P 2.0 (помощник мониторинга папки: "+path+" )");
        window.setOnCloseRequest(e -> closeProgram());
        window.setHeight(600);

        button = new Button();
        button.setText("Обновить");
        button.setOnAction(e -> showWin());

        showWin();
    }

    //обнов\отрисовка окна
    private void showWin(){
        Stage windowWait = new Stage();
        windowWait.setTitle("Подождите идёт обработка...");
        Label labelWait = new Label();
        Scene sceneWait = new Scene(labelWait,300,1);
        windowWait.setScene(sceneWait);
        windowWait.show();

        TableColumn<Folder, String> foldName = new TableColumn<>("Имя папки");
        foldName.setMinWidth(280);
        foldName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Folder, String> startDate = new TableColumn<>("Дата папки");
        startDate.setMinWidth(100);
        startDate.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        TableColumn<Folder, String> changeDate = new TableColumn<>("Изменение");
        changeDate.setMinWidth(100);
        changeDate.setCellValueFactory(new PropertyValueFactory<>("dateChange"));
        TableColumn<Folder, String> state = new TableColumn<>("Предупреждения");
        state.setMinWidth(280);
        state.setCellValueFactory(new PropertyValueFactory<>("state"));
        TableColumn<Folder, String> filesSymb = new TableColumn<>("Содержимое");
        filesSymb.setMinWidth(500);
        filesSymb.setCellValueFactory(new PropertyValueFactory<>("filesSymb"));

        tbl = new TableView<>();
        tbl.setPrefHeight(800);
        ObservableList<Folder> folderObservableList = getFolder();
        tbl.setItems(folderObservableList);
        tbl.getColumns().addAll(foldName,startDate,changeDate,state,filesSymb);
        tbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tbl.setOnKeyPressed(ev -> {
            switch (ev.getCode()){
                case A: {
                    if(archAvailable){
                        //System.out.println("КЛАВА!!!"+e.getCode().toString());
                        ObservableList<Folder> selected;
                        selected = tbl.getSelectionModel().getSelectedItems();
                        String name;
                        String st;

                        if(!selected.isEmpty()){//строка или строки выбраны
                            for (Folder fold: selected) {
                                name =  fold.getName();
                                st = fold.getState();

                                if(!name.substring(0,1).equals("_")){//если не архивный
                                    if(st.equals(" ! КОРРЕКТНО (проверьте и отправьте в архив)")){//если нет предупреждений кроме " ! КОРРЕКТНО (проверьте и отправьте в архив)"
                                        if(RenameDir.rename(path+"/"+name,"_" + name)){
                                            fold.setName("_" + name);
                                            //System.out.println(fold.getName());
                                            //showWin();
                                            tbl.refresh();
                                        }
                                    }
                                    else{
                                        if(admin){
                                            if(RenameDir.rename(path+"/"+name,"_" + name)){
                                                fold.setName("_" + name);
                                                //showWin();
                                                tbl.refresh();
                                            }
                                        }
                                        else{
                                            boolean yesRename = YesNoAlertWindow.display("Предупреждение!!!","Внимание! Папка "+fold.getName()+" содержит не исправленные предупреждения. Перепроверьте.\r\n\r\nВы уверены, что её можно отправлять в архив?");
                                            //System.out.println(yesRename);
                                            if(yesRename){
                                                if(RenameDir.rename(path+"/"+name,"_" + name)){
                                                    //showWin();
                                                    fold.setName("_" + name);
                                                    tbl.refresh();
                                                }
                                            }
                                        }
                                    }
                                }
                                else //if(!selected.isEmpty())
                                    AlertWindow.display("Ошибка!","Папка " + fold.getName() + " уже архивная.");
                            }
                        }
                        else
                            AlertWindow.display("Ошибка!","Не выбрана строка.");
                    }

                    break;
                }
                case L: {
                    boolean look = true;
                    while(look){
                        ObservableList<Folder> selected;
                        selected = tbl.getSelectionModel().getSelectedItems();
                        if(selected.size()>1){
                            AlertWindow.display("Ошибка","Выбрано более одной строки.\r\nДля просмотра содержимого надо выбирать одну строку.");
                            break;
                        }
                        String name = selected.get(0).getName();
                        JFileChooser fileopen = new JFileChooser(path+"/"+name);//"C:\\");
                        window.hide();

                        int ret = fileopen.showDialog(null, "Посмотреть содержимое");

                        if (ret == JFileChooser.APPROVE_OPTION) {
                            File fileView = fileopen.getSelectedFile();
                            try {
                                ProcessBuilder pb = new ProcessBuilder("C:\\Program Files\\Internet Explorer\\iexplore.exe", fileView.getPath());
                                pb.start();
                            } catch (IOException e) {
                                AlertWindow.display("Ошибка","Ошибка открытия файла. Сообщите разработчику.\r\nОжидаемый путь iexplorer: C:\\Program Files\\Internet Explorer\\iexplore.exe");
                            }
                        }

                        if(ret==JFileChooser.CANCEL_OPTION)
                            look = false;
                    }
                    window.show();

                    break;
                }
            }
        });

        Label lbl = new Label("действия через клавиатуру:\r\nArchiv: для переименования в архивную, выберите одну или несколько строк и нажмите клавишу \"A\"(она же русская \"Ф\")\r\nLook: для просмотра содержимого папки и содержимого её файлов, выберите строку и нажмите клавишу \"L\"(она же русская \"Д\")");
        if(!archAvailable)
            lbl.setText("действия через клавиатуру:\r\nArchiv: недоступно (сокращённая версия)\r\nLook: для просмотра содержимого папки и содержимого её файлов, выберите строку и нажмите клавишу \"L\"(она же русская \"Д\")");
        lbl.setMinHeight(50);
        HBox hBox = new HBox(50);
        hBox.getChildren().addAll(button,checkBox,lbl);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox,tbl);
        vBox.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(vBox);
        window.setScene(scene);

        windowWait.close();

        window.show();
    }

    //мониторинг папки
    private ObservableList<Folder> getFolder(){
        ObservableList<Folder> folders = FXCollections.observableArrayList();
        List<String> f = getFolderNamesList(path);
        //String[][] tempArr = new String[4][f.size()];
        List<String> tempList = new ArrayList<>();
        for(int i = 0; i < f.size(); i++){
            //System.out.println(f.get(i));
            if(f.get(i).contains("_")){ //отсев папок без "_"
                String tmpStr;
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
                //System.out.println(nameParts.length);
                if(nameParts[0]!=null)
                    tmpDate = nameParts[0];
                else
                    tmpDate = nameParts[1];

                String changeDate = getInf(path+"/"+f.get(i)+"/inf");
                if(changeDate==null) changeDate = "no inf";

                folders.add(new Folder(f.get(i),tmpDate,changeDate,prim,content));
            }
        }

        folders.sort(Folder.Comparators.STATE);

        return folders;
    }

    private void closeProgram(){
        //System.out.println("Program is closed.");
        if(archAvailable&&!admin)
            LogWriter.write(log,GetDayTime.now()+" EXIT prog");
        if(admin)
            LogWriter.write(log,GetDayTime.now()+" EXIT prog Admin");
    }

    private String kwtAnalizer(String path, String folder, String content){
        //System.out.println(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        String str = "";

        File file = new File(path+"/"+folder);
        File[] listKwtFiles = file.listFiles(new MyFileNameFilter(".xml","kwt"));
        boolean approved = true;
        if (listKwtFiles != null) {
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
                    System.out.println("SAXException");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException");
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    System.out.println("ParserConfigurationException");
                }
            }
        }
        if(!approved) str = " ВНИМАНИЕ: негативная KWT.";

        //проверка на достаточность по content
        String[] cnt = content.split("; ");
        String start = "";
        boolean notPbAnswExistance = false; //ловля не PB ответов (BOS и т.п.)
        for(int k = 0; k < cnt.length; k++) {
            if (cnt[k].length() == 3) {
                if (folder.contains(cnt[k])) start = cnt[k];//символ начального запроса
                cnt[k] = ""; //убираем начальный запрос RPO,ROO,... и.т.д.
            }
        }

        //System.out.println(start);

        for(int k = 0; k < cnt.length; k++) {
            //проверка на наличие ответа не PB и не kwt(д.б. обязательно?)
            for (int l = 0; l < cnt.length; l++) {
                //System.out.println(cnt[l]+" "+start+" "+cnt[l].contains(start)+" "+cnt[l].toLowerCase().contains("kwt")+" "+cnt[l].toLowerCase().contains("pb"));
                if (start.length() == 3 && cnt[l].contains(start) && !cnt[l].toLowerCase().contains("kwt") && !cnt[l].toLowerCase().contains("pb"))//содержит стартовый запрос но не kwt и не pb-шка
                    notPbAnswExistance = true;//т.е. есть не pb-шный ответ
            }

            if (cnt[k].toLowerCase().contains("kwt")) {
                String[] kwt = cnt[k].split("_");
                A:
                for (int l = 0; l < cnt.length; l++) {
                    if (cnt[l].toLowerCase().contains(kwt[1].toLowerCase()) && cnt[l].toLowerCase().contains(kwt[2].toLowerCase()) && l != k) {
                        cnt[l] = "";
                        cnt[k] = "";
                        break A;
                    }
                }
            }
        }
        //System.out.println(notPbAnswExistance+" "+folder);
        for (String s: cnt) cnt[0] = cnt[0] + s; //будет "" если нашлись все соответствия
        //System.out.println("|"+cnt[0]+"|");
        if(!cnt[0].equals("")||start.equals("")||!notPbAnswExistance)//не "очистилось" cnt или нет стартового запроса или нет не-PBшного ответа(должен быть всегда?)
            str = str + " Возможна нехватка файлов - проверяйте.";

        File[] listAllFiles = file.listFiles(new MyFileNameFilter(".xml",""));
        if (listAllFiles != null) {
            for (File f: listAllFiles) {
                String fldrName;
                fldrName = folder.toLowerCase();
                if(fldrName.substring(0,1).equals("_"))
                    fldrName = fldrName.substring(1); //убираем архивное подчеркивание если есть
                fldrName = fldrName.substring(fldrName.indexOf("_")+1);//теперь убираем начальную дату папки
                if(!f.getName().toLowerCase().contains(fldrName))
                {
                    str = "Лишние файлы - проверяйте." + str;
                    break;
                }
            }
        }
        else
            System.out.println("Есть папки не содержащие xml");

        if(str.equals("")) str = " ! КОРРЕКТНО (проверьте и отправьте в архив)";

        return str;
    }

    private static String getInf(String inf) {
        String s = null;
        try {
            BufferedReader fReader = new BufferedReader(new InputStreamReader(new FileInputStream(inf), "Windows-1251"));
            s = fReader.readLine();
            fReader.close();
        } catch (UnsupportedEncodingException e) {
            LogWriter.write(log, "Ошибка кодировки inf.");
        } catch (FileNotFoundException e) {
            LogWriter.write(log, "Не найден inf.");
        } catch (IOException e) {
            LogWriter.write(log, "Ошибка с inf.");
        }

        return s;
    }

    private String getContent(String path){
        //String[] filesList = getFolderNamesList(path);
        List<String> filesList = getFileNamesList(path);//getFolderNamesList(path);
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
            if(folders[i].isDirectory()&&folders[i].getPath().equals(path+"\\"+folders[i].getName())){//что папка и чтобы в подпапки не лез
                //System.out.println(folders[i].isDirectory()+" "+folders[i].getPath()+" "+path+"\\"+folders[i].getName());
                //folderNames[i] = folders[i].getName();
                if(checkBox.isSelected())
                    folderNamesList.add(folders[i].getName());//все берём
                else{//если чекбокс не помечен
                    if(!folders[i].getName().substring(0,1).equals("_"))//если не начинается с "_"
                        folderNamesList.add(folders[i].getName());
                }
                //System.out.println(folders[i].getName()+" "+folders[i].getPath());
            }

        }
        return folderNamesList;
    }

    private List<String> getFileNamesList(String path){
        File file = new File(path);
        File[] files = file.listFiles();
        //String[] folderNames = new String[folders.length];
        List<String> fileNamesList = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            if(files[i].isFile())//файлы а не папки
                fileNamesList.add(files[i].getName());
            /*
            {//файлы а не папки
                //folderNames[i] = folders[i].getName();
                if(checkBox.isSelected())
                    fileNamesList.add(files[i].getName());//все берём
                else{//если чекбокс не помечен
                    if(!files[i].getName().substring(0,1).equals("_"))//если не начинается с "_"
                        fileNamesList.add(files[i].getName());
                }
                //System.out.println(files[i].getName()+" "+files[i].getPath());
            }
            */
        }
        return fileNamesList;
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
