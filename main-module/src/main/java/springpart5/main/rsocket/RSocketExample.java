package springpart5.main.rsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springpart5.utils.RandomStringGenerator;
import springpart5.utils.dto.MessageDto;
import javax.annotation.PostConstruct;

@Slf4j
@Component
public class RSocketExample {

    @Value("${parameter.rsocket.request-response}")
    private String roteRequestResponse;

    @Value("${parameter.rsocket.fire-and-forget}")
    private String roteFireAndForget;

    @Value("${parameter.rsocket.request-stream}")
    private String roteRequestStream;

    private final RSocketRequester client;
    private final RandomStringGenerator stringGenerator;

    @Autowired
    public RSocketExample(RSocketRequester client, RandomStringGenerator stringGenerator) {
        this.client = client;
        this.stringGenerator = stringGenerator;
    }

    @PostConstruct
    public void requestStream() {
        client.route(roteRequestStream)
                .retrieveFlux(MessageDto.class)
                .subscribe(stream -> log.info("Received stream message {}", stream));
    }
    @Scheduled(fixedRate = 7_000)
    public void requestResponse() {
        client.route(roteRequestResponse)
                .data(getMessageDto())
                .retrieveMono(MessageDto.class)
                .subscribe(response -> log.info("Request-response {}", response));
    }
    @Scheduled(fixedRate = 9_000)
    public void fireAndForget() {
        client.route(roteFireAndForget)
                .data(getMessageDto())
                .send()
                .subscribe();
    }
    private MessageDto getMessageDto() {
        String str = stringGenerator.generateRandomString();
        return new MessageDto(str);
    }
}
