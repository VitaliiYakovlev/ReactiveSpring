package springpart5.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String message;
    private long created = Instant.now().getEpochSecond();

    public MessageDto(String message) {
        this.message = message;
    }
}
