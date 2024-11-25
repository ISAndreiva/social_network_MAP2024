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
    protected final Connection db_connection;
    protected final String database;
    protected final List<E> find_cache = new ArrayList<>();
    protected boolean cache_valid = false;

    public AbstractDatabaseRepository(Connection db_connection, String database)
    {
        this.db_connection = db_connection;
        this.database = database;
        populateCache();
    }

    protected abstract E resultToEntity(ResultSet rs);

    protected void populateCache()
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
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<E> findOne(UUID id)
    {
        if (!cache_valid)
        {
            populateCache();
        }
        return find_cache.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public Iterable<E> findAll()
    {
        if (!cache_valid)
        {
            populateCache();
        }
        return find_cache;
    }

    @Override
    public Optional<E> delete(UUID id)
    {
        String sql = "DELETE FROM " + database + " WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            Optional<E> u = findOne(id);
            int result = stm.executeUpdate();
            if (result == 0)
            {
                return Optional.empty();
            }
            return u;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
        finally
        {
            cache_valid = false;
        }
    }

    @Override
    public abstract Optional<E> save(E entity);

    @Override
    public abstract Optional<E> update(E entity);
}
