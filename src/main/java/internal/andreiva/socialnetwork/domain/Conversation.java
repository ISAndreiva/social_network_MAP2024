package internal.andreiva.socialnetwork.domain;

import java.util.List;
import java.util.UUID;

public class Conversation extends Entity
{
    private final List<UUID> members;
    private final List<Message> messages;

    public Conversation(List<UUID> members, List<Message> messages)
    {
        this.members = members;
        this.messages = messages;
    }

    public List<UUID> getMembers()
    {
        return members;
    }

    public void addMember(UUID user)
    {
        members.add(user);
    }

    public void removeMember(UUID user)
    {
        members.remove(user);
    }

    public List<Message> getMessages()
    {
        return messages;
    }

    public void addMessage(Message message)
    {
        messages.add(message);
    }

    public void removeMessage(Message message)
    {
        messages.remove(message);
    }
}
