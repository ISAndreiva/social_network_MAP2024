package internal.andreiva.socialnetwork.gui;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.utils.Event;
import internal.andreiva.socialnetwork.utils.EventType;
import internal.andreiva.socialnetwork.utils.Observer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;

public class GuiProfileController extends GuiController implements Observer
{
    //pair of origin user and destination user
    private Pair<User, User> user;

    @FXML
    Button messageButton;

    @FXML
    Button friendRequestButton;

    @FXML
    Button updateProfileButton;

    @FXML
    Label usernameLabel;

    @FXML
    Label nameLabel;

    @FXML
    Label friendCountLabel;

    @FXML
    ImageView profileImageView;

    @Override
    public void setSomething(Optional<Object> parameter)
    {
        user = (Pair<User, User>) parameter.get();
        if (user.getKey().equals(user.getValue()))
        {
            messageButton.setVisible(false);
            friendRequestButton.setVisible(false);
            updateProfileButton.setVisible(true);
        }
        else
        {
            messageButton.setVisible(true);
            friendRequestButton.setVisible(true);
            updateProfileButton.setVisible(false);
        }
        usernameLabel.setText(user.getValue().getUsername());
        nameLabel.setText(user.getValue().getFullName());
    }

    @Override
    public void setService(Service service)
    {
        super.setService(service);
        service.addObserver(this);
        updateFriendsAndButtons();
        updateImage();
    }

    @Override
    public void update(Event event)
    {
        if (event.getType().equals(EventType.RELATIONSHIP))
        {
            updateFriendsAndButtons();
        }
        if (event.getType().equals(EventType.PROFILE_PICTURE))
        {
            updateImage();
        }
    }

    private void updateFriendsAndButtons()
    {
        friendCountLabel.setText(service.getFriendships(user.getValue().getUsername(), "accepted").size() + " friends");
        var friendship = service.getFriendship(user.getKey().getUsername(), user.getValue().getUsername());
        if (friendship != null)
        {
            friendRequestButton.setDisable(true);
            messageButton.setDisable(!Objects.equals(friendship.getStatus(), "accepted"));
        }
        else
        {
            friendRequestButton.setDisable(false);
            messageButton.setDisable(true);
        }
    }

    private void updateImage()
    {
        var imageBytes = service.getImage(user.getValue());
        if (imageBytes != null)
            profileImageView.setImage(inputStreamToImage(imageBytes));
        else
            profileImageView.setImage(new Image("socialnetwork/gui/images/default.jpg"));
    }

    private Image inputStreamToImage(InputStream inputStream)
    {
        var width = 140;
        var height = 140;
        var image = new WritableImage(width, height);
        try
        {
            image.getPixelWriter().setPixels(0, 0, width, height,
                    PixelFormat.getByteBgraInstance(),
                    inputStream.readAllBytes(), 0, width * 4);
        } catch (IOException e)
        {
            return new Image("socialnetwork/gui/images/default.jpg");
        }
        return image;
    }

    public void handleUpdateProfile()
    {
        Gui.updateUserView(user.getKey());
    }

    public void handleFriendRequest()
    {
        service.addFriendship(user.getKey().getUsername(), user.getValue().getUsername());
    }

    public void handleMessage()
    {
        Gui.chatView(new Pair<>(user.getKey(), user.getValue()));
    }
}
