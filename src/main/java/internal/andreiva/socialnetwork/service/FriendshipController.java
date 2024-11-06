package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

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
        Friendship f = new Friendship(friend1, friend2);
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
        var friends = getFriends(userId);
        friends.forEach(f -> deleteFriendship(userId, f));
    }

    /**
     * Check if a friendship exists
     * @param friend1 user 1
     * @param friend2 user 2
     * @return the id of the friendship if it exists, null otherwise
     */
    private UUID checkFriendshipExists(UUID friend1, UUID friend2)
    {
        Predicate<Friendship> found = f -> (f.getFriend1().equals(friend1) && f.getFriend2().equals(friend2)) ||
                                           (f.getFriend1().equals(friend2) && f.getFriend2().equals(friend1));
        var friendship =  StreamSupport.stream(friendshipRepo.findAll().spliterator(), false).filter(found).findFirst().orElse(null);
        if (friendship != null)
            return friendship.getId();
        return null;
    }

    /**
     * Get the friends of a user
     * @param userId the user
     * @return a list of the user's friends
     */
    public List<UUID> getFriends(UUID userId)
    {
        if (userId == null)
        {
            throw new ServiceException("Invalid user");
        }
        List<UUID> friends = new ArrayList<>();
        friendshipRepo.findAll().forEach(f -> {
            if (f.getFriend1().equals(userId)) friends.add(f.getFriend2());
            if (f.getFriend2().equals(userId)) friends.add(f.getFriend1());});
        return friends;
    }

    /**
     * Get all friendships
     * @return an iterable of all friendships
     */
    public Iterable<Friendship> getFriendshipsIterable()
    {
        return friendshipRepo.findAll();
    }
}
