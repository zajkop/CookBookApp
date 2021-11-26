package com.cookbookapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Recipe {

    private final int id;
    private final String name;
    private final String description;
    private final String category;
    private final String imageUri;
    private final List<String> ingredients;
    private final Kcal kcal;
}
