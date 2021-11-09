package vn.binhduong.eventmgrdemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition (
        info = @Info(
                title = "Event Management Demo API",
                description = "API Definitions of Event Management Demo API",
                version = "0.0.1"
        )
)
public class EventMgrDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventMgrDemoApplication.class, args);
    }

}
