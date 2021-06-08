package springpart5.main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("albums")
public class Album {

    @Id
    private Long id;

    @Column
    private String name;

    @Column("year_release")
    private short yearRelease;

    @Column("music_group_id")
    private long musicGroupId;

    @Column("genre_id")
    private long genreId;

    @Transient
    private MusicGroup musicGroup;

    @Transient
    private Genre genre;

    public Album withGenre(Genre genre) {
        this.genre = genre;
        return this;
    }

    public Album withMusicGroup(MusicGroup musicGroup) {
        this.musicGroup = musicGroup;
        return this;
    }

    public Album(String name, short yearRelease, long musicGroupId, long genreId) {
        this.name = name;
        this.yearRelease = yearRelease;
        this.musicGroupId = musicGroupId;
        this.genreId = genreId;
    }
}
