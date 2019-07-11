package com.planr.synonymsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.stream.Stream;

@Service
public class ImperativeService {

    @Autowired
    RestTemplate restTemplate; 

    public String getSynonym(String word) {
    	SynonymResult[] results = restTemplate.getForObject("https://api.datamuse.com/words?rel_syn="+word, SynonymResult[].class);
    	return getBestSynonym(results, word);
    }

    public String getSynonymSentence(String sentence) {
        return Stream.of(sentence.split(" "))
        		.map(word -> {
        			System.out.println("word getSynonymSentence "+word);
        			return getSynonym(word);
        		})
        		.reduce("", (w1,w2) -> w1+" "+w2);
    }

    private String getBestSynonym(SynonymResult[] synonymResultList, String word) {
        return Stream.of(synonymResultList)
                .filter(r -> !r.getWord().equals(word) && !r.getWord().equals(""))
                .max(Comparator.comparing(SynonymResult::getScore))
                .map(SynonymResult::getWord)
                .orElse(word); //if no synonym found we return the word
    }
}