package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.file.FriendshipFileRepo;
import internal.andreiva.socialnetwork.repository.file.UserFileRepo;

public class RepositoryFactory
{
    public FileMemoRepo<? extends Entity> getRepository(RepositoryType type, String fileName)
    {
        return switch (type)
        {
            case USER -> new FileMemoRepo<>(new UserFileRepo(fileName));
            case FRIENDSHIP -> new FileMemoRepo<>(new FriendshipFileRepo(fileName));
        };
    }

    private RepositoryFactory() {}
    private static final RepositoryFactory instance = new RepositoryFactory();
    public static RepositoryFactory getInstance()
    {
        return instance;
    }
}
