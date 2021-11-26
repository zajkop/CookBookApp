package com.cookbookapp.domain;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@SuppressWarnings({"unchecked", "ConstantConditions"})
@Getter
public class CustomJsonObject {
    private final Map<String, Object> nameValuePairs;

    public CustomJsonObject(Map<String, Object> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public Recipe mapObjectToRecipe() {
        double id = (Double) nameValuePairs.get("id");
        List<String> ingredients = ((Map<String, List<String>>) nameValuePairs.get("ingredients")).get("values");
        String imageUri = (String) nameValuePairs.get("imageUri");
        String name = (String) nameValuePairs.get("name");
        String category = (String) nameValuePairs.get("category");
        String description = (String) nameValuePairs.get("description");
        return new Recipe((int) id, name, description, category, imageUri, ingredients, getKcal());
    }

    @NonNull
    private Kcal getKcal() {
        Map<String, Object> kcal = ((Map<String, Map<String, Object>>)
                requireNonNull(nameValuePairs.get("kcal")))
                .get("nameValuePairs");
        String w = (String) requireNonNull(kcal).get("w");
        String value = (String) kcal.get("value");
        String t = (String) kcal.get("t");
        String b = (String) kcal.get("b");
        double kcalId = (Double) kcal.get("id");

        return new Kcal((int) kcalId, value, b, t, w);
    }
}
