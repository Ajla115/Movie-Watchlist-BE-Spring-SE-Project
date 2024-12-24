package ba.edu.ibu.movieswatchlist.rest.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Movies Watchlist",
                version = "1.0.0",
                description = "Movies Watchlist Backend Application",
                contact = @Contact(name = "Web Engineering", email = "ajla.korman@ibu.edu.ba")
        ),
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)

public class SwaggerConfiguration {
}
