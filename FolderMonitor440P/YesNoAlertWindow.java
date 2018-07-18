import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class YesNoAlertWindow {
    static boolean display(String title, String msg){
        Stage window = new Stage();
        final Boolean[] answ = {false};


        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(350);
        window.setHeight(200);
        window.setResizable(false);

        Label label = new Label();
        label.setText(msg);
        label.setWrapText(true);
        label.setMaxWidth(350);
        Button yesButton = new Button("Да, всё проверено");
        yesButton.setOnAction(e -> {
            window.close();
            answ[0] = true;
        });

        Button noButton = new Button("Нет");
        noButton.setOnAction(e -> {
            window.close();
            answ[0] = false;
        });

        VBox layout = new VBox(10);
        HBox btnBox = new HBox(10);
        btnBox.getChildren().addAll(noButton,yesButton);
        layout.getChildren().addAll(label,btnBox);
        layout.setPadding(new Insets(10,10,10,10));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answ[0];
    }
}
