package id.kai.eraport.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.List;

@Configuration
public class SwaggerConfig {
    private static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${springdoc.swagger-ui.path:/swagger-ui/index.html}")
    private String swaggerPath;

    @Value("${springdoc.api-docs.path:/v3/api-docs}")
    private String apiDocsPath;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName.toUpperCase() + " API Documentation")
                        .version(appVersion))
                .servers(List.of(
                        new Server().url(contextPath + "/").description("Base server (" + activeProfile + " profile)")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Enter your JWT token here: Bearer <token>")
                )
        );
    }

    @PostConstruct
    public void logSwaggerInfo() {
        log.info("\n----------------------------------------------------------");
        log.info("📘 Swagger OpenAPI initialized for application: {}", appName);
        log.info("🏷️ Version                : {}", appVersion);
        log.info("🚀 Swagger UI available   : http://localhost:{}{}", serverPort, swaggerPath);
        log.info("📄 OpenAPI JSON available : http://localhost:{}{}", serverPort, apiDocsPath);
        log.info("----------------------------------------------------------");
    }
}
