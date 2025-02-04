package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.repository.database.ConversationDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.UserDatabaseRepository;
import internal.andreiva.socialnetwork.repository.particularinterfaces.ConversationRepository;
import internal.andreiva.socialnetwork.repository.particularinterfaces.FriendshipRepository;
import internal.andreiva.socialnetwork.repository.particularinterfaces.UserRepository;

import java.sql.Connection;

/**
 * Factory for creating repositories
 */
public class RepositoryFactory
{
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
