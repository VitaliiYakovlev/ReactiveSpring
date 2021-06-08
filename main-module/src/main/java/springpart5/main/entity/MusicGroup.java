package springpart5.main.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("music_groups")
@NoArgsConstructor
public class MusicGroup {

    @Id
    private Long id;

    @Column
    private String name;

    @Column("year_creation")
    private short yearCreation;

    @Column("year_decay")
    private Short yearDecay;
}
