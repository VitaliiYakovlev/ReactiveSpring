package springpart5.main.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import springpart5.main.entity.Genre;

@Repository
public interface IGenreRepo extends ReactiveCrudRepository<Genre, Long> {
    Mono<Genre> findByNameIgnoreCase(String name);
}
