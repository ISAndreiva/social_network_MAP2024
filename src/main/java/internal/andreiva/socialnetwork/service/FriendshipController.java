package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing friendships
 */
public class FriendshipController
{
    private final FriendshipDatabaseRepository friendshipRepo;
    private final FriendshipValidator friendshipValidator;

    /**
     * Constructor
     * @param friendshipRepo repository for friendships
     * @param friendshipValidator validator for friendships
     */
    public FriendshipController(FriendshipDatabaseRepository friendshipRepo, FriendshipValidator friendshipValidator)
    {
        this.friendshipRepo = friendshipRepo;
        this.friendshipValidator = friendshipValidator;
    }

    /**
     * Add a friendship
     * @param friend1 user 1
     * @param friend2 user 2
     */
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
        Friendship f = new Friendship(friend1, friend2, "pending", LocalDateTime.now());
        f.setId(UUID.randomUUID());
        friendshipValidator.validate(f);
        if (friendshipRepo.save(f).isPresent())
        {
            throw new ServiceException("An error occurred adding the friendship");
        }
    }

    /**
     * Delete a friendship
     * @param friend1 user 1
     * @param friend2 user 2
     */
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
        if (friendshipRepo.delete(id).isEmpty())
        {
            throw new ServiceException("An error occurred deleting the friendship");
        }
    }

    /**
     * Delete all friendships of a user
     * @param userId the user
     */
    public void deleteFriendships(UUID userId)
    {
        List<UUID> friendships = new ArrayList<>();
        friendships.addAll(getFriendships(userId, "accepted"));
        friendships.addAll(getFriendships(userId, "pending"));
        friendships.forEach(friendshipRepo::delete);
    }

    /**
     * Check if a friendship exists
     * @param friend1 user 1
     * @param friend2 user 2
     * @return the id of the friendship if it exists, null otherwise
     */
    private UUID checkFriendshipExists(UUID friend1, UUID friend2)
    {
        var friendship = friendshipRepo.getFriendship(friend1, friend2);
        return friendship.map(Friendship::getId).orElse(null);
    }

    /**
     * Get a friendship
     * @param friend1 user 1
     * @param friend2 user 2
     * @return the friendship
     */
    public Friendship getFriendship(UUID friend1, UUID friend2)
    {
        return friendshipRepo.findOne(checkFriendshipExists(friend1, friend2)).orElse(null);
    }

    /**
     * Get the friends of a user
     * @param userId the user
     * @param status the status of the friendship
     * @return a list of the user's friends
     */
    public List<UUID> getFriendships(UUID userId, String status)
    {
        if (userId == null)
        {
            throw new ServiceException("Invalid user");
        }
        if (!status.equals("accepted") && !status.equals("pending"))
        {
            throw new ServiceException("Invalid status");
        }
        return friendshipRepo.getFriendships(userId, status);
    }

    public List<UUID> getReceivedFriendRequests(UUID userId)
    {
        if (userId == null)
        {
            throw new ServiceException("Invalid user");
        }
        return friendshipRepo.getReceivedFriendRequests(userId);
    }


    /**
     * Get all friendships
     * @return an iterable of all friendships
     */
    public Iterable<Friendship> getFriendshipsIterable()
    {
        return friendshipRepo.findAll();
    }

    /**
     * Set the status of a friendship
     * @param friend1 user 1
     * @param friend2 user 2
     * @param status the new status
     */
    public void friendshipSetStatus(UUID friend1, UUID friend2, String status)
    {
        if (status.equals("rejected"))
        {
            deleteFriendship(friend1, friend2);
            return;
        }

        Friendship f = friendshipRepo.findOne(checkFriendshipExists(friend1, friend2)).orElse(null);
        if (f == null)
        {
            throw new ServiceException("Friendship does not exist");
        }
        if (!f.getStatus().equals("pending"))
        {
            throw new ServiceException("Friendship is not pending");
        }
        f.setStatus(status);
        f.setFriendSince(LocalDateTime.now());
        friendshipValidator.validate(f);
        if (friendshipRepo.update(f).isPresent())
        {
            throw new ServiceException("An error occurred accepting the friendship");
        }
    }
}
