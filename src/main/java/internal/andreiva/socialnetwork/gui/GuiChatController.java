package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;

public class GuiChatController extends GuiController implements Observer
{

    Pair<User, User> users;
    Conversation conversation;

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextArea messageTextArea;

    VBox vbox = new VBox();

    @FXML
    public void initialize()
    {
        vbox.setSpacing(2);
        vbox.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setContent(vbox);

        Platform.runLater(() -> {
            Stage stage = (Stage) scrollPane.getScene().getWindow();
            stage.setOnCloseRequest(event -> service.removeObserver(this));
        });
    }

    @Override
    public void update()
    {
        showMessages();
    }

    @Override
    public void setService(Service service)
    {
        super.setService(service);
        service.addObserver(this);
        Platform.runLater(this::showMessages);
    }

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        users = (Pair<User, User>) parameter.get();
    }

    public void showMessages()
    {
        vbox.getChildren().clear();
        conversation = service.getOrCreateConversation(users.getKey().getUsername(), users.getValue().getUsername());
        conversation.getMessages().forEach(message ->
        {
            boolean isSender = message.getSender().equals(service.getUser(users.getKey().getUsername()).getId());
            HBox bubble = createBubble(message.getText(), isSender);
            vbox.getChildren().add(bubble);
        });
        scrollPane.setContent(vbox);
    }

    public void handleSend()
    {
        if (!messageTextArea.getText().isEmpty())
        {
            service.sendMessage(conversation.getId(), users.getKey().getUsername(), messageTextArea.getText());
            messageTextArea.clear();
        }
    }

    private HBox createBubble(String message_content, boolean isSender)
    {
        Label label = new Label(message_content);
        if (isSender)
            label.setStyle("-fx-background-color: #448ed8");
        else
            label.setStyle("-fx-background-color: #74d410");

        label.getStyleClass().add("chat_label");
        label.setMaxWidth(scrollPane.getScene().getWindow().getWidth() / 2 - 20);

        HBox hbox = new HBox();

        if (isSender)
            hbox.setAlignment(Pos.CENTER_RIGHT);
        else
            hbox.setAlignment(Pos.CENTER_LEFT);

        hbox.setMaxWidth(Double.MAX_VALUE);
        hbox.getChildren().add(label);
        return hbox;
    }
}