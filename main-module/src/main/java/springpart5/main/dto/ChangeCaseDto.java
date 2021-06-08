package springpart5.main.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class ChangeCaseDto {

    @NotNull(message = "shall not be null")
    String toUpperCase;
    @NotNull(message = "shall not be null")
    String toLowerCase;
}
