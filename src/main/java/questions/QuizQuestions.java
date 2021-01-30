package questions;

import gui.account.Account;
import gui.popup.notification.UserNotifier;
import gui.quiz.QuizHelper;
import org.json.JSONException;
import questions.question.Question;
import questions.question.QuestionNode;
import requests.AnswerJSONRequest;
import requests.QuizJSONRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stores a static array of questionNodes to be accessed by QuizController for display, initializes preferences
 */
public class QuizQuestions {

    //contains the questions, responses, and javafx information
    private static QuestionNode[] questionNodes;

    private QuizQuestions() {
    }

    /**
     * Initialize questionNodes with specific questions, utilizes QuestionNodeArrayFactory
     *
     * @param amount amount of questions wanted. 0 if maximum possible.
     */
    public static void initializeQuiz(int amount) throws IllegalArgumentException, JSONException, IOException, InterruptedException {
        try {

            //Initialize the request, storing json in the object.
            QuizJSONRequest request = new QuizJSONRequest(Account.getUser()).initializeRequest();

            //Create a pool of question id's in the size of how many questions available, randomize order
            List<Integer> idPool = new LinkedList<>();
            for (int i = 0; i < request.getQuestionJSON().length(); i++) {
                idPool.add(i);
            }

            Collections.shuffle(idPool);

            //Initialize preferences
            System.out.println(request.getPreferenceJSON());
            QuizHelper.Preference.initializePreferences(request.getPreferenceJSON());

            //Initialize questions
            questionNodes = QuestionNodeFactory.nodeArrayFromJSON(request.getQuestionJSON(), amount, idPool);

        } catch (Exception e) {

            //Rethrow exception but get rid of any questions that are invalidly loaded.
            questionNodes = null;
            throw e;
        }
    }

    /**
     * Grades stored QuestionNodes utilizing QuestionAnswerHelper class
     */
    public static void gradeAnswers() {
        try {

            //Attempt to set answers for all questions
            QuestionAnswerHelper.setAnswers(questionNodes, new AnswerJSONRequest(questionNodes).initializeRequest().getJson());

        } catch (Exception e) {

            new UserNotifier("A question failed to be graded.").display();

            e.printStackTrace();
        }

        for (QuestionNode questionNode : questionNodes) {

            if (questionNode.isAnswered()) {

                List<String> response = questionNode.getResponse();
                List<String> answer = questionNode.getAnswer();

                //user input type might be capitalized or spaced wrong. handle differently
                if (questionNode.getType() == Question.Type.WRITTEN) {
                    //Set to all lowercase and no spaces for minimal input based error
                    response = response.stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());

                    answer = questionNode.getAnswer().stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());
                }

                //Answer may be larger than one, so .containsAll is used
                //Check if answer is correct, if no response then mark wrong.
                questionNode.setCorrect(answer.containsAll(response));

            } else {
                questionNode.setCorrect(false);
            }
        }
    }

    public static QuestionNode[] getQuestionNodes() {

        return questionNodes;

    }

}

