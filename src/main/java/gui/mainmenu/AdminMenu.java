package gui.mainmenu;

import gui.StageHolder;
import gui.account.Account;
import gui.account.Quiz;
import gui.etc.FXHelper;
import gui.mainmenu.quizbuilder.QuizBuilderTool;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONException;
import requests.DatabaseRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pane for displaying in MainMenu.
 */
public class AdminMenu extends StageHolder implements Initializable {

    @FXML
    TableView<Quiz> quizzesTable;

    @FXML
    TableColumn<Quiz, String> nameColumn, keyColumn;

    //Instances of other stages this scene can open
    private Stage quizBuilder = new Stage();
    private Stage quizUpload = new Stage();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set column constructors
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));

        try {

            //Try to fetch keys from database
            quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

        } catch (InterruptedException | IOException e) {

            e.printStackTrace();


        } catch (JSONException ignored) {
        }

    }

    public void uploadQuizClicked() {

        try {

            if (!quizUpload.isShowing()) {

                this.quizUpload = FXHelper.getPopupStage(FXHelper.Window.QUIZ_UPLOAD, false);

                //Pass the stage to the quizUpload so it can close itself
                QuizUpload.setStage(quizUpload);

                quizUpload.showAndWait();

                quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

            }

        } catch (Exception e) {

            userNotifier.setText("An unknown internal error occurred.").display();

        }

    }

    public void quizBuilderClicked() {
        try {

            //Check if an instance is already open
            if (!quizBuilder.isShowing()) {

                quizBuilder = FXHelper.getPopupStage(FXHelper.Window.QUIZ_BUILDER, false);

                //Give stage to QuizBuilderTool so it can close itself
                QuizBuilderTool.setStage(quizBuilder);

                quizBuilder.show();

            }

        } catch (Exception e) {

            e.printStackTrace();

            userNotifier.setText("An unknown internal error occurred.").display();

        }
    }

    public void deleteQuizClicked() {

        try {

            if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

                if (confirmNotifier.setPrompt("Are you sure you want to delete: "
                        + quizzesTable.getSelectionModel().getSelectedItem().getName() +
                        "? All existing keys and scores will be lost.").display().getResponse()) {

                    switch (DatabaseRequest.deleteQuiz(quizzesTable.getSelectionModel().getSelectedItem(), Account.getUser())) {

                        case ACCEPTED -> quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

                        case NO_CONTENT -> userNotifier.setText("An error occurred while deleting the quiz.").display();

                        case NO_CONNECTION -> userNotifier.setText("Connection to the server failed.").display();
                    }
                }

            } else {

                userNotifier.setText("Please select a created Quiz.").display();

            }

        } catch (Exception e) {

            e.printStackTrace();

            userNotifier.setText("An unknown internal error occurred.").display();

        }

    }

    public void getExcelClicked() {

//        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {
//
//            try {
//
//                //Table only records name and key, use Account.getUser to get remaining parts for path.
//                Quiz quiz = quizzesTable.getSelectionModel().getSelectedItem();
//                Account.setQuiz(Account.getUser().getUsername(), quiz.getName(), quiz.getKey());
//
//                //Request for answers along with the questions.
//                QuizJSONRequest request = new QuizJSONRequest(Account.getUser(),
//                        Constants.DEFAULT_PATH + "questions/answers/" + Account.getQuiz().getKey());
//
//                request.initializeRequest();
//
//                ExcelReader excelReader = new ExcelReader(request.getQuestionJSON());
//
//                //Open a DirectoryChooser to choose where to store the excel
//                DirectoryChooser directoryChooser = new DirectoryChooser();
//                File selectedDirectory = directoryChooser.showDialog(PrimaryStageHolder.getPrimaryStage());
//
//                FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/quiz.xlsx");
//
//                excelReader.jsonToExcel().write(outputStream);
//                outputStream.close();
//
//
//            } catch (ConnectException e) {
//
//                userNotifier.setText("Could not connect to server.").display();
//
//            } catch (Exception e) {
//
//                userNotifier.setText("An unknown error occurred").display();
//
//            }
//
//        } else {
//
//            userNotifier.setText("Please select a created Quiz.").display();
//
//        }

    }

}
