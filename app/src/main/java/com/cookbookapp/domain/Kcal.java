package com.cookbookapp.domain;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Kcal {

    private final int id;
    private final String value;
    private final String b;
    private final String t;
    private final String w;

    @NonNull
    @Override
    public String toString() {
        return "Kalorie \n" + value + ", białko: " + b + ", tłuszcze: " + t + ", węglowodany: " + w;
    }
}
