package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.repository.FileMemoRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FriendshipService
{
    FileMemoRepo<Friendship> friendshipRepo;
    FriendshipValidator friendshipValidator;

    public FriendshipService(FileMemoRepo<Friendship> friendshipRepo, FriendshipValidator friendshipValidator)
    {
        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
    }

    public void addFriendship(UUID friend1, UUID friend2)
    {
        if (friend1 == null || friend2 == null)
        {
            throw new ServiceException("Invalid user(s)");
        }
        if (checkFriendshipExists(friend1, friend2) != null)
        {
            throw new ServiceException("Friendship already exists");
        }
        Friendship f = new Friendship(friend1, friend2);
        f.setId(UUID.randomUUID());
        friendshipValidator.validate(f);
        if (friendshipRepo.save(f) != null)
        {
            throw new ServiceException("An error occurred adding the friendship");
        }
    }

    public void deleteFriendship(UUID friend1, UUID friend2)
    {
        if (friend1 == null || friend2 == null)
        {
            throw new ServiceException("Invalid user");
        }
        UUID id = checkFriendshipExists(friend1, friend2);
        if (id == null)
        {
            throw new ServiceException("Friendship does not exist");
        }
        if (friendshipRepo.delete(id) == null)
        {
            throw new ServiceException("An error occurred deleting the friendship");
        }
    }

    public void deleteFriendships(UUID userId)
    {
        for (Friendship f : friendshipRepo.findAll())
        {
            if (f.getFriend1().equals(userId) || f.getFriend2().equals(userId))
            {
                friendshipRepo.delete(f.getId());
            }
        }
    }

    private UUID checkFriendshipExists(UUID friend1, UUID friend2)
    {
        for (Friendship f : friendshipRepo.findAll())
        {
            if ((f.getFriend1().equals(friend1) && f.getFriend2().equals(friend2)) || (f.getFriend1().equals(friend2) && f.getFriend2().equals(friend1)))
            {
                return f.getId();
            }
        }
        return null;
    }

    public List<UUID> getFriends(UUID userId)
    {
        if (userId == null)
        {
            throw new ServiceException("Invalid user");
        }
        List<UUID> friends = new ArrayList<>();
        for (Friendship f : friendshipRepo.findAll())
        {
            if (f.getFriend1().equals(userId))
            {
                friends.add(f.getFriend2());
            }
            if (f.getFriend2().equals(userId))
            {
                friends.add(f.getFriend1());
            }
        }
        return friends;
    }

}
