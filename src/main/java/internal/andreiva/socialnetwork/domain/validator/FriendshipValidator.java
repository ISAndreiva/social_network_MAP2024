package internal.andreiva.socialnetwork.domain.validator;

import internal.andreiva.socialnetwork.domain.Friendship;

/**
 * Friendship validator
 */
public class FriendshipValidator implements Validator<Friendship>
{

    @Override
    public void validate(Friendship entity) throws ValidationException
    {
        if (entity.getId() == null)
        {
            throw new ValidationException("An entity must have an id");
        }
        if (entity.getFriend1().equals(entity.getFriend2()))
        {
            throw new ValidationException("A friendship must be between two different users");
        }
        if (entity.getFriend1() == null || entity.getFriend2() == null)
        {
            throw new ValidationException("A friendship must have two users");
        }
        if (entity.getStatus() == null)
        {
            throw new ValidationException("A friendship must have a status");
        }
        if (!entity.getStatus().equals("pending") && !entity.getStatus().equals("accepted"))
        {
            throw new ValidationException("Invalid status");
        }
    }
    private FriendshipValidator() {}
    private static final FriendshipValidator instance = new FriendshipValidator();
    public static FriendshipValidator getInstance()
    {
        return instance;
    }
}
