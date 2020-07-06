package com.planr.synonymsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(SynonymsService.class)
public class SynonymsServiceTest {

    @MockBean
    private SynonymsRepo synonymsRepo;

    @Autowired
    private SynonymsService synonymsService;

    @Test
    public void getSynonymForWord() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File synonymResultFile = ResourceUtils.getFile("src/test/resources/sampleSynonymResult.json");
        String synonymResult = String.valueOf(Files.readAllBytes(synonymResultFile.toPath()));
        SynonymResult[] synonymResults = mapper.readValue(synonymResultFile, SynonymResult[].class);
        when(synonymsRepo.getSynonymsForWord(any())).thenReturn(Mono.just(synonymResults));
        Mono<String> synonymForWord = synonymsService.getSynonym("One");
        StepVerifier.create(synonymForWord).assertNext(synonymWord -> Assert.assertNotNull(synonymWord)).expectComplete().verify();
    }

    @Test
    public void getSynonymForSentence() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        File synonymResultFile = ResourceUtils.getFile("src/test/resources/sampleSynonymResult.json");
        String synonymResult = String.valueOf(Files.readAllBytes(synonymResultFile.toPath()));
        SynonymResult[] synonymResults = mapper.readValue(synonymResultFile, SynonymResult[].class);
        when(synonymsRepo.getSynonymsForWord(any())).thenReturn(Mono.just(synonymResults));
        Mono<String> synonymForWord = synonymsService.getSynonymSentence("All is well");
        StepVerifier.create(synonymForWord).assertNext(synonymSentence -> {
            System.out.println("synonymSentence -> "+synonymSentence);
            Assert.assertNotNull(synonymSentence);
        }).expectComplete().verify();
    }
}
