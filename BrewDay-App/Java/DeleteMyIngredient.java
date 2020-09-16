package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteMyIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_my_ingredient);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        Button goBack = (Button) findViewById(R.id.MyList);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backPage = new Intent(getApplicationContext(), MyIngredientsList.class);
                startActivity(backPage);
            }
        });

        Button deleteAllIngr = (Button) findViewById(R.id.deleteAll);
        deleteAllIngr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteAllIngredient();
                Intent goToMyList = new Intent(getApplicationContext(), MyIngredientsList.class);
                startActivity(goToMyList);
            }
        });


        final ListView deletemyIngredientsList = (ListView) findViewById(R.id.deleteMyIngredientsList);


        final ArrayList<String> theMyIngrList = new ArrayList<>();

        Cursor data = myDB.getMyIngredientsList();

        if (data.getCount() == 0) {
            Toast.makeText(DeleteMyIngredient.this, "THERE ARE NO INGREDIENTS SAVED!", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                theMyIngrList.add(data.getString(1));
                //theBeerList.add(data.getString(2));
                //theBeerList.add(data.getString(1) +"               "+ data.getString(2));
            }

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
            deletemyIngredientsList.setAdapter(SimplelistAdapter);

            deletemyIngredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int idI = (int) id;

                    myDB.deleteIngredient(idI);

                    startActivity(getIntent()); // refresh the all activuty, a better looking good way would be to refresh only the listview
                    finish();
                }
            });

        }

    }


}

