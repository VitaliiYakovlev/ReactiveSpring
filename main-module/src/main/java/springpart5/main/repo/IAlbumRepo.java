package springpart5.main.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springpart5.main.entity.Album;

import java.util.Set;

@Repository
public interface IAlbumRepo extends ReactiveCrudRepository<Album, Long> {
    Mono<Album> findByNameIgnoreCaseAndMusicGroupId(String name, long musicGroupId);
    Flux<Album> findByNameInIgnoreCase(Set<String> name);
}
