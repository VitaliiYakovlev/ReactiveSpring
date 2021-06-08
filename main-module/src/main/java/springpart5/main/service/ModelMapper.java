package springpart5.main.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import springpart5.main.dto.ErrorDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModelMapper {

    public Set<ErrorDto> map(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(error -> (FieldError) error)
                .map(error -> new ErrorDto(error.getField(), String.valueOf(error.getRejectedValue()), error.getDefaultMessage()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
