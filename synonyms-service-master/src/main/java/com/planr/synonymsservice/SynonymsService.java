package com.planr.synonymsservice;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class SynonymsService {

    private WebClient client = WebClient.create("https://api.datamuse.com/");

    public Mono<String> getSynonym(String word) {
        final Mono<SynonymResult[]> synonymResultsMono = client.get().uri("words?rel_syn=" + word).retrieve().bodyToMono(SynonymResult[].class)
                .delayElement(Duration.ofSeconds(1)); //introducing small delay to show concurrency
//        return synonymResultsMono.map(synonymResultList -> getBestSynonym(synonymResultList, word));	//TODO original code
        return synonymResultsMono.map(synonymResultList ->{
        	System.out.println("getSynonym for word "+word+" synonymResultList "+synonymResultList);
        	return getBestSynonym(synonymResultList, word);
        });
    }

    public Mono<String> getSynonymSentence(String sentence) {
        return Stream.of(sentence.split(" "))
//                .map(this::getSynonym) //here we have a stream of Mono // TODO original code
        		.map(word -> {
        			System.out.println("word getSynonymSentence "+word);
        			return getSynonym(word);
        		})
//                .reduce(Mono.just(""), (m1, m2) -> m1.zipWith(m2, (w1, w2) -> w1 + " " + w2)); //we combine the mono with zipWith	// TODO original code
        		.reduce(Mono.just(""), (m1, m2) -> m1.zipWith(m2, (w1, w2) -> {
                	System.out.println("w1 "+w1+" w2 "+w2);
                	return w1 + " " + w2;
                })); 
    }

    private String getBestSynonym(SynonymResult[] synonymResultList, String word) {
        return Stream.of(synonymResultList)
                .filter(r -> !r.getWord().equals(word) && !r.getWord().equals(""))
                .max(Comparator.comparing(SynonymResult::getScore))
                .map(SynonymResult::getWord)
                .orElse(word); //if no synonym found we return the word
    }
}