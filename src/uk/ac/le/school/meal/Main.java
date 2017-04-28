package uk.ac.le.school.meal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("index.fxml"));
        Image icon = new Image("file:Free-School-Meals.jpg");
        primaryStage.setTitle("School Meal");
        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.show();
       //new MainController().primaryScene(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
