package com.cookbookapp.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class UserData {

    public static boolean isLogged;
    public static int userId;
    public static List<String> meals;

    private int id;

    public UserData(int id, List<String> meals) {
        this.id = id;
        this.meals = meals;
        userId = id;
    }
}
