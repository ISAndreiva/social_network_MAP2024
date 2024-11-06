package internal.andreiva.socialnetwork;

import internal.andreiva.socialnetwork.repository.RepositoryException;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.ui.CLI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class app
{
    public void run()
    {
        try (Connection db_connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword()))
        {
            Service service = new Service(db_connection);
            CLI cli = new CLI(service);
            cli.run();
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }
}
