package diogo.project.speechtotextconverter.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class HttpUtils {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Value("${spring.ai.openai.base-url}")
    private String openAiBaseUrl;

    HttpClient httpClient = HttpClient.create().wiretap(true);

    @Bean
    public WebClient webClient(WebClient.Builder builder){

        return builder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofSeconds(120))))
                .baseUrl(openAiBaseUrl)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .build();
    }

}
