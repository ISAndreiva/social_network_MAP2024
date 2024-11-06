package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class UserDatabaseRepository extends AbstractDatabaseRepository<User>
{
    public UserDatabaseRepository(Connection db_connection)
    {
        super(db_connection, "users");
    }

    @Override
    protected User resultToEntity(ResultSet rs)
    {
        try
        {
            User u = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            u.setId(UUID.fromString(rs.getString(1)));
            return u;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> save(User entity)
    {
        String sql = "INSERT INTO " + database + " (UUID, firstName, lastName, username, email) VALUES (?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, entity.getId().toString());
            stm.setString(2, entity.getFirstName());
            stm.setString(3, entity.getLastName());
            stm.setString(4, entity.getUsername());
            stm.setString(5, entity.getEmail());
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
    public Optional<User> delete(UUID id)
    {
        String sql = "DELETE FROM " + database + " WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            Optional<User> u = findOne(id);
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
    public Optional<User> update(User entity)
    {
        String sql = "UPDATE " + database + " SET firstName = ?, lastName = ?, username = ?, email = ? WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setString(1, entity.getFirstName());
            stm.setString(2, entity.getLastName());
            stm.setString(3, entity.getUsername());
            stm.setString(4, entity.getEmail());
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

    public Optional<User> findOne(String username)
    {
        String sql = "SELECT * from " + database + " where username = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            if (rs.next())
                return Optional.of(resultToEntity(rs));
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }
}
