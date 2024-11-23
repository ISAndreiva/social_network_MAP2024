package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Observer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class GuiUserController extends GuiController implements Observer
{
    private User user;

    @FXML
    TableView<User> friendsTable;

    private static final ObservableList<User> friendsTableData = FXCollections.observableArrayList();

    @FXML
    TableColumn<User, String> friendsUsernameColumn;

    @FXML
    TableColumn<User, String> friendsNameColumn;

    @FXML
    TableColumn<User, String> friendsSinceColumn;

    @FXML
    TableColumn<User, Void> friendsDeleteColumn;

    @Override
    public void update()
    {
        populateTables();
    }

    private class FriendshipSinceFactory implements Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>> {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            User user = param.getValue();
            return new SimpleStringProperty(service.getFriendship(GuiUserController.this.user.getUsername(), user.getUsername()).getFriendSince().format(formatter));
        }
    }

    private class DeleteButtonFactory implements Callback<TableColumn<User, Void>, TableCell<User, Void>> {
        @Override
        public TableCell<User, Void> call(TableColumn<User, Void> param) {
            return new TableCell<>() {
                private final Button deleteButton = new Button("Delete");
                {
                    deleteButton.setOnAction(event -> {
                        User user = getTableView().getItems().get(getIndex());
                        if (user != null)
                            try {
                                service.deleteFriendship(GuiUserController.this.user.getUsername(), user.getUsername());
                            } catch (Exception e) {
                                Gui.errorView(e);
                            }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty)
                {
                    super.updateItem(item, empty);
                    if (empty)
                        setGraphic(null);
                    else
                        setGraphic(deleteButton);
                }
            };

        }
    }

    @FXML
    public void initialize()
    {
        friendsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        friendsNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        friendsSinceColumn.setCellValueFactory(new FriendshipSinceFactory());
        friendsDeleteColumn.setCellFactory(new DeleteButtonFactory());
        friendsDeleteColumn.setStyle("-fx-alignment: CENTER;");

        friendsTable.setItems(friendsTableData);

        Platform.runLater(() -> {
            Stage stage = (Stage) friendsTable.getScene().getWindow();
            stage.setOnCloseRequest(event -> service.removeObserver(this));
        });
    }

    @Override
    public void setService(Service service)
    {
        super.setService(service);
        service.addObserver(this);
        populateTables();
    }

    public void populateTables()
    {
        friendsTableData.setAll(service.getFriendships(user.getUsername(), "accepted"));
    }

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        user = (User) parameter.get();
    }

    public void handleLogout()
    {
        service.removeObserver(this);
        Gui.loginView();
    }

    public void handleAddFriendship()
    {
        Gui.friendRequestView(user.getUsername());
    }

    public void handleUpdateUser()
    {
        Gui.updateUserView(user);
    }

    public void handleFriendRequests()
    {
        Gui.friendRequestsView(user);
    }

}
