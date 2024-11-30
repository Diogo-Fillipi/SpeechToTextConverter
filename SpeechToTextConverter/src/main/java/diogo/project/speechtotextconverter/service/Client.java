package diogo.project.speechtotextconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;

@Service
public class Client {

    @Autowired
    WebClient webClient;


    public Mono<String> transcribe(MultipartFile file){
        String model = "whisper-1";
            if(file != null){
                return webClient.post()
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(BodyInserters.fromMultipartData("model", model)
                                .with("file", file.getResource()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(120))
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)));
            }
            return Mono.error(new FileNotFoundException("File not found"));

    }
}
