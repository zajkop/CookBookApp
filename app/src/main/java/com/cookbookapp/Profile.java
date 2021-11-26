package com.cookbookapp;

import static org.apache.commons.lang3.StringUtils.join;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cookbookapp.domain.CustomJsonObject;
import com.cookbookapp.domain.Recipe;
import com.cookbookapp.domain.UserData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;

public class Profile extends AppCompatActivity {

    public static UserData userData;
    private EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(l -> {
            String loginValue = login.getText().toString();
            String passwordValue = password.getText().toString();
            if (loginValue.isEmpty() || passwordValue.isEmpty()) {
                new AlertDialog.Builder(this).setMessage("Wpisz login i hasło!").show();
            } else {

                Map<String, String> params = new HashMap<>();
                params.put("login", loginValue);
                params.put("password", passwordValue);

                String requestUri = "http://10.0.2.2:8080/recipe/login";
                RequestQueue request = Volley.newRequestQueue(this);
                JSONObject jsonRequest = new JSONObject(params);
                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        requestUri, jsonRequest,
                        response -> {
                            try {
                                int id = (int) response.get("id");
                                JSONArray array = (JSONArray) response.get("meals");
                                List<String> meals = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    meals.add((String) array.get(i));
                                }
                                userData = new UserData(id, meals);
                                UserData.isLogged = true;
                                showUserRecipes();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> {
                    new AlertDialog.Builder(this).setMessage("Podaj poprawny login i haslo!").show();
                    Log.e("Error", "Request fails!");
                });
                request.add(objectRequest);
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