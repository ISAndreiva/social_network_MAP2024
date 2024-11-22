package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Gui extends Application
{
    private static Service service;
    private static Stage stage;

    public static void setService(Service service)
    {
        Gui.service = service;
    }

    @Override
    public void start(Stage stage)
    {
        Gui.stage = stage;
        loginView();
        stage.setResizable(false);
        stage.show();
    }

    public static void loginView()
    {
        stage.setScene(createScene("socialnetwork/gui/login.fxml", Optional.empty()));
        stage.setTitle("NameTODO Social Network");
        stage.show();
    }

    public static void loggedUserView(String username)
    {
        stage.setScene(createScene("socialnetwork/gui/user.fxml", Optional.of(service.getUser(username))));
        stage.setTitle("NameTODO Social Network - " + username);
        stage.show();
    }

    private static Scene createScene(String fxml, Optional<Object> parameter)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(fxml));
        try
        {
            AnchorPane root = fxmlLoader.load();
            GuiController controller = fxmlLoader.getController();
            controller.setSomething(parameter);
            controller.setService(service);
            return new Scene(root);
        }
        catch (IOException e)
        {
            throw new GuiException(e);
        }
    }

    public static void friendRequestView(String username)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/friendRequest.fxml", Optional.of(username)));
        stage.setTitle("Send a friend Request");
        stage.show();
    }

    public static void updateUserView(User user)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/userUpdate.fxml", Optional.of(user)));
        stage.setTitle("Update User");
        stage.show();
    }

    public static void errorView(Exception exception)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, exception.getMessage());
        alert.setTitle("Error");
        alert.showAndWait();
    }

    public static void signUpView()
    {
        stage.setScene(createScene("socialnetwork/gui/signUp.fxml", Optional.empty()));
        stage.setTitle("NameTODO Social Network - Sign Up");
        stage.show();
    }

    public static void friendRequestsView(User user)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/friendRequests.fxml", Optional.of(user)));
        stage.setTitle("NameTODO Social Network - Friend Requests");
        stage.show();
    }

    public static void launch()
    {
        Application.launch();
    }

}
