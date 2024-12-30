package ba.edu.ibu.movieswatchlist.rest.configuration;

import ba.edu.ibu.movieswatchlist.api.impl.openai.OpenAISuggester;
import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {
    @Value("${openai.secret}")
    private String apiSecret;

    @Bean
    public GenreSuggester genreSuggester() {
        return new OpenAISuggester(this.openAiService());
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(this.apiSecret);
    }

    @Bean
    public GenreSuggester movieSuggester() {
        return new OpenAISuggester(this.openAiService());
    }
}