package diogo.project.speechtotextconverter.controller;


import diogo.project.speechtotextconverter.service.Client;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/api")
public class SpeechController {

    Client client;

    public SpeechController(Client client){
        this.client = client;
    }

    @PostMapping(value = "/transcribe", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> transcribeAudio(@RequestParam("file") MultipartFile file){

        return client.transcribe(file);

    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste(){
        String response = "It's working";
        return ResponseEntity.ok(response);
    }

}
