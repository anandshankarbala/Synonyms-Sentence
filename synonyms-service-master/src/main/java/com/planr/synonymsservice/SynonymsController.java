package com.planr.synonymsservice;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/synonyms")
public class SynonymsController {

    @Autowired
    private SynonymsService synonymsService;

    @PostMapping(path = "/word")
    public Mono<String> wordSynonym(@RequestBody String word) {
        return synonymsService.getSynonym(word);
    }

    @PostMapping(path = "/sentence")
    public Mono<String> sentenceSynonym(@RequestBody String sentence) {
    	Instant start = Instant.now();
        return synonymsService.getSynonymSentence(sentence).doOnTerminate(() -> System.out.println("Total reactive : " + Duration.between(start, Instant.now()).toMillis() + " millis"));
    }

}
