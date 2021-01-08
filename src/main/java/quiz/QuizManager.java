package quiz;

import database.DatabaseRequest;
import database.QuestionRequest;
import etc.Constants;
import gui.popups.ErrorBox;
import quiz.questions.Question;
import quiz.questions.QuestionFactory;
import quiz.questions.nodes.QuizNode;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores local quiz information, contains grading methods
 */
public class QuizManager {

    //Hashmap of user preferences
    private static final HashMap<String, String> preferences = new HashMap<>();

    //Array of QuizNodes that contain the questions, responses, and javafx information
    private static QuizNode[] quizNodes;

    //Index of the QuizNodes the quiz is currently showing
    private static int currQuestion = 0;

    /**
     * Load questions into QuizManager.questions.
     *
     * @param subject if not null limits questions to that specific subject.
     * @param type    if not null limits questions to that specific type.
     */
    public static void loadQuestions(int amount, @Nullable Question.Type type, @Nullable Question.Subject subject) {

        //Initiate the quizNodes as a static array with the amount specified
        quizNodes = new QuizNode[amount];

        //Create a pool of question id's in the specific size of how many questions available
        Integer[] array = IntStream.range(1, Constants.DATABASE_SIZE).boxed().toArray(Integer[]::new);
        List<Integer> idPool = Arrays.asList(array);

        //Randomize the pool
        Collections.shuffle(idPool);

        //Get rid of values that aren't needed
        idPool = idPool.subList(0, amount);

        //Initialize a request with given type and subject.
        //Count loop iterations for putting into QuizNodes
        QuestionRequest request = new QuestionRequest(type, subject);
        int i = 0;
        for (Integer id : idPool) {
            try {
                request.setId(id);

                //Request for JSON-Question with the given ID. Make that JSON into a Question object.
                Question question = QuestionFactory.questionFromJSON(
                        request.makeRequest().getJSON()
                );

                //Randomize the order of the options of the question.
                question.shuffleOptions();

                quizNodes[i] = new QuizNode(question);
                i++;

            } catch (Exception e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);

                //Clear quizNodes after any error.
                quizNodes = null;
            }
        }
    }

    /**
     * For loading a specific test through the test key.
     *
     * @param ids id of questions to load.
     */
    public static void loadQuestions(List<Integer> ids) {

        QuestionRequest request = new QuestionRequest();
        int i = 0;
        for (Integer id : ids) {
            try {

                request.setId(id);
                Question question = QuestionFactory.questionFromJSON(request.makeRequest().getJSON());
                question.shuffleOptions();
                quizNodes[i] = new QuizNode(question);
                i++;

            } catch (Exception e) {
                ErrorBox.display("A question failed to load. ID: " + id, false);
                quizNodes = null;
                e.printStackTrace();
            }
        }
    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static void checkAnswers() {

        for (QuizNode quizNode : quizNodes) {
            try {

                if (quizNode.isAnswered()) {

                    List<String> response = quizNode.getResponse();

                    List<String> answer = QuestionFactory.answerFromJSON(DatabaseRequest.getQuestionAnswer(quizNode.getQuestion()));

                    //set question answer for use in Results
                    quizNode.getQuestion().setAnswer(answer);

                    //user input type might be capitalized or spaced wrong. handle differently
                    if (quizNode.getQuestion().getType() == Question.Type.WRITTEN) {

                        //Set to all lowercase and no spaces for minimal input based error
                        response = response.stream()
                                .map(String::toLowerCase)
                                .map(str -> str.replaceAll("\\s", ""))
                                .collect(Collectors.toList());

                        answer = answer.stream()
                                .map(String::toLowerCase)
                                .map(str -> str.replaceAll("\\s", ""))
                                .collect(Collectors.toList());
                    }

                    //Answer may be larger than one, so .containsAll is used
                    //Check if answer is correct, if no response then mark wrong.
                    quizNode.setCorrect(answer.containsAll(response));

                } else {
                    quizNode.setCorrect(false);
                }

            } catch (Exception e) {
                ErrorBox.display("A question failed to be graded. ID: " + quizNode.getQuestion().getID(), true);
                e.printStackTrace();
            }
        }
    }


    /**
     * Getters
     */

    public static int getCurrNum() {

        return currQuestion;

    }

    public static HashMap<String, String> getPreferences() {

        return preferences;
    }

    public static QuizNode[] getQuizNodes() {

        return quizNodes;
    }

    public static QuizNode getCurrNode() {

        return quizNodes[currQuestion];

    }

    public static Question getCurrQuestion() {

        return quizNodes[currQuestion].getQuestion();

    }

    public static boolean allResponded() {

        return Arrays.stream(quizNodes).noneMatch(quizNode -> quizNode.getResponse().isEmpty());

    }


    /**
     * Setters
     */

    public static void nextQuestion() {

        currQuestion++;

    }

    public static void prevQuestion() {

        currQuestion--;

    }

    public static void setCurrNum(int num) {

        currQuestion = num;

    }

}

