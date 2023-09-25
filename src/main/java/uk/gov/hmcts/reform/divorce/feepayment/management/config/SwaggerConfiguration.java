package uk.gov.hmcts.reform.divorce.feepayment.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(value = "documentation.swagger.enabled", havingValue = "true")
public class SwaggerConfiguration implements WebMvcConfigurer {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Fees and Payments Service API")
                        .description("Service to interact and consolidate fees and payments api"));
    }
}

