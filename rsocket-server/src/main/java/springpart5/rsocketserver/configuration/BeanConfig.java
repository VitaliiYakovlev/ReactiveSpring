package springpart5.rsocketserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springpart5.utils.RandomStringGenerator;

@Configuration
public class BeanConfig {

    @Bean
    public RandomStringGenerator randomStringGenerator() {
        return new RandomStringGenerator();
    }
}
