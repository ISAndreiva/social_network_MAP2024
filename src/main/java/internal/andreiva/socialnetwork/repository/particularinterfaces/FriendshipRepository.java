package internal.andreiva.socialnetwork.repository.particularinterfaces;

import internal.andreiva.socialnetwork.domain.Friendship;
import internal.andreiva.socialnetwork.repository.PagingRepository;
import internal.andreiva.socialnetwork.utils.Page;
import internal.andreiva.socialnetwork.utils.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends PagingRepository<Friendship>
{
    List<UUID> getFriendships(UUID userId, String status);

    Optional<Friendship> getFriendship(UUID friend1, UUID friend2);

    List<UUID> getReceivedFriendRequests(UUID userId);

    Page<UUID> getFriendships(UUID userId, String status, Pageable pageable);

    Page<UUID> getReceivedFriendRequests(UUID userId, Pageable pageable);
}
