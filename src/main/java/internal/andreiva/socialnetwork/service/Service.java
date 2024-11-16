package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.User;

import java.sql.Connection;
import java.util.*;


/**
 * Service class that provides methods for managing users and friendships
 */
public class Service
{
    private final FriendshipController friendshipController;
    private final UserController userController;

    /**
     * Constructor
     * @param db_connection the connection to the database
     */
    public Service(Connection db_connection)
    {
        friendshipController = ControllerFactory.getInstance().getFriendshipService(db_connection);
        userController = ControllerFactory.getInstance().getUserService(db_connection);
    }

    /**
     * Add a user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param username the username of the user
     * @param password  the password of the user
     */
    public void addUser(String firstName, String lastName, String username, String password)
    {
        userController.addUser(firstName, lastName, username, password);
    }

    /**
     * Delete a user
     * @param username the username of the user
     */
    public void deleteUser(String username)
    {
        User user = userController.deleteUser(username);
        friendshipController.deleteFriendships(user.getId());
    }

    /**
     * Update a user
     * @param firstName the new first name of the user
     * @param lastName the new last name of the user
     * @param username the username of the existing user
     * @param email the new email of the user
     */
    public void updateUser(String firstName, String lastName, String username, String email)
    {
        userController.updateUser(firstName, lastName, username, email);
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
    }

    /**
     * Delete a friendship between two users
     * @param username1 the username of the first user
     * @param username2 the username of the second user
     */
    public void deleteFriendship(String username1, String username2)
    {
        friendshipController.deleteFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
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

}
