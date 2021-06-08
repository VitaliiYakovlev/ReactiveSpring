package springpart5.main.service;

import reactor.core.publisher.Mono;
import springpart5.main.dto.MusicAlbumDto;
import springpart5.main.dto.EditMusicAlbumDto;
import java.util.Set;

public interface IMusicAlbumService {
    Mono<Void> addMusicAlbum(MusicAlbumDto musicAlbum);
    Mono<Void> editMusicAlbum(EditMusicAlbumDto musicAlbum);
    Mono<Set<MusicAlbumDto>> getMusicAlbum(Set<String> names);
}
