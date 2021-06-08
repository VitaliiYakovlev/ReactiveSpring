package springpart5.rsocketserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springpart5.utils.RandomStringGenerator;
import springpart5.utils.dto.MessageDto;

import java.time.Duration;

@Slf4j
@Controller
public class RSocketController {

    private static final String MESSAGE_FORMAT = "index is %d, message is %s";

    private final RandomStringGenerator stringGenerator;

    @Autowired
    public RSocketController(RandomStringGenerator stringGenerator) {
        this.stringGenerator = stringGenerator;
    }

    @MessageMapping("request-response")
    Mono<MessageDto> requestResponse(final MessageDto messageDto) {
        log.info("Received request-response message {}", messageDto);
        return Mono.just(new MessageDto("You said: " + messageDto.getMessage()));
    }

    @MessageMapping("request-stream")
    Flux<MessageDto> requestStream() {
        return Flux.interval(Duration.ofSeconds(5))
                .map(index -> new MessageDto(String.format(
                        MESSAGE_FORMAT, index, stringGenerator.generateRandomString()
                )));
    }

    @MessageMapping("fire-and-forget")
    void fireAndForget(final MessageDto messageDto) {
        log.info("Received fire-and-forget message {}", messageDto);
    }
}
