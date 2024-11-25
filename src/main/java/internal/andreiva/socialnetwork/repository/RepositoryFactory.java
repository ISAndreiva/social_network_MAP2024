package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.database.AbstractDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.ConversationDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.UserDatabaseRepository;
import internal.andreiva.socialnetwork.repository.file.FriendshipFileRepo;
import internal.andreiva.socialnetwork.repository.file.UserFileRepo;

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

    public AbstractDatabaseRepository<? extends Entity> getDatabaseRepository(RepositoryType type, Connection db_connection)
    {
        return switch (type)
        {
            case USER -> new UserDatabaseRepository(db_connection);
            case FRIENDSHIP -> new FriendshipDatabaseRepository(db_connection);
            case CONVERSATION -> new ConversationDatabaseRepository(db_connection);
        };
    }

    private RepositoryFactory() {}
    private static final RepositoryFactory instance = new RepositoryFactory();
    public static RepositoryFactory getInstance()
    {
        return instance;
    }
}
