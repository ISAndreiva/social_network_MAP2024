package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.PasswordHasher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.util.Optional;

public class GuiUpdateUserController extends GuiController
{
    @FXML
    TextField firstNameTextField;

    @FXML
    TextField lastNameTextField;

    @FXML
    TextField emailTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    Button profilePictureUpdateButton;

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
            service.updateUser(firstNameTextField.getText(), lastNameTextField.getText(), user.getUsername(), emailTextField.getText(), PasswordHasher.hashPassword(passwordTextField.getText(), user.getUsername()));
        } catch (Exception e)
        {
            Gui.errorView(e);
        }
    }

    public void handleUpdateProfilePicture()
    {
        FileChooser fil_chooser = new FileChooser();
        var file = fil_chooser.showOpenDialog(profilePictureUpdateButton.getScene().getWindow());
        try
        {
            Image img = new Image(file.toURI().toString(), 140, 140, false, false);
            service.saveImage(user, img);
        } catch (Exception e)
        {
            Gui.errorView(e);
        }
    }
}
