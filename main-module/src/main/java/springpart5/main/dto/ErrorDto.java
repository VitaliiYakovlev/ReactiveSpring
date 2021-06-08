package springpart5.main.dto;

import lombok.Value;

@Value
public class ErrorDto {

    String field;
    String value;
    String message;
}
