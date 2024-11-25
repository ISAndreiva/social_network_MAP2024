package internal.andreiva.socialnetwork.repository.database;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.Message;
import internal.andreiva.socialnetwork.repository.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConversationDatabaseRepository extends AbstractDatabaseRepository<Conversation>
{
    public ConversationDatabaseRepository(Connection db_connection)
    {
        super(db_connection, "conversations");
    }

    private Message resultToMessage(ResultSet rs)
    {
        try
        {
            Message m = new Message(rs.getString(2), rs.getTimestamp(3).toLocalDateTime(), UUID.fromString(rs.getString(4)), null);
            m.setId(UUID.fromString(rs.getString(1)));
            return m;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    private List<UUID> getConversationUsers(UUID id)
    {
        List<UUID> users = new ArrayList<>();
        String sql = "SELECT \"user\" from conversationUsers where conversation = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next())
            {
                users.add(UUID.fromString(rs.getString(1)));
            }
            return users;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    private List<Message> getConversationMessages(UUID id)
    {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * from messages where conversation = ?";
        try
        {
            PreparedStatement stm = db_connection.prepareStatement(sql);
            stm.setObject(1, id.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next())
            {
                messages.add(resultToMessage(rs));
            }
            return messages;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    protected Conversation resultToEntity(ResultSet rs)
    {
        try
        {
            UUID id = UUID.fromString(rs.getString(1));
            Conversation c = new Conversation(getConversationUsers(id), getConversationMessages(id));
            c.setId(UUID.fromString(rs.getString(1)));
            for (Message m : c.getMessages())
            {
                m.setParentConversation(c);
            }
            return c;
        } catch (SQLException e)
        {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Conversation> delete(UUID id)
    {
        String sqlDelConv = "DELETE FROM conversations WHERE UUID = ?";
        String sqlDelConvUser = "DELETE FROM conversationUsers WHERE conversation = ?";
        String sqlDelMessages = "DELETE FROM messages WHERE conversation = ?";
        try
        {
            PreparedStatement stmDelConv = db_connection.prepareStatement(sqlDelConv);
            PreparedStatement stmDelConvUser = db_connection.prepareStatement(sqlDelConvUser);
            PreparedStatement stmDelMessages = db_connection.prepareStatement(sqlDelMessages);
            stmDelConv.setObject(1, id.toString());
            stmDelConvUser.setObject(1, id.toString());
            stmDelMessages.setObject(1, id.toString());


            Optional<Conversation> u = findOne(id);
            stmDelMessages.executeUpdate();
            stmDelConvUser.executeUpdate();
            int result = stmDelConv.executeUpdate();
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
    public Optional<Conversation> save(Conversation entity)
    {
        String sqlConv = "INSERT INTO conversations (UUID) VALUES (?)";
        String sqlConvUser = "INSERT INTO conversationUsers (conversation, \"user\") VALUES (?, ?)";
        String sqlMessages = "INSERT INTO messages (UUID, text, date , sender, conversation) VALUES (?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement stmConv = db_connection.prepareStatement(sqlConv);
            PreparedStatement stmConvUser = db_connection.prepareStatement(sqlConvUser);
            PreparedStatement stmMessages = db_connection.prepareStatement(sqlMessages);
            stmConv.setObject(1, entity.getId().toString());
            int result = stmConv.executeUpdate();
            if (result == 0)
            {
                return Optional.of(entity);
            }
            for (UUID u : entity.getMembers())
            {
                stmConvUser.setObject(1, entity.getId().toString());
                stmConvUser.setObject(2, u.toString());
                stmConvUser.executeUpdate();
            }
            for (Message m : entity.getMessages())
            {
                stmMessages.setObject(1, m.getId().toString());
                stmMessages.setString(2, m.getText());
                stmMessages.setTimestamp(3, java.sql.Timestamp.valueOf(m.getDate()));
                stmMessages.setObject(4, m.getSender());
                stmMessages.setObject(5, entity.getId().toString());
                stmMessages.executeUpdate();
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
    public Optional<Conversation> update(Conversation entity)
    {
        String sqlConvUser = "INSERT INTO conversationUsers (conversation, \"user\") VALUES (?, ?)";
        String sqlMessages = "INSERT INTO messages (UUID, text, date , sender, conversation) VALUES (?, ?, ?, ?, ?)";
        try
        {
            PreparedStatement stmConvUser = db_connection.prepareStatement(sqlConvUser);
            PreparedStatement stmMessages = db_connection.prepareStatement(sqlMessages);
            int result = 0;

            for (UUID u : entity.getMembers())
            {
                PreparedStatement stm = db_connection.prepareStatement("SELECT * FROM conversationUsers WHERE conversation = ? AND \"user\" = ?");
                stm.setObject(1, entity.getId().toString());
                stm.setObject(2, u.toString());
                if (stm.executeQuery().next())
                {
                    continue;
                }
                stmConvUser.setObject(1, entity.getId().toString());
                stmConvUser.setObject(2, u.toString());
                result += stmConvUser.executeUpdate();
            }
            for (Message m : entity.getMessages())
            {
                PreparedStatement stm = db_connection.prepareStatement("SELECT * FROM messages WHERE UUID = ?");
                stm.setObject(1, m.getId().toString());
                if (stm.executeQuery().next())
                {
                    continue;
                }
                stmMessages.setObject(1, m.getId().toString());
                stmMessages.setString(2, m.getText());
                stmMessages.setTimestamp(3, java.sql.Timestamp.valueOf(m.getDate()));
                stmMessages.setObject(4, m.getSender());
                stmMessages.setObject(5, entity.getId().toString());
                result += stmMessages.executeUpdate();
            }
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
