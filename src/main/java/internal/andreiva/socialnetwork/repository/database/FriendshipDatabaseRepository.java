package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public class FriendshipDatabaseRepository extends AbstractDatabaseRepository<Friendship>
{
    public FriendshipDatabaseRepository(Connection db_connection)
    {
        super(db_connection, "friendships");
    }

    @Override
    protected Friendship resultToEntity(ResultSet rs)
    {
        try
        {
            Friendship f = new Friendship(UUID.fromString(rs.getString(2)), UUID.fromString(rs.getString(3)), rs.getString(4), rs.getTimestamp(5).toLocalDateTime());
            f.setId(UUID.fromString(rs.getString(1)));
            return f;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity)
    {
        String sql = "INSERT INTO " + database + " (UUID, friend_1, friend_2, status, friendssince) VALUES (?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, entity.getId().toString());
            stm.setObject(2, entity.getFriend1().toString());
            stm.setObject(3, entity.getFriend2().toString());
            stm.setString(4, entity.getStatus());
            stm.setTimestamp(5, Timestamp.valueOf(entity.getFriendSince()));
            int result = stm.executeUpdate();
            if (result == 0)
            {
                return Optional.of(entity);
            }
            return Optional.empty();
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
    public Optional<Friendship> delete(UUID id)
    {
        String sql = "DELETE FROM " + database + " WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            Optional<Friendship> f = findOne(id);
            int result = stm.executeUpdate();
            if (result == 0)
            {
                return Optional.empty();
            }
            return f;
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
    public Optional<Friendship> update(Friendship entity)
    {
        String sql = "UPDATE " + database + " SET friend_1 = ?, friend_2 = ?, status = ?, friendssince = ? WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, entity.getFriend1().toString());
            stm.setObject(2, entity.getFriend2().toString());
            stm.setString(3, entity.getStatus());
            stm.setTimestamp(4, Timestamp.valueOf(entity.getFriendSince()));
            stm.setObject(5, entity.getId().toString());
            int result = stm.executeUpdate();
            if (result == 0)
            {
                return Optional.of(entity);
            }
            return Optional.empty();
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
        finally
        {
            cache_valid = false;
        }
    }
}
