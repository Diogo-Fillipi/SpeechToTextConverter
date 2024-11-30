# SpeechToText API
This Spring application consumes the whisper-1 model from OpenAi. It has a front-end where you can upload a MP3 file up to 25MB, send it to the back-end where it will be processed and then returned to the front-end, after that, the JavaScript will create a TXT file and automatically download it on your machine.

## Code Explanation:
We have below the HttpUtils class, where we create our WebClient so we can consume external API's
``` Java
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

}]
```
The Client class where we configure our request to the API
``` Java
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
```

Finally we have the SpeechController class, where we configure our REST API end-points to the front-end
``` Java
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
```
## API instructions 
- 1 - Create an account on OpenAi, you can do it on: https://platform.openai.com/
- 2 - Create a SpeechToText project and generate your API Key, you can do it on: https://platform.openai.com/settings/organization/api-keys
- 3 - Put the API Key on:  ```spring.ai.openai.api-key=[YourApiKey] ``` <br> <br>
### Now you should be able to use the API!

