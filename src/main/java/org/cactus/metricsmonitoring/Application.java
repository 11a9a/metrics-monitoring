package org.cactus.metricsmonitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @RestController
    static class HelloController {
        private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);
        private final RestTemplate restTemplate;

        HelloController(RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
        }

        @GetMapping("/hello")
        public String hello() {
            LOGGER.info("---------Hello method started!               ---------");
            LOGGER.error("---------Hello method started, id is missing!---------");
            ResponseEntity<String> responseEntity = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello!!!", String.class);
            return responseEntity.getBody();
        }

        @GetMapping("/exception")
        public String exception() {
            throw new IllegalArgumentException("This id is invalid");
        }

        @ExceptionHandler(value = {IllegalArgumentException.class})
        protected ResponseEntity<String> handleConflict(IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
