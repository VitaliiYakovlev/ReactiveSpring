package springpart5.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketConfiguration {

    @Bean
    public RSocketRequester requester(RSocketRequester.Builder builder,
                                   @Value("${parmeter.rsocket.host}") String host,
                                   @Value("${parmeter.rsocket.port}") int port) {
        return builder
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp(host, port);
    }
}
