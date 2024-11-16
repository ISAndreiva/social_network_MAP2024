package internal.andreiva.socialnetwork;

import internal.andreiva.socialnetwork.gui.Gui;
import internal.andreiva.socialnetwork.repository.RepositoryException;
import internal.andreiva.socialnetwork.service.Service;
import internal.andreiva.socialnetwork.cli.CLI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class app
{
    private final Connection db_connection;

    public app()
    {
        try
        {
            db_connection = DriverManager.getConnection(DatabaseConfig.getDbUrl(), DatabaseConfig.getDbUsername(), DatabaseConfig.getDbPassword());
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }

    }

    public void run_cli()
    {
        Service service = new Service(db_connection);
        CLI cli = new CLI(service);
        cli.run();
    }

    public void run_gui()
    {
        Service service = new Service(db_connection);
        Gui.setService(service);
        Gui.launch();
    }

    public void close_db()
    {
        try
        {
            db_connection.close();
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }
}
