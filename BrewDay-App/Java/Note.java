package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Note extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Bundle bundle = getIntent().getExtras();
        final Integer beerID = bundle.getInt("bID", 0);

        Button home = (Button) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homepage);
            }
        });

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);
        //Integer x = 4;

        Cursor n = myDB.getNote(beerID);
        String note = null;

        while (n.moveToNext()) {
            note = n.getString(0);
        }

        TextView showNote = (TextView) findViewById(R.id.comments);
        showNote.setMovementMethod(new ScrollingMovementMethod());
        String error = "There is no note! Please write one!";

        if (note.isEmpty()) {
            showNote.setText(error);
        } else {
            showNote.setText(note);
        }


        Button modifyNote = (Button) findViewById(R.id.modify);
        final String finalNote = note;
        modifyNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent modifyN = new Intent(getApplicationContext(), ModifyNote.class);
                modifyN.putExtra("noteN", finalNote);
                modifyN.putExtra("ID", beerID);
                startActivity(modifyN);
            }
        });

    }
}
