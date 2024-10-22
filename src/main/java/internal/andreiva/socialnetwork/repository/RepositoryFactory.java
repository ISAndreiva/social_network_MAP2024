package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.file.FriendshipFileRepo;
import internal.andreiva.socialnetwork.repository.file.UserFileRepo;

/**
 * Factory for creating repositories
 */
public class RepositoryFactory
{
    /**
     * Creates and returns an repository based on the given type
     * @param type - the type of the repository
     * @param fileName - the name of the file where the data is stored
     * @return the repository object or null if type is wrong
     */
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
