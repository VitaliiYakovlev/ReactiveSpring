package springpart5.main.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import springpart5.main.entity.MusicGroup;

@Repository
public interface IMusicGroupRepo extends ReactiveCrudRepository<MusicGroup, Long> {
    Mono<MusicGroup> findByNameIgnoreCase(String name);
}
