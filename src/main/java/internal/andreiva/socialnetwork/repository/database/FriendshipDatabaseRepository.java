package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.repository.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Get the friends of a user
     * @param userId the user
     * @param status the status of the friendship
     * @return a list of the user's friends
     */
    public List<UUID> getFriendships(UUID userId, String status)
    {
        String sql = "SELECT * FROM " + database + " WHERE (friend_1 = ? OR friend_2 = ?) AND status = ?";
        try
        {
            List<UUID> friends = new ArrayList<>();
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, userId.toString());
            stm.setObject(2, userId.toString());
            stm.setString(3, status);
            ResultSet rs = stm.executeQuery();
            while (rs.next())
            {
                Friendship f = resultToEntity(rs);
                if (f.getFriend1().equals(userId))
                    friends.add(f.getFriend2());
                if (f.getFriend2().equals(userId))
                    friends.add(f.getFriend1());
            }
            return friends;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }
    /**
     * Get a friendship if it exists
     * @param friend1 user 1
     * @param friend2 user 2
     * @return the friendship if it exists, null otherwise
     */
    public Optional<Friendship> getFriendship(UUID friend1, UUID friend2)
    {
        String sql = "SELECT * FROM " + database + " WHERE (friend_1 = ? AND friend_2 = ?) OR (friend_1 = ? AND friend_2 = ?)";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, friend1.toString());
            stm.setObject(2, friend2.toString());
            stm.setObject(3, friend2.toString());
            stm.setObject(4, friend1.toString());
            ResultSet rs = stm.executeQuery();
            if (rs.next())
            {
                return Optional.of(resultToEntity(rs));
            }
            return Optional.empty();
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    public List<UUID> getReceivedFriendRequests(UUID userId)
    {
        String sql = "SELECT * FROM " + database + " WHERE friend_2 = ? AND status = 'pending'";
        try
        {
            List<UUID> friends = new ArrayList<>();
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, userId.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next())
            {
                Friendship f = resultToEntity(rs);
                    friends.add(f.getFriend1());
            }
            return friends;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }
}
