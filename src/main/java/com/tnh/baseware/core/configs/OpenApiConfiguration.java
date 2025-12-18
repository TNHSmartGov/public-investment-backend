package com.tnh.baseware.core.configs;

import com.tnh.baseware.core.components.CustomRequiredFieldConverter;
import com.tnh.baseware.core.properties.OpenApiProperties;
import com.tnh.baseware.core.utils.LogStyleHelper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@OpenAPIDefinition(security = @SecurityRequirement(name = "bearerAuth"))
@RequiredArgsConstructor
public class OpenApiConfiguration {

    private final OpenApiProperties openApiProperties;
    private final CustomRequiredFieldConverter customRequiredFieldConverter;

    @PostConstruct
    public void registerCustomConverter() {
        log.debug(LogStyleHelper.debug("Registering custom field converter for OpenAPI"));
        ModelConverters.getInstance().addConverter(customRequiredFieldConverter);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        log.debug(LogStyleHelper.debug("Building custom OpenAPI configuration"));

        var securityName = openApiProperties.getSecurity().getName();

        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers())
                .externalDocs(apiExternalDocs())
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                        .addList(securityName))
                .components(new Components()
                        .addSecuritySchemes(securityName, securityScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title(openApiProperties.getInfo().getTitle())
                .description(openApiProperties.getInfo().getDescription())
                .version(openApiProperties.getInfo().getVersion())
                .termsOfService(openApiProperties.getInfo().getTermsOfService())
                .contact(new Contact()
                        .name(openApiProperties.getContact().getName())
                        .email(openApiProperties.getContact().getEmail())
                        .url(openApiProperties.getContact().getUrl()))
                .license(new License()
                        .name(openApiProperties.getLicense().getName())
                        .url(openApiProperties.getLicense().getUrl()));
    }

    private List<Server> apiServers() {
        return openApiProperties.getServers().stream()
                .map(serverInfo -> new Server()
                        .description(serverInfo.getDescription())
                        .url(serverInfo.getUrl()))
                .toList();
    }

    private ExternalDocumentation apiExternalDocs() {
        return new ExternalDocumentation()
                .description(openApiProperties.getExternalDocs().getDescription())
                .url(openApiProperties.getExternalDocs().getUrl());
    }

    private SecurityScheme securityScheme() {
        var security = openApiProperties.getSecurity();
        return new SecurityScheme()
                .name(security.getName())
                .description(security.getDescription())
                .scheme(security.getScheme())
                .type(SecurityScheme.Type.valueOf(security.getType().toUpperCase()))
                .bearerFormat(security.getBearerFormat())
                .in(SecurityScheme.In.valueOf(security.getIn().toUpperCase()));
    }
}