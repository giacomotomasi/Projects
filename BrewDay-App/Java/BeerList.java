package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class BeerList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        final DatabaseHelper myDB;

        Button addBeer = (Button) findViewById(R.id.add);
        addBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getApplicationContext(), AddBeer.class);
                startActivity(add);
            }
        });

        ListView beerList = (ListView) findViewById(R.id.beerList);
        myDB = new DatabaseHelper(this);

        Cursor data = myDB.getBeerList(); // CURSORE !!!

        if (data.getCount() == 0){
            Toast.makeText(BeerList.this,"THERE ARE NO BEERS SAVED!", Toast.LENGTH_SHORT).show();
        }else{
            final String[] from = new String[] {
                    //DatabaseHelper.COL0,
                    DatabaseHelper.COL1,
                    DatabaseHelper.COL2};
            final int[] to = new int[] {
                    //R.id.bId,
                    R.id.bName,
                    R.id.bTime};

            SimpleCursorAdapter SimplelistAdapter = new SimpleCursorAdapter(this, R.layout.row, data, from, to);
            beerList.setAdapter(SimplelistAdapter);

            beerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent showIngredientsList = new Intent(getApplicationContext(), showBeerIngredients.class);

                    int idB = (int) id;

                    showIngredientsList.putExtra("beerID", idB);
                    //showIngredientsList.putExtra("beerID", position);
                    startActivity(showIngredientsList);
                }
            });
        }
    }
}

