package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteBeerIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_beer_ingredient);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        Button goBack = (Button) findViewById(R.id.BeerList);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backPage = new Intent(getApplicationContext(), BeerList.class);
                startActivity(backPage);
            }
        });

        Button homepage = (Button) findViewById(R.id.homepage);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHomepage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goHomepage);
            }
        });


        final ListView deletemyIngredientsList = (ListView) findViewById(R.id.deleteBeerIngredientsList);


        final ArrayList<String> theBeerIngrList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        final Integer beerID = bundle.getInt("beerID", 0);

        Cursor data = myDB.getRecipeIngredientsList(beerID);

        if (data.getCount() == 0) {
            Toast.makeText(DeleteBeerIngredient.this, "THERE ARE NO INGREDIENTS SAVED!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                theBeerIngrList.add(data.getString(1));
                //theBeerList.add(data.getString(2));
                //theBeerList.add(data.getString(1) +"               "+ data.getString(2));
            }

            final String[] from = new String[]{
                    //DatabaseHelper.COL0,
                    DatabaseHelper.COL2R,
                    DatabaseHelper.COL3R,
                    DatabaseHelper.COL4R};
            final int[] to = new int[]{
                    //R.id.bId,
                    R.id.iName,
                    R.id.iQuantity,
                    R.id.iUnit};

            SimpleCursorAdapter SimplelistAdapter = new SimpleCursorAdapter(this, R.layout.row_ingred, data, from, to);
            deletemyIngredientsList.setAdapter(SimplelistAdapter);

            deletemyIngredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int idB = (int) id;
                    myDB.deleteRecipeIngredient(idB);

                    startActivity(getIntent()); // refresh the all activuty, a better looking good way would be to refresh only the listview
                    finish();

                }
            });

        }
    }
}
