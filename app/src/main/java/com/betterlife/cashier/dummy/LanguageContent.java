package com.betterlife.cashier.dummy;

import java.util.ArrayList;

public class LanguageContent {
    private String id;
    private String label;

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    private LanguageContent(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public static ArrayList<LanguageContent> items() {
        ArrayList<LanguageContent> data = new ArrayList<>();
        data.add(new LanguageContent("en", "English"));
        data.add(new LanguageContent("in", "Bahasa Indonesia"));

        return data;
    }
}
