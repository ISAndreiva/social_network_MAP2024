package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.repository.Repository;
import internal.andreiva.socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractDatabaseRepository<E extends Entity> implements Repository<E>
{
    protected Connection db_connection;
    protected String database;

    public AbstractDatabaseRepository(Connection db_connection)
    {
        this.db_connection = db_connection;
    }

    protected abstract E result_to_entity(ResultSet rs);

    @Override
    public Optional<E> findOne(UUID id)
    {
        String sql = "SELECT * from " + database + " where UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            ResultSet rs = stm.executeQuery();
            rs.next();
            return Optional.ofNullable(result_to_entity(rs));
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll()
    {
        try
        {
            Statement stm = db_connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * from " + database);
            List<E> entity_list = new ArrayList<>();
            while (rs.next())
            {
                E entity = result_to_entity(rs);
                entity_list.add(entity);
            }
            return entity_list;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public abstract Optional<E> save(E entity);

    @Override
    public abstract Optional<E> delete(UUID id);

    @Override
    public abstract Optional<E> update(E entity);
}
