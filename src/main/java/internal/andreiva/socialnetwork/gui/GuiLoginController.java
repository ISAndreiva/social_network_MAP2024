package internal.andreiva.socialnetwork.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class GuiLoginController extends GuiController
{
    @FXML
    TextField usernameTextField;

    @FXML
    Label errorLabel;

    public void handleLogin(ActionEvent actionEvent)
    {
        if (!usernameTextField.getText().isEmpty())
        {
            if (service.userExists(usernameTextField.getText()))
            {
                Gui.loggedUserView(usernameTextField.getText());
            }
            else
            {
                errorLabel.setText("Username does not exist!");
                errorLabel.setStyle("-fx-text-fill: red");
            }
        }
    }
}
