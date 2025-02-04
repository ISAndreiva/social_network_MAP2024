package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Event;
import internal.andreiva.socialnetwork.utils.EventType;
import internal.andreiva.socialnetwork.utils.Observer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;

public class GuiFriendRequestsController extends GuiController<User> implements Observer
{
    private User user;

    private static final ObservableList<User> requestsTableData = FXCollections.observableArrayList();

    @FXML
    TableView<User> requestsTable;

    @FXML
    TableColumn<User, String> requestsUsernameColumn;

    @FXML
    TableColumn<User, String> requestsNameColumn;

    @FXML
    TableColumn<User, String> requestsSinceColumn;

    @Override
    public void update(Event event)
    {
        if (event.getType().equals(EventType.RELATIONSHIP))
            populateTables();
    }

    private class FriendshipSinceFactory implements Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>
    {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            User user = param.getValue();
            return new SimpleStringProperty(service.getFriendship(GuiFriendRequestsController.this.user.getUsername(), user.getUsername()).getFriendSince().format(formatter));
        }
    }

    @FXML
    public void initialize()
    {
        requestsUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        requestsNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        requestsSinceColumn.setCellValueFactory(new GuiFriendRequestsController.FriendshipSinceFactory());
        requestsTable.setItems(requestsTableData);

        Platform.runLater(() -> {
            Stage stage = (Stage) requestsTable.getScene().getWindow();
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
        requestsTableData.setAll(service.getReceivedFriendRequests(user.getUsername()));
    }

    @Override
    public void setSomething(User parameter)
    {
        user = parameter;
    }

    public void handleAcceptFriendship()
    {
        User friend = requestsTable.getSelectionModel().getSelectedItem();
        if (friend != null)
        {
            try
            {
                service.respondToFriendship(user.getUsername(), friend.getUsername(), "accepted");
            } catch (Exception e)
            {
                Gui.errorView(e);
            }
        }
    }

    public void handleRejectFriendship()
    {
        User friend = requestsTable.getSelectionModel().getSelectedItem();
        if (friend != null)
        {
            try
            {
                service.respondToFriendship(user.getUsername(), friend.getUsername(), "rejected");
            } catch (Exception e)
            {
                Gui.errorView(e);
            }
        }
    }
}
