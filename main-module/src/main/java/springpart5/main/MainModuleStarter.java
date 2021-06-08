package springpart5.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Hooks;

@EnableScheduling
@SpringBootApplication
public class MainModuleStarter {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(MainModuleStarter.class, args);
    }
}
