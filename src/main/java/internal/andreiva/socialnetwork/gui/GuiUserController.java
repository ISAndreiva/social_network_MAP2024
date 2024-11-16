package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class GuiUserController extends GuiController
{
    private User user;

    @FXML
    TableView<User> friendsTable;

    ObservableList<User> friendsTableData = FXCollections.observableArrayList();

    @FXML
    TableView<User> requestsTable;

    ObservableList<User> requestsTableData = FXCollections.observableArrayList();

    @FXML
    TableColumn<User, String> friendsUsernameColumn;

    @FXML
    TableColumn<User, String> friendsNameColumn;

    @FXML
    TableColumn<User, String> friendsSinceColumn;

    @FXML
    TableColumn<User, String> requestsUsernameColumn;

    @FXML
    TableColumn<User, String> requestsNameColumn;

    @FXML
    TableColumn<User, String> requestsSinceColumn;

    private class FriendshipSinceFactory implements Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>> {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            User user = param.getValue();
            return new SimpleStringProperty(service.getFriendship(GuiUserController.this.user.getUsername(), user.getUsername()).getFriendSince().format(formatter));
        }
    }

    @FXML
    public void initialize()
    {
        friendsUsernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        friendsSinceColumn.setCellValueFactory(new FriendshipSinceFactory());


        requestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        requestsNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("fullName"));
        requestsSinceColumn.setCellValueFactory(new FriendshipSinceFactory());
        friendsTable.setItems(friendsTableData);
        requestsTable.setItems(requestsTableData);
    }

    @Override
    public void setService(Service service)
    {
        super.setService(service);
        populateTables();
    }

    public void populateTables()
    {
        friendsTableData.setAll(service.getFriendships(user.getUsername(), "accepted"));
        requestsTableData.setAll(service.getFriendships(user.getUsername(), "pending"));
    }

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        user = (User) parameter.get();
    }

    public void handleLogout()
    {
        Gui.initLoginView();
    }

    public void handleDeleteFriendship()
    {
        User friend = friendsTable.getSelectionModel().getSelectedItem();
        if (friend != null)
        {
            service.deleteFriendship(user.getUsername(), friend.getUsername());
            populateTables();
        }
    }

    public void handleAcceptFriendship()
    {
        User friend = requestsTable.getSelectionModel().getSelectedItem();
        if (friend != null)
        {
            service.respondToFriendship(user.getUsername(), friend.getUsername(), "accepted");
            populateTables();
        }
    }

    public void handleRejectFriendship()
    {
        User friend = requestsTable.getSelectionModel().getSelectedItem();
        if (friend != null)
        {
            service.respondToFriendship(user.getUsername(), friend.getUsername(), "rejected");
            populateTables();
        }
    }

    public void handleAddFriendship()
    {
        Gui.friendRequestView(user.getUsername());
    }

    public void handleUpdateUser()
    {
        Gui.updateUserView(user);
    }

}
