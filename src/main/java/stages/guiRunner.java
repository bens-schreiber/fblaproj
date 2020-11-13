package stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quiz.Quiz;


public class guiRunner extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        Quiz.loadQuestions(5);


        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));
//
//        Rectangle2D visualBounds= Screen.getPrimary().getVisualBounds(); //Get the screen size
//        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
        Scene scene = new Scene(root, 1980, 1080);
//        primaryStage.setAlwaysOnTop(true);
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
