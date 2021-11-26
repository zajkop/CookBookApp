package com.cookbookapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cookbookapp.domain.Recipe;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public class MealsAdapter extends ArrayAdapter {

    private final List<Recipe> recipes;
    private final Activity context;

    public MealsAdapter(Activity context, List<Recipe> recipes) {
        super(context, R.layout.row_item, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @SneakyThrows
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null)
            row = inflater.inflate(R.layout.activity_my_meals, null, true);
        TextView name = row.findViewById(R.id.textViewImageName);
        TextView description = row.findViewById(R.id.textViewImageDescription);
        TextView calories = row.findViewById(R.id.calories);
        TextView ingredients = row.findViewById(R.id.ingredients);
        ImageView imageFlag = row.findViewById(R.id.imageViewFlag);

        Recipe recipe = recipes.get(position);
        name.setText(recipe.getName());
        description.setText("Opis \n" + recipe.getDescription() + "\n");
        calories.setText(recipe.getKcal().toString());
        ingredients.setText("Skadniki \n" + StringUtils.join(recipe.getIngredients(), ", ") + "\n");
        String uri = recipe.getImageUri();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("http://10.0.2.2:8080/recipe/" + uri);
        URLConnection connection = url.openConnection();
        Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        imageFlag.setImageBitmap(bitmap);
        return row;
    }
}
