package questions.nodes;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

public class CheckBoxNode extends TypeNode {

    public CheckBoxNode(List<String> options) {
        //Get box amount from options size
        int boxAmount = options.size();

        //Create an array of checkboxes
        CheckBox[] boxes = new CheckBox[boxAmount];

        //Iterate through the array and init each button
        for (int i = 0; i < boxAmount; i++) {

            CheckBox checkBox = new CheckBox(options.get(i));

            boxes[i] = checkBox;

            //On mouse clicked, record response to responses
            checkBox.setOnMouseClicked(e -> {

                //Make a new linked list everytime so we don't need to track removing.
                LinkedList<String> list = new LinkedList<>();
                for (CheckBox box : boxes) {
                    if (box.isSelected())
                        list.add(box.getText());
                }
                this.response = list;
            });
        }

        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(boxes);

        this.node = vbox;
    }

}
