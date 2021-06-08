package springpart5.main.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import springpart5.main.constants.ErrorConstants;
import springpart5.main.dto.EditMusicAlbumDto;
import springpart5.main.dto.ErrorDto;
import springpart5.main.dto.MusicAlbumDto;
import springpart5.main.exception.UnprocessableEntityException;
import springpart5.main.service.IMusicAlbumService;
import springpart5.main.service.ModelMapper;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import static springpart5.main.constants.ErrorConstants.Fields;

@RestController
public class MusicController {

    private final IMusicAlbumService musicAlbumService;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public MusicController(IMusicAlbumService musicAlbumService, Gson gson, ModelMapper modelMapper) {
        this.musicAlbumService = musicAlbumService;
        this.gson = gson;
        this.modelMapper = modelMapper;
    }

    @PostMapping(path = "/add-new-album", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> createMusicAlbum(@Valid @RequestBody Mono<MusicAlbumDto> musicAlbum) {
        return writeProcessing(musicAlbum, musicAlbumService::addMusicAlbum);
    }

    @PostMapping(path = "/edit-music-album", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> editMusicAlbum(@Valid @RequestBody Mono<EditMusicAlbumDto> musicAlbum) {
        return writeProcessing(musicAlbum, musicAlbumService::editMusicAlbum);
    }

    @GetMapping(path = "/get-music-album", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getMusicAlbum(@RequestParam(name = "name") Set<String> musicAlbumNames) {
        if (musicAlbumNames.stream().allMatch(StringUtils::hasText)) {
            return musicAlbumService.getMusicAlbum(musicAlbumNames)
                    .map(gson::toJson)
                    .map(ResponseEntity::ok);
        }
        return Mono.just(Collections.singleton(
                new ErrorDto(Fields.REQUEST_PARAMS, null, ErrorConstants.Msgs.CONTAINS_NULL_OR_EMPTY)))
                .map(gson::toJson)
                .map(errorText -> ResponseEntity.badRequest().body(errorText));
    }

    private <T> Mono<ResponseEntity<String>> writeProcessing(Mono<T> musicAlbum,
                                                             Function<? super T, ? extends Mono<Void>> transformer) {
        return musicAlbum
                .flatMap(transformer)
                .thenReturn(new ResponseEntity<String>(HttpStatus.OK))
                .onErrorResume(WebExchangeBindException.class,
                        e -> Mono.just(new ResponseEntity<>(transformBindingResult(e.getBindingResult()), HttpStatus.BAD_REQUEST)))
                .onErrorResume(UnprocessableEntityException.class,
                        e -> Mono.just(new ResponseEntity<>(gson.toJson(e.getErrors()), HttpStatus.BAD_REQUEST)));
    }

    private String transformBindingResult(BindingResult bindingResult) {
        Set<ErrorDto> errors = modelMapper.map(bindingResult);
        return gson.toJson(errors);
    }
}
