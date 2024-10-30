package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.User;

import java.util.*;


/**
 * Service class that provides methods for managing users and friendships
 */
public class Service
{
    private FriendshipController friendshipController;
    private UserController userController;

    /**
     * Constructor
     * @param userFilename the filename for the user repository
     * @param friendshipFilename the filename for the friendship repository
     */
    public Service(String userFilename, String friendshipFilename)
    {
        friendshipController = ControllerFactory.getInstance().getFriendshipService(friendshipFilename);
        userController = ControllerFactory.getInstance().getUserService(userFilename);
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
     * @param password the new password of the user
     */
    public void updateUser(String firstName, String lastName, String username, String password)
    {
        userController.updateUser(firstName, lastName, username, password);
    }

    /**
     * Returns a list of all users
     * @return an array of all users
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
     * @return an array of friends of the user
     */
    public List<String> getFriends(String username)
    {
        List<String> friends = new ArrayList<>();
        friendshipController.getFriends(userController.checkUserExists(username)).forEach(f -> friends.add(userController.getUser(f).toString()));
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
            adj.get(f.getFriend1()).add(f.getFriend2());
            adj.get(f.getFriend2()).add(f.getFriend1());
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
     * @return array of the members of the biggest community
     */
    public List<String> biggest_community()
    {
        Map<UUID, List<UUID>> adj = new HashMap<>();
        userController.getUsersIterable().forEach(u -> adj.put(u.getId(), new ArrayList<>()));
        friendshipController.getFriendshipsIterable().forEach(f -> {
            adj.get(f.getFriend1()).add(f.getFriend2());
            adj.get(f.getFriend2()).add(f.getFriend1());
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

}
