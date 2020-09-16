package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyMyIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_ingredient);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        final EditText modifyName = (EditText) findViewById(R.id.modifyN);
        final EditText modifyQuantity = (EditText) findViewById(R.id.modifyQ);
        Button update = (Button) findViewById(R.id.update);

        Bundle bundle = getIntent().getExtras();
        final Integer beerID = bundle.getInt("beerID", 0);

        Cursor ingredient = myDB.getOneIngredient(beerID);
        String nameI = null;
        String quantityI = null; // even if it is a number, for the editText it must be passed as String

        if (ingredient.moveToFirst()) {
            nameI = ingredient.getString(1);
            quantityI = ingredient.getString(2);
        }

        modifyName.setText(nameI);
        modifyQuantity.setText(quantityI);

        final String finalNameI = nameI;
        
         update.setEnabled(false); // from line 44 to 65 a code to avoid to update an ingredient if the boxes are not both filled
        TextWatcher checkBox = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newBeerName = modifyName.getText().toString().trim();
                String newBeerTime = modifyQuantity.getText().toString().trim();
                update.setEnabled(!newBeerName.isEmpty() && !newBeerTime.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        modifyName.addTextChangedListener(checkBox);
        modifyQuantity.addTextChangedListener(checkBox);

        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateIngN = modifyName.getText().toString();
                String updateIngQ = modifyQuantity.getText().toString();
                Double quantity = Double.parseDouble(updateIngQ);

                Boolean validIngredientName = myDB.checkMyIngredientName(updateIngN); // ONLY THIS CHECK IS NOT ENOUGH IN THIS CASE SINCE THE INGREDIENT SURELY IS IN THE DATABASE. A SOLUTION IS GIVE A DEGREE O FREEDOM IN THE SENSE OF AVOID THE CHECK IF THE NAME I WANT TO UPDATE IS THE SAME I WANT TO MODIFY

                if (validIngredientName == false && !finalNameI.equals(updateIngN)) {
                    Toast.makeText(ModifyMyIngredient.this, "This ingredient is already in the list!", Toast.LENGTH_SHORT).show();
                } else {
                    Ingredient i = new Ingredient();
                    i.setMyIngName(updateIngN);
                    i.setMyIngQuantity(quantity);
                    i.setMyIngUnit("grams");

                    myDB.updateMyIngredient(beerID, i);

                    Intent myListIngr = new Intent(getApplicationContext(), MyIngredientsList.class);
                    startActivity(myListIngr);
                }

            }
        });

    }
}
