package com.cookbookapp;

import android.os.Binder;

public class ObjectWrapperForBinder extends Binder {

    private final RecipeAdapter adapter;
    private final MealsAdapter mealsAdapter;

    public ObjectWrapperForBinder(RecipeAdapter adapter, MealsAdapter mealsAdapter) {
        this.adapter = adapter;
        this.mealsAdapter = mealsAdapter;
    }

    public RecipeAdapter getData() {
        return adapter;
    }

    public MealsAdapter getMealsAdapter() {
        return mealsAdapter;
    }
}
