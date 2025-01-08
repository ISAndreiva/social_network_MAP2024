package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.utils.PasswordHasher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class GuiLoginController extends GuiController
{
    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    Label errorLabel;

    public void handleLogin(ActionEvent actionEvent)
    {
        if (!usernameTextField.getText().isEmpty())
        {
            if (service.userExists(usernameTextField.getText()))
            {
                if (service.checkPassword(usernameTextField.getText(), PasswordHasher.hashPassword(passwordTextField.getText(), usernameTextField.getText())))
                    Gui.loggedUserView(usernameTextField.getText());
                else
                {
                    errorLabel.setText("Incorrect password!");
                    errorLabel.setStyle("-fx-text-fill: red");
                }
            }
            else
            {
                errorLabel.setText("Username does not exist!");
                errorLabel.setStyle("-fx-text-fill: red");
            }
        }
    }

    public void handleSignUp(ActionEvent actionEvent)
    {
        Gui.signUpView();
    }
}
