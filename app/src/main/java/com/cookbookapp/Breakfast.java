package com.cookbookapp;

import static java.lang.String.valueOf;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cookbookapp.domain.Recipe;
import com.cookbookapp.domain.UserData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Breakfast extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);

        listView = findViewById(R.id.breakfastListView);

        RecipeAdapter adapter = ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("adapter")).getData();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (!UserData.isLogged) {
                new AlertDialog.Builder(this).setMessage("Zaloguj sie aby dodawac posilki do panelu!").show();
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(Breakfast.this);
                adb.setTitle("Dodaj posilek");
                adb.setMessage("Czy chcesz dodac ten posilek?");
                adb.setNegativeButton("Nie", null);
                adb.setPositiveButton("Tak", (dialog, which) -> {
                    Recipe recipe = adapter.getRecipes().get(position);
                    if (UserData.meals.contains(valueOf(recipe.getId()))) {
                        new AlertDialog.Builder(this).setMessage("Posilek wystepuje juz w twoim panelu!").show();
                    } else {
                        addItem(recipe.getId());
                        UserData.meals.add(valueOf(recipe.getId()));
                        new AlertDialog.Builder(this).setMessage("Posilek dodany do twojego panelu!").show();
                    }
                });
                adb.show();
            }
        });
    }

    void addItem(int recipeId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", valueOf(UserData.userId));
        params.put("mealId", valueOf(recipeId));

        String requestUri = "http://10.0.2.2:8080/recipe/user/add-meal";
        RequestQueue request = Volley.newRequestQueue(this);
        JSONObject jsonRequest = new JSONObject(params);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                requestUri, jsonRequest,
                response -> {
                    Log.i("Info", "Successfully added meal from user");
                }, error -> Log.e("Error", "Request fails!"));
        request.add(objectRequest);
    }
}