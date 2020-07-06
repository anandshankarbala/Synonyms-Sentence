package com.planr.synonymsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SynonymsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SynonymsServiceApplication.class, args);
    }
    
    @Bean
    RestTemplate getRestTemplate() {
    	return new RestTemplate();
    }

    @Bean
    WebClient getWebClient(){
        return WebClient.create("https://api.datamuse.com/");
    }
}
