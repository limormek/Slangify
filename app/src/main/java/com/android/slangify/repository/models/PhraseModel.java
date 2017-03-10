package com.android.slangify.repository.models;

/**
 * Created by bettykin on 09/03/2017.
 */

public class PhraseModel {
    private String text;
    private String extra;
    private int score;
    private String translation;

    public PhraseModel() {
    }

    public PhraseModel(String text, String extra, int score, String translation) {
        this.text = text;
        this.extra = extra;
        this.score = score;
        this.translation = translation;
    }


    public String getText() {
        return text;
    }

    public String getExtra() {
        return extra;
    }

    public int getScore() {
        return score;
    }

    public String getTranslation() {
        return translation;
    }
}
