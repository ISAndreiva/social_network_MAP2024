package internal.andreiva.socialnetwork.repository;

import internal.andreiva.socialnetwork.domain.Entity;
import internal.andreiva.socialnetwork.utils.Page;
import internal.andreiva.socialnetwork.utils.Pageable;

public interface PagingRepository<E extends Entity> extends Repository<E> {

    Page<E> findAllOnPage(Pageable pageable);
}