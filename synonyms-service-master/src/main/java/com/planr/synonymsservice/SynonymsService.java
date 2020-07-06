package com.planr.synonymsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SynonymsService {
    @Autowired
    private SynonymsRepo synonymsRepo;

    public Mono<String> getSynonym(String word) {
        Mono<SynonymResult[]> synonymResultsMono = synonymsRepo.getSynonymsForWord(word);   // TODO change to Flux
        return synonymResultsMono.map(synonymResultList ->{
        	System.out.println("getSynonym for word "+word+" synonymResultList "+ Arrays.toString(synonymResultList));
        	String bestMatch =  getBestSynonym(synonymResultList, word);
            System.out.println("the best Synonym for word "+word+" is "+ bestMatch);
        	return bestMatch;
        });
    }

    private SynonymResult calculateMaxValueSynonym(SynonymResult synonymResult, SynonymResult synonymResult2) {
        return synonymResult.getScore() > synonymResult2.getScore() ? synonymResult : synonymResult2;
    }

    private String getMaxValueSynomyn(String word, SynonymResult maxScoreResult) {
        return (!maxScoreResult.getWord().equals(word) && !maxScoreResult.getWord().equals("") ) ? maxScoreResult.getWord() : word;
    }

    public Mono<String> getSynonymSentence(String sentence) {
        return Stream.of(sentence.split(" "))
        		.map(word -> getSynonym(word))
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