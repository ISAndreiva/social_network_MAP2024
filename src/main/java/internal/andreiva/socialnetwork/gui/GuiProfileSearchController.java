package internal.andreiva.socialnetwork.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Pair;


public class GuiProfileSearchController extends GuiController<String>
{
    private String username;

    @FXML
    TextField usernameTextField;

    @FXML
    Label resultLabel;

    @Override
    public void setSomething(String parameter)
    {
        username = parameter;
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
