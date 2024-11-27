package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.Message;
import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;
import java.util.UUID;

public class GuiChatController extends GuiController implements Observer
{

    Pair<User, User> users;
    Conversation conversation;

    @FXML
    ScrollPane scrollPane;

    @FXML
    TextArea messageTextArea;

    UUID replyTo = null;

    VBox vbox = new VBox();

    @FXML
    public void initialize()
    {
        vbox.setSpacing(2);
        vbox.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setContent(vbox);
        vbox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));

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
            Message replyMessage = service.getMessage(message.getReplyTo());


            Button replyButton = createReplyButton(message.getId());
            if (message.getId() == replyTo)
                replyButton.setVisible(true);

            HBox bubble;
            if (replyMessage == null)
                bubble = createBubble(message.getText(), isSender, false, null, replyButton);
            else
            {
                boolean isReplySender = replyMessage.getSender().equals(service.getUser(users.getKey().getUsername()).getId());
                bubble = createBubble(message.getText(), isSender, isReplySender, replyMessage.getText(), replyButton);
            }

            vbox.getChildren().add(bubble);
        });
    }

    public void handleSend()
    {
        if (!messageTextArea.getText().isEmpty())
        {
            service.sendMessage(conversation.getId(), users.getKey().getUsername(), messageTextArea.getText(), replyTo);
            messageTextArea.clear();
            replyTo = null;
        }
    }

    private Button createReplyButton(UUID replyToID)
    {
        Button replyButton = new Button();
        replyButton.getStyleClass().add("reply_button");

        replyButton.setOnAction(event -> {
            if (replyTo == replyToID)
                replyTo = null;
            else
                replyTo = replyToID;
            update();
        });

        replyButton.setVisible(false);

        return replyButton;
    }

    private HBox createBubble(String message_content, boolean isSender, boolean isReplySender, String replyMessage, Button replyButton)
    {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("chat_bubble");
        HBox hbox = new HBox();
        Label label = new Label(message_content);
        if (isSender)
            vbox.setStyle("-fx-background-color: #2985e4");
        else
            vbox.setStyle("-fx-background-color: #77de0e");

        label.getStyleClass().add("chat_label");
        label.setMaxWidth(scrollPane.getScene().getWindow().getWidth() / 2 - 20);

        if (isSender)
            hbox.setAlignment(Pos.CENTER_RIGHT);
        else
            hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setMaxWidth(Double.MAX_VALUE);

        if (replyMessage != null)
        {
            Label replyLabel = new Label(replyMessage);
            replyLabel.getStyleClass().add("chat_label");
            HBox replyBox = new HBox();
            replyBox.getChildren().add(replyLabel);
            replyBox.getStyleClass().add("chat_bubble");

            if (isReplySender)
                replyBox.setStyle("-fx-background-color: rgb(107,175,242)");
            else
                replyBox.setStyle("-fx-background-color: rgb(157,248,76)");

            vbox.getChildren().add(replyBox);
        }

        vbox.getChildren().add(label);
        if (isSender)
        {
            hbox.getChildren().add(replyButton);
            hbox.getChildren().add(vbox);
            if (!replyButton.isVisible())
            {
                hbox.setOnMouseEntered(event -> hbox.getChildren().getFirst().setVisible(true));
                hbox.setOnMouseExited(event -> hbox.getChildren().getFirst().setVisible(false));
            }
        }
        else
        {
            hbox.getChildren().add(vbox);
            hbox.getChildren().add(replyButton);
            if (!replyButton.isVisible())
            {
                hbox.setOnMouseEntered(event -> hbox.getChildren().getLast().setVisible(true));
                hbox.setOnMouseExited(event -> hbox.getChildren().getLast().setVisible(false));
            }
        }

        return hbox;
    }
}
