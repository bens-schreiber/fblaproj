package questions.nodes;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.Arrays;
import java.util.Collections;

public class TrueOrFalse extends TypeNode {

    public TrueOrFalse(String directions, String prompt) {
        super(directions, prompt);

        //Establish options
        RadioButton radio1 = new RadioButton("true");
        RadioButton radio2 = new RadioButton("false");

        //Create toggleGroup to make only one button at a time selectable
        ToggleGroup buttons = new ToggleGroup();

        //Iterate through buttons and init functionality
        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);
            radioButton.setOnMouseClicked(e -> this.response = Collections.singletonList(radioButton.getText()));
        }

        this.node.getChildren().addAll(radio1, radio2);
    }

}
