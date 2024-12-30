package ba.edu.ibu.movieswatchlist.rest.configuration;

import ba.edu.ibu.movieswatchlist.api.impl.infobip.InfobipEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class EmailConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public InfobipEmailService infobipEmailService() {
        return new InfobipEmailService();
    }
}
