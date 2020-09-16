package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddBeer extends AppCompatActivity {

    DatabaseHelper myDB = new DatabaseHelper(this);
    Boolean Valid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        final EditText beerN = (EditText) findViewById(R.id.beerN);
        final EditText beerT = (EditText) findViewById(R.id.BeerT);

        final AutoCompleteTextView ingN = (AutoCompleteTextView) findViewById(R.id.ingN);
        final EditText ingQ = (EditText) findViewById(R.id.ingQ);
        //final EditText ingU = (EditText) findViewById(R.id.ingU);

        Cursor IngredientsData = myDB.getAllIngredientsList();
        Cursor IngredientsData2 = myDB.getMyIngredientsList();

        final ArrayList<String> ingredientsName1 = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEI MIEI INGREDIENTI
        while (IngredientsData1.moveToNext()) {
            ingredientsName1.add(IngredientsData1.getString(2));
        }
        final ArrayList<String> ingredientsName2 = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEI MIEI INGREDIENTI
        while (IngredientsData2.moveToNext()) {
            ingredientsName2.add(IngredientsData2.getString(1));
        }

        ingredientsName1.addAll(ingredientsName2);

        final ArrayList<String> noDuplicates = new ArrayList<>();

        for (int i = 0; i < ingredientsName1.size(); i++){
            if(!noDuplicates.contains(ingredientsName1.get(i))){
                noDuplicates.add(ingredientsName1.get(i));
            }
        }

            ArrayAdapter<String> autocompleteIngredients = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noDuplicates);
            ingN.setAdapter(autocompleteIngredients);



            final Button beerList = (Button) findViewById(R.id.beerList);
            final Button addBeer = (Button) findViewById(R.id.addBeer);
            addBeer.setEnabled(false);

            TextWatcher checkBox = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String newBeerName = beerN.getText().toString().trim();
                    String newBeerTime = beerT.getText().toString().trim();
                    addBeer.setEnabled(!newBeerName.isEmpty() && !newBeerTime.isEmpty());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            };

            beerN.addTextChangedListener(checkBox);
            beerT.addTextChangedListener(checkBox);

            addBeer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newBeerN = beerN.getText().toString();
                    String newBeerT = beerT.getText().toString();
                    Integer intTime = Integer.parseInt(newBeerT);

                    Boolean validBeerName = myDB.checkBeerName(newBeerN);

                    if (validBeerName == false) {
                        Toast.makeText(AddBeer.this, "Beer name already used! Choose another name.", Toast.LENGTH_SHORT).show();
                    } else {
                        Beer b = new Beer();
                        b.setBeerName(newBeerN);
                        b.setBeerTime(intTime);

                        myDB.addBeer(b);

                        //addIngredient.setEnabled(true);
                        Valid = true; // step one to enable addIngredient button
                    }
                }
            });

        final Integer beerID = myDB.getLastBeerID();

            beerList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent beerListPage = new Intent(getApplicationContext(), BeerList.class);
                    startActivity(beerListPage);
                }
            });

        final Button addIngredient = (Button) findViewById(R.id.homepage);
        addIngredient.setEnabled(false);

        TextWatcher checkBox2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newIngrName = ingN.getText().toString().trim();
                String newIngrQuantity = ingQ.getText().toString().trim();
                //String newIngrUnit = ingU.getText().toString().trim();
                //addIngredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty() && !newIngrUnit.isEmpty() && Valid == true); // In case the Unit must be typed use this command instead of the one below
                addIngredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty() && Valid == true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        ingN.addTextChangedListener(checkBox2);
        ingQ.addTextChangedListener(checkBox2);
        //ingU.addTextChangedListener(checkBox2);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newIngN = ingN.getText().toString();
                String newIngQ = ingQ.getText().toString();
                Double intIngQ = Double.parseDouble(newIngQ); //inizialmente era un Integer poi cambiato a Double
                //String newIngU = ingU.getText().toString();

                Boolean validIngredientName = myDB.checkIngredientName(newIngN, beerID);

                if (validIngredientName == false) {
                    Toast.makeText(AddBeer.this, "This ingredient is already in the list!", Toast.LENGTH_SHORT).show();
                } else {

                    Recipe r = new Recipe();
                    r.setIngName(newIngN);
                    r.setIngQuantity(intIngQ);
                    r.setIngUnit("grams");

                    myDB.addIngredient(r);

                    ingN.getText().clear();
                    ingQ.getText().clear();
                    //ingU.getText().clear();
                }
            }
        });

    }
}
