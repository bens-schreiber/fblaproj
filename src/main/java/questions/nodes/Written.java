package questions.nodes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Collections;

public class Written extends TypeNode {
    {

        TextField textField = new TextField();
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        //Set functionality
        textField.setOnKeyTyped(e -> this.response = Collections.singletonList(textField.getText()));

        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(label, textField);

        this.node = vbox;

    }
}
