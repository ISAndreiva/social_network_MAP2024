package internal.andreiva.socialnetwork.gui;

import javafx.fxml.FXML;
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

    public void handleSignUp()
    {
        try
        {
            service.addUser(firstNameTextField.getText(), lastNameTextField.getText(), usernameTextField.getText(), emailTextField.getText());
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
