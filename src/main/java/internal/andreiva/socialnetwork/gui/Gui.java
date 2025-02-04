package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

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
        stage.setScene(createScene("socialnetwork/gui/login.fxml", "socialnetwork/gui/css/login.css", null));
        stage.setTitle("NameTODO Social Network");
        stage.show();
    }

    public static void loggedUserView(String username)
    {
        stage.setScene(createScene("socialnetwork/gui/user.fxml", "socialnetwork/gui/css/user.css", service.getUser(username)));
        stage.setTitle("NameTODO Social Network - " + username);
        stage.show();
    }

    private static Scene createScene(String fxml, String css, Object parameter)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getClassLoader().getResource(fxml));
        try
        {
            AnchorPane root = fxmlLoader.load();
            GuiController<Object> controller = fxmlLoader.getController();
            controller.setSomething(parameter);
            controller.setService(service);
            double scaleValue = 1.25;
            Scale scale = new Scale(scaleValue, scaleValue);
            root.getTransforms().add(scale);
            root.setPrefHeight(root.getPrefHeight()*scaleValue);
            root.setPrefWidth(root.getPrefWidth()*scaleValue);
            Scene scene = new Scene(root);
            if (css != null)
            {
                scene.getStylesheets().add(Gui.class.getClassLoader().getResource(css).toExternalForm());
            }
            return scene;
        }
        catch (IOException e)
        {
            throw new GuiException(e);
        }
    }

    public static void profileSearchView(String username)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/profileSearch.fxml", "socialnetwork/gui/css/login.css", username));
        stage.setTitle("Look for a user profile");
        stage.show();
    }

    public static void updateUserView(User user)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/userUpdate.fxml", "socialnetwork/gui/css/login.css", user));
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
        stage.setScene(createScene("socialnetwork/gui/signUp.fxml", "socialnetwork/gui/css/login.css", null));
        stage.setTitle("NameTODO Social Network - Sign Up");
        stage.show();
    }

    public static void friendRequestsView(User user)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/friendRequests.fxml", "socialnetwork/gui/css/friendRequests.css", user));
        stage.setTitle("NameTODO Social Network - Friend Requests");
        stage.show();
    }

    public static void chatView(Pair<User, User> users)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/chat.fxml", "socialnetwork/gui/css/chat.css", users));
        stage.setTitle("NameTODO Social Network - Chat");
        stage.show();
    }

    public static void profileView(Pair<User, User> user)
    {
        Stage stage = new Stage();
        stage.setScene(createScene("socialnetwork/gui/profile.fxml", "socialnetwork/gui/css/profile.css", user));
        stage.setTitle("NameTODO Social Network - " + user.getValue().getUsername() + "'s Profile");
        stage.show();
    }

    public static void launch()
    {
        Application.launch();
    }

}
