package com.planr.synonymsservice;

import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/imperative")
public class SynonymsImperativeController {

    @Autowired
    private ImperativeService synonymsService;

    @PostMapping(path = "/word")
    public String wordSynonym(@RequestBody String word) {
        return synonymsService.getSynonym(word);
    }

    @PostMapping(path = "/sentence")
    public String sentenceSynonym(@RequestBody String sentence) {
    	Instant start = Instant.now();
    	String result =  synonymsService.getSynonymSentence(sentence);
    	System.out.println("Total reactive : " + Duration.between(start, Instant.now()).toMillis() + " millis");
    	return result;
    }

}
