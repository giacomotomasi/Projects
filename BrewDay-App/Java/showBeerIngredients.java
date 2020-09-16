package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class showBeerIngredients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_beer_ingredients);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        final AutoCompleteTextView ingN = (AutoCompleteTextView) findViewById(R.id.ingN);
        final EditText ingQ = (EditText) findViewById(R.id.ingQ);
        final TextView tv = (TextView) findViewById(R.id.textView);
        //final EditText ingU = (EditText) findViewById(R.id.ingU);

        Cursor IngredientsData = myDB.getAllIngredientsList();

        final ArrayList<String> ingredientsName = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEI MIEI INGREDIENTI
        while (IngredientsData.moveToNext()) {
            ingredientsName.add(IngredientsData.getString(2));


            final ArrayList<String> noDuplicates = new ArrayList<>();

            for (int i = 0; i < ingredientsName.size(); i++){
                if(!noDuplicates.contains(ingredientsName.get(i))){
                    noDuplicates.add(ingredientsName.get(i));
                }
            }

            ArrayAdapter<String> autocompleteIngredients = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noDuplicates);
            ingN.setAdapter(autocompleteIngredients);

            Bundle bundle = getIntent().getExtras();
            final Integer beerID = bundle.getInt("beerID", 0);

            ListView RecipeIngredients = (ListView) findViewById(R.id.beerIngredientsList);

            Cursor recipeData = myDB.getRecipeIngredientsList(beerID);
            Cursor myIngredientsData = myDB.getMyIngredientsList();
            Cursor beerData = myDB.getBeerList();

            //------------------ARRAY LISTS CREATION-----------------------------------------------------------------------------------------------------

            final ArrayList<String> IngredientsBeer = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEGLI INGREDIENTI DELLA BIRRA SELEZIONATA E LE QUANTITà
            while (recipeData.moveToNext()) {
                IngredientsBeer.add(recipeData.getString(2)); // Ingredient name
                IngredientsBeer.add(recipeData.getString(3)); // Ingredient quantity
            }

            final ArrayList<String> MyIngredients = new ArrayList<>(); // CREO UNA LISTA CON I NOMI DEI MIEI INGREDIENTI E LA QUANTITà
            while (myIngredientsData.moveToNext()) {
                MyIngredients.add(myIngredientsData.getString(1)); //  My Ingredient name
                MyIngredients.add(myIngredientsData.getString(2)); // My Ingredeint quantity
            }

            final ArrayList<String> BeerInfo = new ArrayList<>();
            while (beerData.moveToNext()){
                BeerInfo.add(beerData.getString(0)); // Beer ID
                BeerInfo.add(beerData.getString(3)); // Beer Counter
            }

            //-------------------------------------------------------------------------------------------------------------------------------------------

            if (recipeData.getCount() == 0) {
                Toast.makeText(showBeerIngredients.this, "THERE ARE NO INGREDIENTS SAVED!", Toast.LENGTH_SHORT).show();
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

                //---------------MODIFY INGREDIENT--------------------------------------------------------------------------------------

                RecipeIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent modify = new Intent(getApplicationContext(), ModifyBeerIngredient.class);
                        int ID = (int) id;
                        modify.putExtra("beerID", beerID);
                        modify.putExtra("ingID", ID); // I AM PASSING THE ID OF THE INGREDIENT IN TABLE_RECIPE
                        startActivity(modify);
                    }
                });
            }

                final Button addingredient = (Button) findViewById(R.id.addIngredient);
                addingredient.setEnabled(false);

                TextWatcher checkBox = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String newIngrName = ingN.getText().toString().trim();
                        String newIngrQuantity = ingQ.getText().toString().trim();
                        //String newIngrUnit = ingU.getText().toString().trim();
                        //addingredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty() && !newIngrUnit.isEmpty());
                        addingredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };

                ingN.addTextChangedListener(checkBox);
                ingQ.addTextChangedListener(checkBox);
                //ingU.addTextChangedListener(checkBox);

                addingredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newIngN = ingN.getText().toString();
                        String newIngQ = ingQ.getText().toString();
                        Double intIngQ = Double.parseDouble(newIngQ);//inizialmente era un Integer poi cambiato a Double
                        //String newIngU = ingU.getText().toString();

                        Boolean validIngredientName = myDB.checkIngredientName(newIngN, beerID);

                        if (validIngredientName == false) {
                            Toast.makeText(showBeerIngredients.this, "This ingredient is already in the list!", Toast.LENGTH_SHORT).show();
                        } else {
                            Recipe r = new Recipe();
                            r.setIngName(newIngN);
                            r.setIngQuantity(intIngQ);
                            r.setIngUnit("grams");

                            Integer bID = beerID;

                            myDB.addIngredientOld(r, bID);

                            ingN.getText().clear();
                            ingQ.getText().clear();
                            //ingU.getText().clear();

                            startActivity(getIntent()); // refresh the all activuty, a better looking good way would be to refresh only the listview
                            finish();
                        }

                    }
                });



            Button deleteBeer = (Button) findViewById(R.id.deleteBeer);

            deleteBeer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDB.deleteBeer(beerID);
                    Toast.makeText(showBeerIngredients.this, "Beer deleted!", Toast.LENGTH_SHORT).show();
                    Intent goToListBeer = new Intent(getApplicationContext(), BeerList.class);
                    startActivity(goToListBeer);
                }
            });


            Button cook = (Button) findViewById(R.id.cook);

        /*
        Integer count = myDB.chekAvailability(NameIngredientsBeer, NameMyIngredients, QuantityIngredientsBeer, QuantityMYIngredients);

        if (count == NameIngredientsBeer.size()){
            cook.setEnabled(true);
        }else{
            cook.setEnabled(false);
        }
         */

            Integer count = myDB.chekAvailability(IngredientsBeer, MyIngredients);

            if (count == IngredientsBeer.size()) {
                cook.setEnabled(true);
            } else {
                cook.setEnabled(false);
            }

            //tv.setText(NameIngredientsBeer.get(1));

            cook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDB.subtractQuantities(IngredientsBeer, MyIngredients);
                    myDB.updateCounter(beerID, BeerInfo);

                    Intent myListofIngredients = new Intent(getApplicationContext(), MyIngredientsList.class);
                    startActivity(myListofIngredients);
                }
            });

        }

        Button home = (Button) findViewById(R.id.homepage);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homepage);
            }
        });

        Bundle bundle = getIntent().getExtras();
        final Integer beerID = bundle.getInt("beerID", 0);

        Button deleteIngredient = (Button) findViewById(R.id.deleteIngredient);
        deleteIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIng = new Intent(getApplicationContext(), DeleteBeerIngredient.class);
                deleteIng.putExtra("beerID", beerID);
                startActivity(deleteIng);
            }
        });

        Button note = (Button) findViewById(R.id.note);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent note = new Intent(getApplicationContext(), Note.class);
                note.putExtra("bID", beerID);
                startActivity(note);
            }
        });

    }
}

