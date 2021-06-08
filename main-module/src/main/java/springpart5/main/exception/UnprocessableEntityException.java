package springpart5.main.exception;

import lombok.Getter;
import springpart5.main.dto.ErrorDto;
import java.util.Collections;
import java.util.List;

public class UnprocessableEntityException extends RuntimeException {

    @Getter
    private final List<ErrorDto> errors;

    public UnprocessableEntityException(ErrorDto error) {
        this.errors = Collections.singletonList(error);
    }

}
