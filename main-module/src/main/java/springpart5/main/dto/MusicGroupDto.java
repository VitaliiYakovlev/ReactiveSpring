package springpart5.main.dto;

import lombok.Value;

import java.util.List;

@Value
public class MusicGroupDto {
    String name;
    int creationYear;
    int decayYear;
    int availableAlbums;
    List<String> genres;
}