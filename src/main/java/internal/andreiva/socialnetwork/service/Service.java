package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.User;

import java.util.*;

public class Service
{
    private FriendshipController friendshipController;
    private UserController userController;

    public Service(String userFilename, String friendshipFilename)
    {
        friendshipController = ControllerFactory.getInstance().getFriendshipService(friendshipFilename);
        userController = ControllerFactory.getInstance().getUserService(userFilename);
    }

    public void addUser(String firstName, String lastName, String username, String password)
    {
        userController.addUser(firstName, lastName, username, password);
    }

    public void deleteUser(String username)
    {
        User user = userController.deleteUser(username);
        friendshipController.deleteFriendships(user.getId());
    }

    public void updateUser(String firstName, String lastName, String username, String password)
    {
        userController.updateUser(firstName, lastName, username, password);
    }

    public String[] getUsers()
    {
        return userController.getUsers();
    }

    public void addFriendship(String username1, String username2)
    {
        friendshipController.addFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
    }

    public void deleteFriendship(String username1, String username2)
    {
        friendshipController.deleteFriendship(userController.checkUserExists(username1), userController.checkUserExists(username2));
    }

    public String[] getFriends(String username)
    {
        List<UUID> friendships = friendshipController.getFriends(userController.checkUserExists(username));
        String[] friends = new String[friendships.size()];
        for (int i = 0; i < friendships.size(); i++)
        {
            friends[i] = userController.getUser(friendships.get(i)).toString();
        }
        return friends;
    }

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

    public int no_communities()
    {
        Map<UUID, List<UUID>> adj = new HashMap<>();
        for (User u: userController.getUsersIterable())
        {
            adj.put(u.getId(), new ArrayList<>());
        }
        for (Friendship f: friendshipController.getFriendshipsIterable())
        {
            adj.get(f.getFriend1()).add(f.getFriend2());
            adj.get(f.getFriend2()).add(f.getFriend1());
        }
        Map<UUID, Boolean> visited = new HashMap<>();
        for (User u: userController.getUsersIterable())
        {
            visited.put(u.getId(), false);
        }
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

    public String[] biggest_community()
    {
        Map<UUID, List<UUID>> adj = new HashMap<>();
        for (User u: userController.getUsersIterable())
        {
            adj.put(u.getId(), new ArrayList<>());
        }
        for (Friendship f: friendshipController.getFriendshipsIterable())
        {
            adj.get(f.getFriend1()).add(f.getFriend2());
            adj.get(f.getFriend2()).add(f.getFriend1());
        }
        Map<UUID, Boolean> visited = new HashMap<>();
        for (User u: userController.getUsersIterable())
        {
            visited.put(u.getId(), false);
        }
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
        for (User u: userController.getUsersIterable())
        {
            visited.put(u.getId(), false);
        }
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
        return community.toArray(new String[0]);
    }

}
