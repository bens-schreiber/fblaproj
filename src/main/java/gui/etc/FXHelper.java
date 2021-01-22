package gui.etc;

import gui.PrimaryStageHolder;
import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashSet;

public class FXHelper {

    private FXHelper() {
    }

    private static final HashSet<Window> openedInstances = new HashSet<>();

    public static HashSet<Window> getOpenedInstances() {
        return openedInstances;
    }

    //Get a Scene from Window
    public static Scene getScene(Window window) throws IOException {
        return new Scene(FXMLLoader.load(FXHelper.class.getResource(window.getPath())));
    }

    /**
     * @param requireResponse if the stage should take priority out of all others.
     */
    public static Stage getPopupStage(Window window, boolean requireResponse) throws IOException {

        //Set stage, take ownership from primary stage so it stays on the same window
        Stage stage = new Stage();
        stage.initOwner(PrimaryStageHolder.getPrimaryStage());

        //Make stage required to be answered if needed
        if (requireResponse) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        Scene scene = new Scene(FXMLLoader.load(ErrorNotifier.class.getResource(window.getPath())));
        stage.setScene(scene);

        return stage;

    }

    //Window names, hashmap of window names and their corresponding fxml location
    public enum Window {
        STARTPAGE("/startpage.fxml"),
        PREMADEQUIZES("/premadequizes.fxml"),
        LOGIN("/login.fxml"),
        REGISTER("/quiz.fxml"),
        QUIZ("/quiz.fxml"),
        ENTERCODE("/entercode.fxml"),
        CALCULATOR("/calculator.fxml"),
        DRAWINGPAD("/drawingpad.fxml"),
        NOTEPAD("/notepad.fxml"),
        PRINTRESULTS("/printableresults.fxml"),
        SEERESULTS("/questionresults.fxml"),
        CUSTOMQUIZ("/customquiz.fxml"),
        ERROR("/errorScreen.fxml"),
        CONFIRM("/confirmScreen.fxml");

        private final String path;

        Window(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
