package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.Message;
import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.utils.*;
import internal.andreiva.socialnetwork.utils.Observable;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.util.*;


/**
 * Service class that provides methods for managing users and friendships
 */
public class Service extends Observable
{
    private final FriendshipController friendshipController;
    private final UserController userController;
    private final ConversationController conversationController;

    /**
     * Constructor
     * @param db_connection the connection to the database
     */
    public Service(Connection db_connection)
    {
        friendshipController = ControllerFactory.getInstance().getFriendshipService(db_connection);
        userController = ControllerFactory.getInstance().getUserService(db_connection);
        conversationController = ControllerFactory.getInstance().getConversationService(db_connection);
    }

    /**
     * Add a user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param username the username of the user
     * @param email  the email of the user
     */
    public void addUser(String firstName, String lastName, String username, String email, String passwordHash)
    {
        userController.addUser(firstName, lastName, username, email, passwordHash);
        notifyObservers(new Event(EventType.USER));
    }

    /**
     * Delete a user
     * @param username the username of the user
     */
    public void deleteUser(String username)
    {
        User user = userController.deleteUser(username);
        friendshipController.deleteFriendships(user.getId());
        notifyObservers(new Event(EventType.USER));
    }

    /**
     * Update a user
     * @param firstName the new first name of the user
     * @param lastName the new last name of the user
     * @param username the username of the existing user
     * @param email the new email of the user
     */
    public void updateUser(String firstName, String lastName, String username, String email, String passwordHash)
    {
        userController.updateUser(firstName, lastName, username, email, passwordHash);
        notifyObservers(new Event(EventType.USER));
    }

    /**
     * Returns a list of all users
     * @return a list of all users
     */
    public List<String> getUsers()
    {
        return userController.getUsers();
    }

    /**
     * Add a friendship between two users
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     */
    public void addFriendship(String username1, String username2)
    {
        friendshipController.addFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
        notifyObservers(new Event(EventType.RELATIONSHIP));
    }

    /**
     * Delete a friendship between two users
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     */
    public void deleteFriendship(String username1, String username2)
    {
        friendshipController.deleteFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
        var conv = conversationController.getConversationBetweenUsers(userController.checkUserExists(username1), userController.checkUserExists(username2));
        conv.ifPresent(conversation -> conversationController.deleteConversation(conversation.getId()));
        notifyObservers(new Event(EventType.RELATIONSHIP));
    }

    /**
     * Returns a list of friends of a user
     * @param username the username of the user
     * @return a list of friends of the user
     */
    public List<User> getFriendships(String username, String status)
    {
        List<User> friends = new ArrayList<>();
        friendshipController.getFriendships(userController.checkUserExists(username), status).forEach(f -> friends.add(userController.getUser(f)));
        return friends;
    }

    public Page<User> getFriendships(String username, String status, Pageable pageable)
    {
        List<User> friends = new ArrayList<>();
        var page = friendshipController.getFriendshipsOnPage(userController.checkUserExists(username), status, pageable);
        page.getElementsOnPage().forEach(f -> friends.add(userController.getUser(f)));
        return new Page<>(friends, page.getTotalNumberOfElements());
    }

    public List<User> getReceivedFriendRequests(String username)
    {
        List<User> friends = new ArrayList<>();
        friendshipController.getReceivedFriendRequests(userController.checkUserExists(username)).forEach(f -> friends.add(userController.getUser(f)));
        return friends;
    }

    public List<User> getReceivedFriendRequests(String username, Pageable pageable)
    {
        List<User> friends = new ArrayList<>();
        var page = friendshipController.getReceivedFriendRequestsOnPage(userController.checkUserExists(username), pageable);
        page.getElementsOnPage().forEach(f -> friends.add(userController.getUser(f)));
        return friends;
    }

    /**
     * Helper function for DFS
     * @param adj the adjacency list
     * @param u the current node
     * @param visited the visited nodes
     * @return the length of the connected component
     */
    private int DFS_visit(Map<UUID, List<UUID>> adj, UUID u, Map<UUID, Boolean> visited)
    {
        visited.put(u, true);
        int length = 1;
        for (UUID v: adj.get(u))
        {
            if (!visited.get(v))
            {
                length += DFS_visit(adj, v, visited);
            }
        }
        return length;
    }

    /**
     * Returns the number of communities
     * @return the number of communities
     */
    public int no_communities()
    {
        Map<UUID, List<UUID>> adj = new HashMap<>();
        userController.getUsersIterable().forEach(u -> adj.put(u.getId(), new ArrayList<>()));
        friendshipController.getFriendshipsIterable().forEach(f -> {
            if (f.getStatus().equals("accepted"))
            {
                adj.get(f.getFriend1()).add(f.getFriend2());
                adj.get(f.getFriend2()).add(f.getFriend1());
            }
        });

        Map<UUID, Boolean> visited = new HashMap<>();
        userController.getUsersIterable().forEach(u -> visited.put(u.getId(), false));
        int no = 0;
        for (User u: userController.getUsersIterable())
        {
            if (!visited.get(u.getId()))
            {
                DFS_visit(adj, u.getId(), visited);
                no++;
            }

        }
        return no;
    }

    /** Returns the members of the biggest community
     * @return list of the members of the biggest community
     */
    public List<String> biggest_community()
    {
        Map<UUID, List<UUID>> adj = new HashMap<>();
        userController.getUsersIterable().forEach(u -> adj.put(u.getId(), new ArrayList<>()));
        friendshipController.getFriendshipsIterable().forEach(f -> {
            if (f.getStatus().equals("accepted"))
            {
                adj.get(f.getFriend1()).add(f.getFriend2());
                adj.get(f.getFriend2()).add(f.getFriend1());
            }
        });

        Map<UUID, Boolean> visited = new HashMap<>();
        userController.getUsersIterable().forEach(u -> visited.put(u.getId(), false));
        int max = 0; UUID maxNode = null;
        for (User u: userController.getUsersIterable())
        {
            if (!visited.get(u.getId()))
            {
                int length = DFS_visit(adj, u.getId(), visited);
                if (length > max)
                {
                    max = length;
                    maxNode = u.getId();
                }
            }
        }
        List<String> community = new ArrayList<>();
        userController.getUsersIterable().forEach(u -> visited.put(u.getId(), false));
        Queue<UUID> q = new LinkedList<>();
        q.add(maxNode);
        visited.put(maxNode, true);
        while (!q.isEmpty())
        {
            UUID u = q.poll();
            community.add(userController.getUser(u).toString());
            for (UUID v: adj.get(u))
            {
                if (!visited.get(v))
                {
                    visited.put(v, true);
                    q.add(v);
                }
            }
        }
        return community;
    }

    /**
     * Check if a user exists
     * @param username the username of the user
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String username)
    {
        return userController.checkUserExists(username) != null;
    }

    /**
     * Get a user by username
     * @param username the username of the user
     * @return the user
     */
    public User getUser(String username)
    {
        return userController.getUser(userController.checkUserExists(username));
    }

    /**
     * Respond to a friendship request
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     * @param response the response to the request
     */
    public void respondToFriendship(String username1, String username2, String response)
    {
        friendshipController.friendshipSetStatus(userController.checkUserExists(username1), userController.checkUserExists(username2), response);
        notifyObservers(new Event(EventType.RELATIONSHIP));
    }

    /**
     * Get a friendship
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     * @return the friendship
     */
    public Friendship getFriendship(String username1, String username2)
    {
        return friendshipController.getFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
    }

    public Conversation getOrCreateConversation(String username1, String username2)
    {
        if (!userExists(username1) || !userExists(username2))
        {
            throw new ServiceException("User does not exist");
        }
        if (username1.equals(username2))
        {
            throw new ServiceException("Cannot create conversation with self");
        }
        UUID user1 = userController.checkUserExists(username1);
        UUID user2 = userController.checkUserExists(username2);
        if (conversationController.getConversationBetweenUsers(user1, user2).isEmpty())
        {
            conversationController.createConversation(List.of(user1, user2));
        }
        return conversationController.getConversationBetweenUsers(user1, user2).get();
    }

    public void sendMessage(UUID conversationId, String username, String text, UUID replyTo)
    {
        if (!userExists(username))
        {
            throw new ServiceException("User does not exist");
        }
        UUID sender = userController.checkUserExists(username);
        conversationController.addMessage(conversationId, sender, text, replyTo);
        notifyObservers(new Event(EventType.MESSAGE));
    }

    public Message getMessage(UUID messageId)
    {
        if (messageId == null)
        {
            return null;
        }
        return conversationController.getMessage(messageId);
    }

    public boolean checkPassword(String username, String passwordHash)
    {
        var hash = userController.getUser(userController.checkUserExists(username)).getPasswordHash();
        if (hash == null)
        {
            return true;
        }
        return hash.equals(passwordHash);
    }

    public void saveImage(User user, Image image)
    {
        userController.saveImage(user.getId(), image);
        notifyObservers(new Event(EventType.PROFILE_PICTURE));
    }

    public Image getImage(User user)
    {
        return userController.getImage(user.getId());
    }
}
