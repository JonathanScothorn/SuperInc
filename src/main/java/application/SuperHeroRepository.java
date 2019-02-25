package application;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "heroes", path = "heroes")
public interface SuperHeroRepository extends PagingAndSortingRepository<SuperHero, Long> {

    @Override
    @RestResource(exported = false)
    void deleteById(Long id);

}
