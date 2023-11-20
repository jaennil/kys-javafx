package Other;

import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

public class UnmaskedPasswordField extends PasswordField {

    private boolean maskDisabled = false;
    private String actualText = "";

    public UnmaskedPasswordField() {
        super();
    }

//    public void toggleMask() {
//        System.out.println("toggle mask");
//        maskDisabled = !maskDisabled;
//        System.out.println(maskDisabled);
//        if (maskDisabled) {
//            System.out.println(getText());
//            setText(getText()); // Refresh text to show actual characters
//        } else {
//            System.out.println(getText());
//            super.setText(getText()); // Use the default masked text
//        }
//    }
//public void toggleMask() {
//    System.out.println("toggle mask");
//    maskDisabled = !maskDisabled;
//    System.out.println(maskDisabled);
//    if (maskDisabled) {
//        System.out.println(getText());
//        setManaged(false);
//        setVisible(false);
//        setManaged(true);
//        setVisible(true);
//    } else {
//        System.out.println(getText());
//        super.setText(getText());
//    }

//    public void toggleMask() {
//        System.out.println("toggle mask");
//        maskDisabled = !maskDisabled;
//        System.out.println(maskDisabled);
//        if (maskDisabled) {
//            actualText = getText();
//            setText("");
//        } else {
//            setText(actualText);
//        }
//    }

    public boolean isMaskDisabled() {
        return maskDisabled;
    }
}
