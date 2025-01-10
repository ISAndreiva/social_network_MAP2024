package internal.andreiva.socialnetwork.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;

import java.util.Optional;

public class GuiProfileSearchController extends GuiController
{
    private String username;

    @FXML
    TextField usernameTextField;

    @FXML
    Label resultLabel;

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        username = (String) parameter.get();
    }

    public void handleAdd()
    {
        if (!usernameTextField.getText().isEmpty())
        {
            if (service.userExists(usernameTextField.getText()))
            {
                try
                {
                    Gui.profileView(new Pair<>(service.getUser(username), service.getUser(usernameTextField.getText())));
                }
                catch (Exception e)
                {
                    Gui.errorView(e);
                }
            }
            else
            {
                resultLabel.setText("User does not exist!");
                resultLabel.setStyle("-fx-text-fill: red");
            }
        }
    }

}
