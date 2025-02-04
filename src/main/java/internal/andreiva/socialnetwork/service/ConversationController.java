package internal.andreiva.socialnetwork.service;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.Message;
import internal.andreiva.socialnetwork.domain.validator.MessageValidator;
import internal.andreiva.socialnetwork.repository.particularinterfaces.ConversationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

public class ConversationController
{
    private final ConversationRepository conversationRepo;
    private final MessageValidator messageValidator;

    public ConversationController(ConversationRepository conversationRepo, MessageValidator messageValidator)
    {
        this.conversationRepo = conversationRepo;
        this.messageValidator = messageValidator;
    }

    public void createConversation(List<UUID> users)
    {
        Conversation conversation = new Conversation(users, List.of());
        conversation.setId(UUID.randomUUID());
        if (conversationRepo.save(conversation).isPresent())
        {
            throw new ServiceException("An error occurred creating the conversation");
        }
    }

    public void addMessage(UUID conversationId, UUID sender, String text, UUID replyTo)
    {
        var result = conversationRepo.findOne(conversationId);
        if (result.isEmpty())
        {
            throw new ServiceException("Conversation does not exist");
        }
        var conversation = result.get();
        Message message = new Message(text, LocalDateTime.now(), sender, conversation, replyTo);
        message.setId(UUID.randomUUID());
        messageValidator.validate(message);
        conversation.addMessage(message);
        conversationRepo.update(conversation);
    }

    public void deleteConversation(UUID conversationId)
    {
        var result = conversationRepo.findOne(conversationId);
        if (result.isEmpty())
        {
            throw new ServiceException("Conversation does not exist");
        }
        conversationRepo.delete(conversationId);
    }

    public void addMember(UUID conversationId, UUID user)
    {
        var result = conversationRepo.findOne(conversationId);
        if (result.isEmpty())
        {
            throw new ServiceException("Conversation does not exist");
        }
        var conversation = result.get();
        conversation.addMember(user);
        conversationRepo.update(conversation);
    }

    public Optional<Conversation> getConversationBetweenUsers(UUID user1, UUID user2)
    {
        return StreamSupport.stream(conversationRepo.findAll().spliterator(), false)
                .filter(conversation -> conversation.getMembers().contains(user1) && conversation.getMembers().contains(user2)).findFirst();
    }

    public Message getMessage(UUID messageId)
    {
        var result = conversationRepo.findOneMessage(messageId);
        if (result.isEmpty())
        {
            throw new ServiceException("Message does not exist");
        }
        return result.get();
    }
}
