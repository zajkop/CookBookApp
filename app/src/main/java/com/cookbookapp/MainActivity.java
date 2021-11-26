package com.cookbookapp;

import static org.apache.commons.lang3.StringUtils.join;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cookbookapp.domain.CustomJsonObject;
import com.cookbookapp.domain.Recipe;
import com.cookbookapp.domain.UserData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {

    ImageView breakfast, lunch, dinner, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        breakfast = findViewById(R.id.breakfastListView);
        breakfast.setClickable(true);
        lunch = findViewById(R.id.lunch);
        lunch.setClickable(true);
        dinner = findViewById(R.id.dinner);
        dinner.setClickable(true);
        profile = findViewById(R.id.profile);
        profile.setClickable(true);

        breakfast.setOnClickListener(l -> {
            String requestUri = "http://10.0.2.2:8080/recipe/breakfast";
            RequestQueue request = Volley.newRequestQueue(this);
            JsonArrayRequest objectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    requestUri, null,
                    response -> {
                        List<Recipe> breakFastRecipes = new ArrayList<>();
                        Gson gson = new Gson();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                CustomJsonObject customJsonObject = gson.fromJson(gson.toJson(response.get(i)), CustomJsonObject.class);
                                breakFastRecipes.add(customJsonObject.mapObjectToRecipe());
                            }
                            RecipeAdapter breakfastAdapter = new RecipeAdapter(this, breakFastRecipes);

                            Bundle bundle = new Bundle();
                            bundle.putBinder("adapter", new ObjectWrapperForBinder(breakfastAdapter, null));
                            startActivity(new Intent(this, Breakfast.class).putExtras(bundle));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.e("Error", "Request fails!"));
            request.add(objectRequest);
        });

        lunch.setOnClickListener(l -> {
            String requestUri = "http://10.0.2.2:8080/recipe/lunch";
            RequestQueue request = Volley.newRequestQueue(this);
            JsonArrayRequest objectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    requestUri, null,
                    response -> {
                        List<Recipe> breakFastRecipes = new ArrayList<>();
                        Gson gson = new Gson();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                CustomJsonObject customJsonObject = gson.fromJson(gson.toJson(response.get(i)), CustomJsonObject.class);
                                breakFastRecipes.add(customJsonObject.mapObjectToRecipe());
                            }
                            RecipeAdapter breakfastAdapter = new RecipeAdapter(this, breakFastRecipes);

                            Bundle bundle = new Bundle();
                            bundle.putBinder("adapter", new ObjectWrapperForBinder(breakfastAdapter, null));
                            startActivity(new Intent(this, Breakfast.class).putExtras(bundle));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.e("Error", "Request fails!"));
            request.add(objectRequest);
        });

        dinner.setOnClickListener(l -> {
            String requestUri = "http://10.0.2.2:8080/recipe/dinner";
            RequestQueue request = Volley.newRequestQueue(this);
            JsonArrayRequest objectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    requestUri, null,
                    response -> {
                        List<Recipe> breakFastRecipes = new ArrayList<>();
                        Gson gson = new Gson();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                CustomJsonObject customJsonObject = gson.fromJson(gson.toJson(response.get(i)), CustomJsonObject.class);
                                breakFastRecipes.add(customJsonObject.mapObjectToRecipe());
                            }
                            RecipeAdapter breakfastAdapter = new RecipeAdapter(this, breakFastRecipes);

                            Bundle bundle = new Bundle();
                            bundle.putBinder("adapter", new ObjectWrapperForBinder(breakfastAdapter, null));
                            startActivity(new Intent(this, Breakfast.class).putExtras(bundle));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.e("Error", "Request fails!"));
            request.add(objectRequest);
        });

        profile.setOnClickListener(l -> {
            if (UserData.isLogged) {
                showUserRecipes();
            } else {
                startActivity(new Intent(this, Profile.class));
            }
        });
    }

    @SneakyThrows
    void showUserRecipes() {
        String requestUri = "http://10.0.2.2:8080/recipe/user/connected-meals";
        RequestQueue request = Volley.newRequestQueue(this);
        request.getCache().clear();
        JSONArray jsonArray = new JSONArray("[{id:'" + join(UserData.meals, ",") + "'}]");
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.POST,
                requestUri, jsonArray,
                response -> {
                    List<Recipe> meals = new ArrayList<>();
                    Gson gson = new Gson();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            CustomJsonObject customJsonObject = gson.fromJson(gson.toJson(response.get(i)), CustomJsonObject.class);
                            meals.add(customJsonObject.mapObjectToRecipe());
                        }
                        MealsAdapter mealsAdapter = new MealsAdapter(this, meals);

                        Bundle bundle = new Bundle();
                        bundle.putBinder("adapter", new ObjectWrapperForBinder(null, mealsAdapter));
                        startActivity(new Intent(this, MyMeals.class).putExtras(bundle));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e("Error", "Request fails!")

        );
        objectRequest.setShouldCache(false);
        request.add(objectRequest);
    }
}