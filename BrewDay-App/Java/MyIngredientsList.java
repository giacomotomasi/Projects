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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyIngredientsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ingredients_list);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        final AutoCompleteTextView myingN = (AutoCompleteTextView) findViewById(R.id.myingN);
        final EditText myingQ = (EditText) findViewById(R.id.myingQ);
        //final EditText myingU = (EditText) findViewById(R.id.myingU);

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
            myingN.setAdapter(autocompleteIngredients);

            Button deleteIngr = (Button) findViewById(R.id.deleteIngredient);
            deleteIngr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent deleteList = new Intent(getApplicationContext(), DeleteMyIngredient.class);
                    startActivity(deleteList);
                }
            });


            ListView myIngredientsList = (ListView) findViewById(R.id.myIngredientsList);

            Cursor data = myDB.getMyIngredientsList();

            if (data.getCount() == 0) {
                Toast.makeText(MyIngredientsList.this, "THERE ARE NO INGREDIENTS SAVED!", Toast.LENGTH_SHORT).show();
            } else {
                final String[] from = new String[]{
                        //DatabaseHelper.COL0,
                        DatabaseHelper.COL2M,
                        DatabaseHelper.COL3M,
                        DatabaseHelper.COL4M};
                final int[] to = new int[]{
                        //R.id.bId,
                        R.id.iName,
                        R.id.iQuantity,
                        R.id.iUnit};

                SimpleCursorAdapter SimplelistAdapter = new SimpleCursorAdapter(this, R.layout.row_ingred, data, from, to);
                myIngredientsList.setAdapter(SimplelistAdapter);

                myIngredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent modify = new Intent(getApplicationContext(), ModifyMyIngredient.class);
                        int ID = (int) id;
                        modify.putExtra("beerID", ID);
                        startActivity(modify);
                });
            }
                final Button newIngredient = (Button) findViewById(R.id.addMyIngredient);
                newIngredient.setEnabled(false);

                TextWatcher checkBox = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String newIngrName = myingN.getText().toString().trim();
                        String newIngrQuantity = myingQ.getText().toString().trim();
                        //String newIngrUnit = myingU.getText().toString().trim();
                        //newIngredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty() && !newIngrUnit.isEmpty());
                        newIngredient.setEnabled(!newIngrName.isEmpty() && !newIngrQuantity.isEmpty());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };

                myingN.addTextChangedListener(checkBox);
                myingQ.addTextChangedListener(checkBox);
                //myingU.addTextChangedListener(checkBox);

                newIngredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mynewIngN = myingN.getText().toString();
                        String mynewIngQ = myingQ.getText().toString();
                        Double myintIngQ = Double.parseDouble(mynewIngQ);
                        //String mynewIngU = myingU.getText().toString();

                        Boolean validIngredientName = myDB.checkMyIngredientName(mynewIngN);

                        if (validIngredientName == false) {
                            Toast.makeText(MyIngredientsList.this, "This ingredient is already in the list!", Toast.LENGTH_SHORT).show();
                        } else {
                            Ingredient i = new Ingredient();
                            i.setMyIngName(mynewIngN);
                            i.setMyIngQuantity(myintIngQ);
                            i.setMyIngUnit("grams");

                            myDB.addMyIngredient(i);

                            myingN.getText().clear();
                            myingQ.getText().clear();
                            //myingU.getText().clear();

                            startActivity(getIntent()); // refresh the all activity, a better looking good way would be to refresh only the listview
                            finish();
                        }
                    }
                });

            Button homepage = (Button) findViewById(R.id.homepage);
            homepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent home = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(home);
                }
            });
        }
    }

