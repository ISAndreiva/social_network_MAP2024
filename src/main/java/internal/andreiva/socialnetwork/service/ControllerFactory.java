package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.FileMemoRepo;
import internal.andreiva.socialnetwork.repository.RepositoryFactory;
import internal.andreiva.socialnetwork.repository.RepositoryType;

public class ControllerFactory
{
    public FriendshipController getFriendshipService(String filename)
    {
        return new FriendshipController((FileMemoRepo<Friendship>) RepositoryFactory.getInstance().getRepository(RepositoryType.FRIENDSHIP, filename), FriendshipValidator.getInstance());
    }

    public UserController getUserService(String filename)
    {
        return new UserController((FileMemoRepo<User>) RepositoryFactory.getInstance().getRepository(RepositoryType.USER, filename), UserValidator.getInstance());
    }

    private ControllerFactory() {}
    private static final ControllerFactory instance = new ControllerFactory();
    public static ControllerFactory getInstance()
    {
        return instance;
    }
}
