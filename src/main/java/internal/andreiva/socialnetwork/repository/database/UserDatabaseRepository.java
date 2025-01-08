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
            User u = new User(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
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
        String sql = "INSERT INTO users (UUID, firstName, lastName, username, email, passwordhash) VALUES (?, ?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, entity.getId().toString());
            stm.setString(2, entity.getFirstName());
            stm.setString(3, entity.getLastName());
            stm.setString(4, entity.getUsername());
            stm.setString(5, entity.getEmail());
            stm.setString(6, entity.getPasswordHash());
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
    public Optional<User> update(User entity)
    {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, email = ?, passwordhash = ? WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setString(1, entity.getFirstName());
            stm.setString(2, entity.getLastName());
            stm.setString(3, entity.getEmail());
            stm.setString(4, entity.getPasswordHash());
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
        String sql = "SELECT * from users where username = ?";
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
