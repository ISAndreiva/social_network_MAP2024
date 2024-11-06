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
    protected final String database;
    protected List<E> find_cache = new ArrayList<>();
    protected boolean cache_valid = false;

    public AbstractDatabaseRepository(Connection db_connection, String database)
    {
        this.db_connection = db_connection;
        this.database = database;
    }

    protected abstract E resultToEntity(ResultSet rs);

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
            return Optional.ofNullable(resultToEntity(rs));
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    @Override
    public Iterable<E> findAll()
    {
        if (cache_valid)
        {
            return find_cache;
        }
        else
        {
            find_cache.clear();
            try
            {
                Statement stm = db_connection.createStatement();
                ResultSet rs = stm.executeQuery("SELECT * from " + database);
                while (rs.next())
                {
                    E entity = resultToEntity(rs);
                    find_cache.add(entity);
                }
                cache_valid = true;
                return find_cache;
            } catch (SQLException e)
            {
                throw new RepositoryException(e);
            }
        }
    }

    @Override
    public abstract Optional<E> save(E entity);

    @Override
    public abstract Optional<E> delete(UUID id);

    @Override
    public abstract Optional<E> update(E entity);
}
