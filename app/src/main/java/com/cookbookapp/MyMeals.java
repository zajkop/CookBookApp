package com.cookbookapp;

import static java.lang.String.valueOf;

import android.content.Intent;
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

public class MyMeals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast);


        ListView listView = findViewById(R.id.breakfastListView);

        MealsAdapter adapter = ((ObjectWrapperForBinder) getIntent().getExtras().getBinder("adapter")).getMealsAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder adb = new AlertDialog.Builder(MyMeals.this);
            adb.setTitle("Usun posilek");
            adb.setMessage("Czy chcesz usunac ten posilek?");
            adb.setNegativeButton("Nie", null);
            adb.setPositiveButton("Tak", (dialog, which) -> {
                Recipe recipe = adapter.getRecipes().get(position);
                removeItem(recipe.getId());
                adapter.getRecipes().remove(position);
                adapter.notifyDataSetChanged();
                UserData.meals.remove(valueOf(recipe.getId()));
            });
            adb.show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    void removeItem(int recipeId) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", valueOf(UserData.userId));
        params.put("mealId", valueOf(recipeId));

        String requestUri = "http://10.0.2.2:8080/recipe/user/remove-meal";
        RequestQueue request = Volley.newRequestQueue(this);
        JSONObject jsonRequest = new JSONObject(params);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                requestUri, jsonRequest,
                response -> {
                    Log.i("Info", "Successfully removed meal from user");
                }, error -> Log.e("Error", "Request fails!"));
        request.add(objectRequest);
    }
}