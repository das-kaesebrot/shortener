package eu.kaesebrot.dev.shortener.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition(
        info = @Info(title = "Shortener API", version = "1.0",
                description = "REST API for managing short links"
        )
)
public class ApiInfo {
}
