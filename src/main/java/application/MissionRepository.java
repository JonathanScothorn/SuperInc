package application;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "missions", path = "missions")
public interface MissionRepository extends PagingAndSortingRepository<Mission, Long> {

}
