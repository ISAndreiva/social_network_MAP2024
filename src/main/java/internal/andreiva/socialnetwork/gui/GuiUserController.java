package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Event;
import internal.andreiva.socialnetwork.utils.EventType;
import internal.andreiva.socialnetwork.utils.Observer;
import internal.andreiva.socialnetwork.utils.Pageable;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


public class GuiUserController extends GuiController implements Observer
{
    private User user;
    private final int pageSize = 2;


    @FXML
    Pagination friendsPagination;

    @FXML
    Button friendshipsRequestsButton;

    @Override
    public void update(Event event)
    {
        if (event.getType().equals(EventType.RELATIONSHIP) || event.getType().equals(EventType.USER))
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
        Platform.runLater(() -> {
            Stage stage = (Stage) friendshipsRequestsButton.getScene().getWindow();
            stage.setOnCloseRequest(event -> service.removeObserver(this));
        });
    }


    private Node createPage(int pageIndex)
    {
        TableView<User> table = new TableView<>();
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        TableColumn<User, String> sinceColumn = new TableColumn<>("Friends since");
        TableColumn<User, Void> deleteColumn = new TableColumn<>("Delete");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        sinceColumn.setCellValueFactory(new FriendshipSinceFactory());
        deleteColumn.setCellFactory(new DeleteButtonFactory());
        deleteColumn.setStyle("-fx-alignment: CENTER;");

        var page = service.getFriendships(user.getUsername(), "accepted", new Pageable(pageIndex, pageSize));
        var friends = StreamSupport.stream(page.getElementsOnPage().spliterator(), false).toList();
        table.getColumns().setAll(List.of(usernameColumn, nameColumn, sinceColumn, deleteColumn));
        table.setItems(FXCollections.observableArrayList(friends));

        table.setRowFactory( tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    User rowData = row.getItem();
                    Gui.chatView(new Pair<>(user, rowData));
                }
            });
            return row ;
        });

        return table;
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
        int totalNumberOfElements = service.getFriendships(user.getUsername(), "accepted", new Pageable(0, pageSize)).getTotalNumberOfElements();


        int totalNumberOfPages = totalNumberOfElements / pageSize;
        if(totalNumberOfElements % pageSize != 0)
            totalNumberOfPages++;

        if (totalNumberOfPages == 0)
            totalNumberOfPages = 1;
        var currentPage = friendsPagination.getCurrentPageIndex();
        friendsPagination.setPageCount(totalNumberOfPages);
        friendsPagination.setPageFactory(this::createPage);
        friendsPagination.setCurrentPageIndex(currentPage);


        friendshipsRequestsButton.setVisible(!service.getReceivedFriendRequests(user.getUsername()).isEmpty());
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
