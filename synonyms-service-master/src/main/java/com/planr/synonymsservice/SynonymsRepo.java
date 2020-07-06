package com.planr.synonymsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Repository
public class SynonymsRepo {

    @Autowired
    private WebClient client;
    public Mono<SynonymResult[]> getSynonymsForWord(String word){
            return client.get().uri("words?rel_syn=" + word).retrieve().bodyToMono(SynonymResult[].class);
//                    .delayElements(Duration.ofMillis(100));
    }
}
