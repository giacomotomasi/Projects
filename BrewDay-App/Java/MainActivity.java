package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button beerList = (Button) findViewById(R.id.MyList);
        beerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myBeerList = new Intent(getApplicationContext(), BeerList.class);
                startActivity(myBeerList);
            }
        });

        Button myIngredientsList = (Button) findViewById(R.id.myIngr);
        myIngredientsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIngredients = new Intent(getApplicationContext(), MyIngredientsList.class);
                startActivity(myIngredients);
            }
        });

        final Button recommendation = (Button) findViewById(R.id.BrewToday);
        recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showRecommendation = new Intent(getApplicationContext(), Recommendation.class);
                startActivity(showRecommendation);
            }
        });
    }
}
