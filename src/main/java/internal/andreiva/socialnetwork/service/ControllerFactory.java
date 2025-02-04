package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.validator.FriendshipValidator;
import internal.andreiva.socialnetwork.domain.validator.MessageValidator;
import internal.andreiva.socialnetwork.domain.validator.UserValidator;
import internal.andreiva.socialnetwork.repository.RepositoryFactory;
import internal.andreiva.socialnetwork.repository.RepositoryType;
import internal.andreiva.socialnetwork.repository.database.ConversationDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.FriendshipDatabaseRepository;
import internal.andreiva.socialnetwork.repository.database.UserDatabaseRepository;

import java.sql.Connection;

/**
 * Factory for creating controllers
 */
public class ControllerFactory
{
    /**
     * Creates and returns a FriendshipController
     * @param db_connection a connection to the database
     * @return a FriendshipController
     */
    public FriendshipController getFriendshipService(Connection db_connection)
    {
        return new FriendshipController((FriendshipDatabaseRepository) RepositoryFactory.getInstance().getDatabaseRepository(RepositoryType.FRIENDSHIP, db_connection), FriendshipValidator.getInstance());
    }

    /**
     * Creates and returns a UserController
     * @param db_connection a connection to the database
     * @return a UserController
     */
    public UserController getUserService(Connection db_connection)
    {
        return new UserController((UserDatabaseRepository) RepositoryFactory.getInstance().getDatabaseRepository(RepositoryType.USER, db_connection), UserValidator.getInstance());
    }

    /**
     * Creates and returns a ConversationController
     * @param db_connection a connection to the database
     * @return a ConversationController
     */
    public ConversationController getConversationService(Connection db_connection)
    {
        return new ConversationController((ConversationDatabaseRepository) RepositoryFactory.getInstance().getDatabaseRepository(RepositoryType.CONVERSATION, db_connection), MessageValidator.getInstance());
    }

    private ControllerFactory() {}
    private static final ControllerFactory instance = new ControllerFactory();
    public static ControllerFactory getInstance()
    {
        return instance;
    }
}
