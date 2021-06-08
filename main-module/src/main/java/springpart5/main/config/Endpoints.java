package springpart5.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import springpart5.main.dto.ChangeCaseDto;
import java.time.Duration;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Endpoints {

    @Bean
    public RouterFunction<ServerResponse> composedRoutes() {
        return route(POST("func/update-case"),
                req -> req.bodyToMono(ChangeCaseDto.class)
                        .map(dto -> new ChangeCaseDto(dto.getToUpperCase().toUpperCase(),
                                dto.getToLowerCase().toLowerCase()))
                        .flatMap(dto -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(dto)))
        .andRoute(GET("func/subscribe"),
                req -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(Flux.just("0", "1", "2", "3", "4", "5")
                                .delayElements(Duration.ofSeconds(2)), String.class));
    }
}
