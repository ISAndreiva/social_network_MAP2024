package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Optional;

public class GuiUpdateUserController extends GuiController
{
    @FXML
    TextField firstNameTextField;

    @FXML
    TextField lastNameTextField;

    @FXML
    TextField emailTextField;

    private User user;

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        user = (User) parameter.get();
    }

    @Override
    public void setService(Service service)
    {
        super.setService(service);
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        emailTextField.setText(user.getEmail());
    }

    public void handleUpdate()
    {
        try
        {
            service.updateUser(firstNameTextField.getText(), lastNameTextField.getText(), user.getUsername(), emailTextField.getText());
        } catch (Exception e)
        {
            Gui.errorView(e);
        }
    }
}
