package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.database.ConversationDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.UserDatabaseRepository;
import internal.andreiva.socialnetwork.repository.file.FriendshipFileRepo;
import internal.andreiva.socialnetwork.repository.file.UserFileRepo;
import internal.andreiva.socialnetwork.repository.particularinterfaces.ConversationRepository;
import internal.andreiva.socialnetwork.repository.particularinterfaces.FriendshipRepository;
import internal.andreiva.socialnetwork.repository.particularinterfaces.UserRepository;

import java.sql.Connection;

/**
 * Factory for creating repositories
 */
public class RepositoryFactory
{
    /**
     * Creates and returns a repository based on the given type
     * @param type - the type of the repository
     * @param fileName - the name of the file where the data is stored
     * @return the repository object or null if type is wrong
     */
    public FileMemoRepo<? extends Entity> getFileRepository(RepositoryType type, String fileName)
    {
        return switch (type)
        {
            case USER -> new FileMemoRepo<>(new UserFileRepo(fileName));
            case FRIENDSHIP -> new FileMemoRepo<>(new FriendshipFileRepo(fileName));
            case CONVERSATION -> null;
        };
    }

    public UserRepository getUserRepository(Connection db_connection)
    {
        return new UserDatabaseRepository(db_connection);
    }

    public FriendshipRepository getFriendshipRepository(Connection db_connection)
    {
        return new FriendshipDatabaseRepository(db_connection);
    }

    public ConversationRepository getConversationRepository(Connection db_connection)
    {
        return new ConversationDatabaseRepository(db_connection);
    }

    private RepositoryFactory() {}
    private static final RepositoryFactory instance = new RepositoryFactory();
    public static RepositoryFactory getInstance()
    {
        return instance;
    }
}
