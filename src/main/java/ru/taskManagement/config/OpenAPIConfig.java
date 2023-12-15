package ru.taskManagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI(){
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("URL сервера для среды разработки");

        Contact contact = new Contact();
        contact.setEmail("sobolevaalinaa@yandex.ru");
        contact.setName("Алина");

        Info info = new Info()
                .title("Task Management API")
                .version("1.0")
                .contact(contact)
                .description("Это API предоставляет эндпоинты для управления задачами");
        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
