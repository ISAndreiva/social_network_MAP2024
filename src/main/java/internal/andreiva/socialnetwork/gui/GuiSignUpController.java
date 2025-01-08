package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class GuiSignUpController extends GuiController
{
    @FXML
    TextField usernameTextField;

    @FXML
    TextField firstNameTextField;

    @FXML
    TextField lastNameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    PasswordField passwordTextField;

    public void handleSignUp()
    {
        try
        {
            service.addUser(firstNameTextField.getText(), lastNameTextField.getText(), usernameTextField.getText(), emailTextField.getText(), PasswordHasher.hashPassword(passwordTextField.getText(), usernameTextField.getText()));
            Gui.loggedUserView(usernameTextField.getText());
        } catch (Exception e)
        {
            Gui.errorView(e);
        }
    }

    public void handleBack()
    {
        Gui.loginView();
    }
}
