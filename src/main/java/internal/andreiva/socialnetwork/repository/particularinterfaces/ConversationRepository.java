package internal.andreiva.socialnetwork.repository.particularinterfaces;

import internal.andreiva.socialnetwork.domain.Conversation;
import internal.andreiva.socialnetwork.domain.Message;
import internal.andreiva.socialnetwork.repository.PagingRepository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends PagingRepository<Conversation>
{
    Message resultToMessage(ResultSet rs);

    List<UUID> getConversationUsers(UUID id);

    List<Message> getConversationMessages(UUID id);

    Optional<Message> findOneMessage(UUID id);
}
