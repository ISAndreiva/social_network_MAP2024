package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.User;

import java.util.List;
import java.util.UUID;

public class Service
{
    FriendshipService friendshipService;
    UserService userService;

    public Service(String userFilename, String friendshipFilename)
    {
        friendshipService = ServiceFactory.getInstance().getFriendshipService(friendshipFilename);
        userService = ServiceFactory.getInstance().getUserService(userFilename);
    }

    public void addUser(String firstName, String lastName, String username, String password)
    {
        userService.addUser(firstName, lastName, username, password);
    }

    public void deleteUser(String username)
    {
        User user = userService.deleteUser(username);
        friendshipService.deleteFriendships(user.getId());
    }

    public void updateUser(String firstName, String lastName, String username, String password)
    {
        userService.updateUser(firstName, lastName, username, password);
    }

    public String[] getUsers()
    {
        return userService.getUsers();
    }

    public void addFriendship(String username1, String username2)
    {
        friendshipService.addFriendship(userService.checkUserExists(username1), userService.checkUserExists(username2));
    }

    public void deleteFriendship(String username1, String username2)
    {
        friendshipService.deleteFriendship(userService.checkUserExists(username1), userService.checkUserExists(username2));
    }

    public String[] getFriends(String username)
    {
        List<UUID> friendships = friendshipService.getFriends(userService.checkUserExists(username));
        String[] friends = new String[friendships.size()];
        for (int i = 0; i < friendships.size(); i++)
        {
            friends[i] = userService.getUser(friendships.get(i)).toString();
        }
        return friends;
    }

}
