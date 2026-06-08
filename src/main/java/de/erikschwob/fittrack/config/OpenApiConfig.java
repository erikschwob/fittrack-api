package de.erikschwob.fittrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fittrackOpenApi() {
        return new OpenAPI().info(new Info()
                .title("fittrack-api")
                .version("0.5.0")
                .description("""
                        A strength-training tracker. Log workouts and the sets within them,
                        track body metrics, and derive analytics: estimated one-rep-max personal
                        records and training volume per muscle group.""")
                .contact(new Contact().name("Erik Schwob").url("https://github.com/erikschwob"))
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }
}
