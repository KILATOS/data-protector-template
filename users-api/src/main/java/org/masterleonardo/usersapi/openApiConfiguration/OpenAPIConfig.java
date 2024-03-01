package org.masterleonardo.usersapi.openApiConfiguration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {




    @Bean
    public OpenAPI myOpenAPI() {
        /*Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");
*/
        Contact contact = new Contact();
        contact.setEmail("kilchitskij3@gmail.com");
        contact.setName("Kilchitskii K.A.");
        contact.setUrl("@kilatos_vn");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Users Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage users.")
                .license(mitLicense);

        return new OpenAPI().info(info);//.servers(List.of(devServer));
    }
}