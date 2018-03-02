import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class finder550 extends Application {
    Stage window;
    TableView<person> table;
    Scene loginScene, mainScene;
    private String user;
    private String pass;

    public static void main(String[] args){

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("finder550");
        int recLimit = 1000;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel = new Label("Username:");
        GridPane.setConstraints(nameLabel,0,0);
        TextField nameInput = new TextField("Ivanov");
        GridPane.setConstraints(nameInput,1,0);
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel,0,1);
        PasswordField passInput = new PasswordField();
        passInput.setPromptText("password");
        GridPane.setConstraints(passInput,1,1);
        Button loginButton = new Button("Log in");
        GridPane.setConstraints(loginButton,1,2);

        loginButton.setOnAction(e -> {
            user = nameInput.getText();
            pass = passInput.getText();
            window.setScene(mainScene);
        });

        //VBox layout1 = new VBox(20);
        grid.getChildren().addAll(nameLabel,nameInput,passLabel,passInput,loginButton);
        loginScene = new Scene(grid,220,100);

        GridPane gridMain = new GridPane();
        gridMain.setPadding(new Insets(10,10,10,10));
        gridMain.setVgap(8);
        gridMain.setHgap(10);

        Label fioLabel = new Label("FIO содержит:");
        GridPane.setConstraints(fioLabel,0,0);
        TextField fioInput = new TextField();
        fioInput.setPromptText("фамилия или её часть");
        fioInput.setPrefWidth(300);
        GridPane.setConstraints(fioInput,1,0);
        Label innLabel = new Label("INN");
        GridPane.setConstraints(innLabel,0,1);
        TextField innInput = new TextField();
        innInput.setPromptText("ИНН если есть");
        GridPane.setConstraints(innInput,1,1);
        Label nomdocLabel = new Label("Номер документа без пробела");
        GridPane.setConstraints(nomdocLabel,0,2);
        TextField nomdocInput = new TextField();
        nomdocInput.setPromptText("серия и/или номер");
        GridPane.setConstraints(nomdocInput,1,2);

        Button button2 = new Button("Поиск");
        GridPane.setConstraints(button2,1,3);

        gridMain.getChildren().addAll(fioLabel,fioInput,innLabel,innInput,nomdocLabel,nomdocInput,button2);



        button2.setOnAction(e -> {
            //System.out.println(user+" "+pass);

            ObservableList<person> persons = FXCollections.observableArrayList();
            Connection connection = null;
            Statement stmt = null;
            String query = "select top " + recLimit + " * from tRB550 where FIO like('%"+fioInput.getText()+"%') and INN like ('%"+innInput.getText()+"%') and Passport like ('%"+nomdocInput.getText()+"%')";

            try {
                // Register JDBC Driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection("jdbc:sqlserver://dia7:1433;database=diatrain;",user,pass);

                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    //String accountNumber = rs.getString("accountNumber");
                    //System.out.println("Account Number: "+accountNumber);

                    //System.out.println(rs.getString(2));

                    person p = new person();
                    p.setNoteId(rs.getString(2));
                    p.setClientType(rs.getString(3));
                    p.setFio(rs.getString(4));
                    p.setInn(rs.getString(5));
                    p.setPolnNaim(rs.getString(6));
                    p.setRazdel(rs.getString(7));
                    p.setUchStatus(rs.getString(8));
                    p.setDocumentNum(rs.getString(9));
                    persons.add(p);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            TableColumn<person,String> noteIdColumn = new TableColumn<>("NoteID");//это как в таблице в MsSqlServer
            noteIdColumn.setMinWidth(200);
            noteIdColumn.setCellValueFactory(new PropertyValueFactory<>("noteId"));//это как поле в классе

            TableColumn<person,String> clientTypeColumn = new TableColumn<>("ClientType");
            clientTypeColumn.setMinWidth(20);
            clientTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ClientType"));

            TableColumn<person,String> fioColumn = new TableColumn<>("FIO");
            fioColumn.setMinWidth(200);
            fioColumn.setCellValueFactory(new PropertyValueFactory<>("Fio"));

            TableColumn<person,String> innColumn = new TableColumn<>("INN");
            innColumn.setMinWidth(100);
            innColumn.setCellValueFactory(new PropertyValueFactory<>("Inn"));

            TableColumn<person,String> polnNaimColumn = new TableColumn<>("FullName");
            polnNaimColumn.setMinWidth(200);
            polnNaimColumn.setCellValueFactory(new PropertyValueFactory<>("PolnNaim"));

            TableColumn<person,String> razdelColumn = new TableColumn<>("Part");
            razdelColumn.setMinWidth(20);
            razdelColumn.setCellValueFactory(new PropertyValueFactory<>("Razdel"));

            TableColumn<person,String> uchStatusColumn = new TableColumn<>("Status");
            uchStatusColumn.setMinWidth(20);
            uchStatusColumn.setCellValueFactory(new PropertyValueFactory<>("UchStatus"));

            TableColumn<person,String> documentNumColumn = new TableColumn<>("Passport");
            documentNumColumn.setMinWidth(100);
            documentNumColumn.setCellValueFactory(new PropertyValueFactory<>("DocumentNum"));

            table = new TableView<>();
            table.setItems(persons);
            table.getColumns().addAll(noteIdColumn,clientTypeColumn,fioColumn,innColumn,polnNaimColumn,razdelColumn,uchStatusColumn,documentNumColumn);

            //window.setScene(loginScene);
            StackPane layout2 = new StackPane();

            VBox vBox = new VBox();
            vBox.getChildren().addAll(table);

            VBox main = new VBox();
            main.getChildren().addAll(gridMain,vBox);

            layout2.getChildren().addAll(main);
            mainScene = new Scene(layout2,1000,300);

            window.setScene(mainScene);
            window.show();

            if(persons.size() == recLimit)
                AlertWindow.display("Внимание!","По данным параметрам поиска слишком много записей. Показаны "+ recLimit +" первых записей.");
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().addAll(gridMain);
        mainScene = new Scene(layout2,1000,300);

        window.setScene(loginScene);
        window.show();
    }

}
