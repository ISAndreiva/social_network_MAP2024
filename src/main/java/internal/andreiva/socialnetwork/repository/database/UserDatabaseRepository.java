package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.repository.RepositoryException;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    public void saveImage(UUID userId, Image image) 
    {
        String sql = "UPDATE users SET profilepicture = ?  WHERE UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);

            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            byte[] pixelBytes = new byte[width * height * 4];
            if (image.getPixelReader() == null)
                throw new RepositoryException("Twas's no image, nilfguardian scum!");
            image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), pixelBytes, 0, width * 4);
            var stream = new ByteArrayInputStream(pixelBytes);
            stm.setBinaryStream(1, stream);
            stm.setObject(2, userId.toString());
            stm.executeUpdate();
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    public Image getImage(UUID userID)
    {
        String sql = "SELECT profilepicture from users where UUID = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, userID.toString());
            ResultSet rs = stm.executeQuery();
            if (rs.next())
            {
                var pixelBytes = rs.getBinaryStream(1);
                if (pixelBytes == null)
                    return null;
                var width = 140;
                var height = 140;
                var image = new WritableImage(width, height);
                image.getPixelWriter().setPixels(0, 0, width, height,
                        PixelFormat.getByteBgraInstance(),
                        pixelBytes.readAllBytes(), 0, width * 4);
                return image;
            }
            return null;
        }
        catch (SQLException | IOException e)
        {
            throw new RepositoryException(e);
        }

    }
}
