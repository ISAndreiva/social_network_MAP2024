package internal.andreiva.socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message extends Entity
{
    private final String text;
    private final LocalDateTime date;
    private final UUID sender;
    private Conversation parentConversation;
    private final UUID replyTo;

    public Message(String text, LocalDateTime date, UUID sender, Conversation parentConversation, UUID replyTo)
    {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.parentConversation = parentConversation;
        this.replyTo = replyTo;
    }

    public String getText()
    {
        return text;
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public UUID getSender()
    {
        return sender;
    }

    public UUID getReplyTo()
    {
        return replyTo;
    }

    public Conversation getParentConversation()
    {
        return parentConversation;
    }

    public void setParentConversation(Conversation conversation)
    {
        this.parentConversation = conversation;
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "text='" + text + '\'' +
                ", date=" + date +
                ", sender=" + sender +
                ", parentConversation=" + parentConversation +
                '}';
    }
}
