package application;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "missions", path = "missions")
public interface MissionRepository extends PagingAndSortingRepository<Mission, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);
}
