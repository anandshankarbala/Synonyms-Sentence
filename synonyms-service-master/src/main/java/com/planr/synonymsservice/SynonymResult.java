package com.planr.synonymsservice;

public class SynonymResult {
    private String word;
    private int score;

    public String getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "SynonymResult{" +
                "word='" + word + '\'' +
                ", score=" + score +
                '}';
    }
}
