package ba.edu.ibu.movieswatchlist.api.impl.openai;

import ba.edu.ibu.movieswatchlist.core.api.genresuggester.GenreSuggester;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

@Service
public class OpenAISuggester implements GenreSuggester {
    private final OpenAiService openAiService;

    public OpenAISuggester(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }
    @Override
    public String suggestGenre(String title) {
        String prompt = "Suggest a movie genre for the title: " + title;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct") // Use a suitable model
                .maxTokens(10)
                .build();

        try {
            return openAiService.createCompletion(completionRequest)
                    .getChoices()
                    .get(0)
                    .getText()
                    .trim();

        } catch (Exception e) {
            System.err.println("OpenAI API error: " + e.getMessage());
            return "Error: OpenAI API is overwhelmed at the moment. Please try again later.";
        }
    }


    @Override
    public String suggestMovies(String genre) {
        String prompt = "Suggest at least one good movie from this genre: " + genre;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(10)
                .build();

        try {
            return openAiService.createCompletion(completionRequest)
                    .getChoices()
                    .get(0)
                    .getText()
                    .trim();

        } catch (Exception e) {
            System.err.println("OpenAI API error: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve movie suggestions", e);
        }
    }
}
