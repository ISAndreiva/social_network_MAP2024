package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.FileMemoRepo;
import internal.andreiva.socialnetwork.repository.RepositoryFactory;
import internal.andreiva.socialnetwork.repository.RepositoryType;

public class ServiceFactory
{
    public FriendshipService getFriendshipService(String filename)
    {
        return new FriendshipService((FileMemoRepo<Friendship>) RepositoryFactory.getInstance().getRepository(RepositoryType.FRIENDSHIP, filename), FriendshipValidator.getInstance());
    }

    public UserService getUserService(String filename)
    {
        return new UserService((FileMemoRepo<User>) RepositoryFactory.getInstance().getRepository(RepositoryType.USER, filename), UserValidator.getInstance());
    }

    private ServiceFactory() {}
    private static final ServiceFactory instance = new ServiceFactory();
    public static ServiceFactory getInstance()
    {
        return instance;
    }
}
