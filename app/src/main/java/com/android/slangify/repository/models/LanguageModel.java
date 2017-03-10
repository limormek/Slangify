package com.android.slangify.repository.models;

/**
 * Created by bettykin on 09/03/2017.
 */

public class LanguageModel {

    private String name;
    private int id;

    public LanguageModel(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
