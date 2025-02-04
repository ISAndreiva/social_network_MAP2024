package internal.andreiva.socialnetwork.repository.particularinterfaces;

import internal.andreiva.socialnetwork.domain.User;
import internal.andreiva.socialnetwork.repository.PagingRepository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends PagingRepository<User>
{
    Optional<User> findOne(String username);

    void saveImage(UUID userId, ByteArrayInputStream imageStream);

    InputStream getImage(UUID userID);
}
