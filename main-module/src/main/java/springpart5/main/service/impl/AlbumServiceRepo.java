package springpart5.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springpart5.main.dto.EditMusicAlbumDto;
import springpart5.main.dto.ErrorDto;
import springpart5.main.dto.MusicAlbumDto;
import springpart5.main.entity.Album;
import springpart5.main.entity.Genre;
import springpart5.main.entity.MusicGroup;
import springpart5.main.exception.UnprocessableEntityException;
import springpart5.main.repo.IAlbumRepo;
import springpart5.main.repo.IGenreRepo;
import springpart5.main.repo.IMusicGroupRepo;
import springpart5.main.service.IMusicAlbumService;

import java.util.Set;
import java.util.stream.Collectors;

import static springpart5.main.constants.ErrorConstants.Fields;
import static springpart5.main.constants.ErrorConstants.Msgs;

@Service
public class AlbumServiceRepo implements IMusicAlbumService {

    private final IAlbumRepo albumRepo;
    private final IGenreRepo genreRepo;
    private final IMusicGroupRepo musicGroupRepo;

    @Autowired
    public AlbumServiceRepo(IAlbumRepo albumRepo, IGenreRepo genreRepo, IMusicGroupRepo musicGroupRepo) {
        this.albumRepo = albumRepo;
        this.genreRepo = genreRepo;
        this.musicGroupRepo = musicGroupRepo;
    }

    @Override
    @Transactional
    public Mono<Void> addMusicAlbum(MusicAlbumDto album) {
        Mono<Album> albumMono = Mono.zip(
                findGenre(album.getGenre()),
                findMusicGroup(album.getMusicGroup()),
                (genre, group) -> new Album(album.getName(), album.getReleaseYear(), group.getId(), genre.getId())
        );
        return saveAlbum(albumMono, album.getName());
    }

    @Override
    @Transactional
    public Mono<Void> editMusicAlbum(EditMusicAlbumDto musicAlbum) {
        Mono<Album> albumToUpdate = findMusicGroup(musicAlbum.getOldMusicGroup())
                .flatMap(group -> findAlbum(musicAlbum.getOldName(), group.getId()));
        if (musicAlbum.getMusicGroup() != null) {
            albumToUpdate.zipWith(
                    findMusicGroup(musicAlbum.getMusicGroup()),
                    Album::withMusicGroup
            );
        }
        if (musicAlbum.getGenre() != null) {
            albumToUpdate.zipWith(
                    findGenre(musicAlbum.getGenre()),
                    Album::withGenre
            );
        }
        Mono<Album> updatedAlbum = albumToUpdate
                .map(album -> updateAlbum(album, musicAlbum));
        return saveAlbum(updatedAlbum, musicAlbum.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Set<MusicAlbumDto>> getMusicAlbum(Set<String> names) {
        Set<String> upperCaseNames = names.stream().map(String::toUpperCase).collect(Collectors.toSet());
        Flux<Album> albums = albumRepo.findByNameInIgnoreCase(upperCaseNames);
        return albums
                .flatMap(album -> Flux.zip(
                        Mono.just(album),
                        genreRepo.findById(album.getGenreId()),
                        Album::withGenre))
                .flatMap(album -> Flux.zip(
                        Mono.just(album),
                        musicGroupRepo.findById(album.getMusicGroupId()),
                        Album::withMusicGroup
                ))
                .map(album -> new MusicAlbumDto(
                        album.getName(), album.getYearRelease(), album.getGenre().getName(), album.getMusicGroup().getName()
                ))
                .collect(Collectors.toSet());
    }

    private Mono<Genre> findGenre(String genreName) {
        return genreRepo.findByNameIgnoreCase(genreName)
                .switchIfEmpty(Mono.error(
                        new UnprocessableEntityException(new ErrorDto(Fields.GENRE, genreName, Msgs.NOT_FOUND))
                ));
    }

    private Mono<MusicGroup> findMusicGroup(String musicGroupName) {
        return musicGroupRepo.findByNameIgnoreCase(musicGroupName)
                .switchIfEmpty(Mono.error(
                        new UnprocessableEntityException(new ErrorDto(Fields.MUSIC_GROUP, musicGroupName, Msgs.NOT_FOUND))
                ));
    }

    private Mono<Album> findAlbum(String name, long groupId) {
        return albumRepo.findByNameIgnoreCaseAndMusicGroupId(name, groupId)
                .switchIfEmpty(Mono.error(
                        new UnprocessableEntityException(new ErrorDto(Fields.ALBUM, name, Msgs.NOT_FOUND))
                ));
    }

    private Mono<Void> saveAlbum(Mono<Album> album, String albumName) {
        return album.flatMap(albumRepo::save)
                .onErrorMap(DataIntegrityViolationException.class,
                        e -> new UnprocessableEntityException(new ErrorDto(Fields.ALBUM, albumName, Msgs.ALREADY_EXISTS))
                )
                .then();
    }

    private Album updateAlbum(Album album, EditMusicAlbumDto editMusicAlbumDto) {
        if (album.getGenre() != null) {
            album.setGenreId(album.getGenre().getId());
        }
        if (album.getMusicGroup() != null) {
            album.setMusicGroupId(album.getMusicGroup().getId());
        }
        if (editMusicAlbumDto.getName() != null) {
            album.setName(editMusicAlbumDto.getName());
        }
        if (editMusicAlbumDto.getReleaseYear() != null) {
            album.setYearRelease(editMusicAlbumDto.getReleaseYear());
        }
        return album;
    }
}
