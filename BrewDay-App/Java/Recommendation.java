package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Recommendation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        Cursor beerData = myDB.getBeerList();
        //Cursor recipeData = myDB.getRecipeIngredientsList(1);
        Cursor myIngredientsData = myDB.getMyIngredientsList();

        final ArrayList<String> BeerInfo = new ArrayList<>();
        while (beerData.moveToNext()) {
            BeerInfo.add(beerData.getString(0)); // Beer ID
            BeerInfo.add(beerData.getString(3)); // Beer Counter
        }


        final ArrayList<String> MyIngredients = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEI MIEI INGREDIENTI
        while (myIngredientsData.moveToNext()) {
            MyIngredients.add(myIngredientsData.getString(1)); //  My Ingredient name
            MyIngredients.add(myIngredientsData.getString(2)); // My Ingredeint quantity
        }

        final ArrayList<Boolean> validBeer = new ArrayList<>();

        ArrayList<Integer> IDBeer = new ArrayList<>();
        for (int i = 0; i < BeerInfo.size(); i = i + 2) { // IDBeer is an ArrayList with all the ID of beers in the database
            String prova = BeerInfo.get(i);
            Integer id = Integer.parseInt(prova);
            IDBeer.add(id);
        }

        Integer numberB = (BeerInfo.size() / 2); // numero birre nella lista

        for (int i = 0; i < numberB; i = i + 1) {
            Integer id = IDBeer.get(i);
            Cursor recipeData = myDB.getRecipeIngredientsList(id);

            ArrayList<String> IngredientsBeer = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEGLI INGREDIENTI DELLA BIRRA SELEZIONATA
            while (recipeData.moveToNext()) {
                IngredientsBeer.add(recipeData.getString(2)); // Ingredient name
                IngredientsBeer.add(recipeData.getString(3)); // Ingredient quantity
            }
            Integer count = myDB.chekAvailability(IngredientsBeer, MyIngredients);

            if (count == IngredientsBeer.size() && count != 0) {
                validBeer.add(true);
            } else {
                validBeer.add(false);
            }
        }


        //------------------ RECOMMENDATION ALGOROTHM---------------------------------------------------------------------------------------------------

        final ArrayList<Double> beta = new ArrayList();
        final ArrayList<Double> alpha = new ArrayList();
        final ArrayList<Double> score = new ArrayList<>();
        final ArrayList<Double> alphaN = new ArrayList<>();



        for (int i = 0; i < validBeer.size(); i++) {  // valid size is the number of beers checked (sia cucinabili che non)
            if (validBeer.get(i) == true) {
                Cursor recipedata = myDB.getRecipeIngredientsList(IDBeer.get(i)); // mettento solo myDB.getRecipeIngredientsList(i) se gli id non sono consecutivi da errore

                Integer beer_counter = 0;

                ArrayList<String> IngredientsBeer = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEGLI INGREDIENTI DELLA BIRRA
                while (recipedata.moveToNext()) {
                    IngredientsBeer.add(recipedata.getString(2)); // Ingredient name
                    IngredientsBeer.add(recipedata.getString(3)); // Ingredient quantity
                }

                for (int j = 0; j < BeerInfo.size(); j = j + 2) {
                    if (Integer.parseInt(BeerInfo.get(j)) == IDBeer.get(i)){
                        beer_counter = Integer.parseInt(BeerInfo.get(j+1));
                    }
                }

                double Beta = myDB.getBeta(beer_counter); // passo il valore del counter della birra in esame
                double Alpha = myDB.getAlpha(IngredientsBeer, MyIngredients);
                beta.add(Beta);
                alpha.add(Alpha);
            } else {
                beta.add(0.00);
                alpha.add(0.00);
            }
        }


        Double maxAlpha = Collections.max(alpha); // Alpha normalization
        for (int i = 0; i < validBeer.size(); i++) {
            Double norm = (alpha.get(i) / maxAlpha) * 10;
            alphaN.add(norm);
        }

        for (int i = 0; i < alpha.size(); i++) { // OR (int i = 0; i < beta.size(); i++)
            Double scoreB = (alphaN.get(i) * 0.65) + (beta.get(i) * 0.35);
            score.add(scoreB);
        }


        //ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, score);
        //BETA.setAdapter(listAdapter);

        //tv.setText(alpha);

        Double max = Collections.max(score); // this function returns the max score following the natural ordering. If two scores have the same value the one returned is the last one
        Integer ID = score.indexOf(max);
        final Integer realID = IDBeer.get(ID); // restituisce l'ID del database della birra che ha lo score massimo. la variabile ID nella riga sotto restituisce l'indice dell'array con valore massimo

        // UNTIL NOW WE HAVE GOT THE SCORE OF THE BEER AND THE ID OF THE ONE WITH THE BEST SCORE (WHICH IS THE ONE SUPPOSED TO BE RECOMMENDED)


        //----------------SHOW RECOMMENDED RECIPE------------------------------------------------------------------------------------------------------------

        Cursor recipeData = myDB.getRecipeIngredientsList(realID);
        String recommendationN = myDB.getBeerName(realID);

        final ArrayList<String> IngredientsBeer = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEGLI INGREDIENTI DELLA BIRRA SELEZIONATA
        while (recipeData.moveToNext()) {
            IngredientsBeer.add(recipeData.getString(2)); // Ingredient name
            IngredientsBeer.add(recipeData.getString(3)); // Ingredient quantity
        }

        ListView RecipeIngredients = (ListView) findViewById(R.id.showRecommended);
        TextView Name = (TextView) findViewById(R.id.recommendationName);

        if (recipeData.getCount() == 0 || max.isNaN()) {
            Toast.makeText(Recommendation.this, "THERE IS NO BEER RECOMMENDED!", Toast.LENGTH_SHORT).show();
        } else {
            final String[] from = new String[]{
                    //DatabaseHelper.COL0,
                    DatabaseHelper.COL2R,
                    DatabaseHelper.COL3R,
                    DatabaseHelper.COL4R};
            final int[] to = new int[]{
                    R.id.iName,
                    R.id.iQuantity,
                    R.id.iUnit};

            SimpleCursorAdapter SimplelistAdapter = new SimpleCursorAdapter(this, R.layout.row_ingred, recipeData, from, to);
            RecipeIngredients.setAdapter(SimplelistAdapter);

            Name.setText(recommendationN);
        }

        Button cook = (Button) findViewById(R.id.cook);

        Integer count = myDB.chekAvailability(IngredientsBeer, MyIngredients);

        if (count == IngredientsBeer.size()) {
            cook.setEnabled(true);
        } else {
            cook.setEnabled(false);
        }

        cook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.subtractQuantities(IngredientsBeer, MyIngredients);
                myDB.updateCounter(realID, BeerInfo);

                Intent myListofIngredients = new Intent(getApplicationContext(), MyIngredientsList.class);
                startActivity(myListofIngredients);
            }
        });


        final Button scoreButton = (Button) findViewById(R.id.score);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent showScores = new Intent(getApplicationContext(), Score.class);

                showScores.putExtra("ValuesPassed", score);

                startActivity(showScores);

            }
        });
    }
}

