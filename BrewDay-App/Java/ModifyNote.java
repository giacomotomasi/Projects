package com.example.beerday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        final DatabaseHelper myDB;
        myDB = new DatabaseHelper(this);

        final EditText modify = (EditText) findViewById(R.id.notes);
        final Button confirm = (Button) findViewById(R.id.confirm);

        Intent intent = getIntent();
        final String note = intent.getExtras().getString("noteN");
        Bundle bundle = getIntent().getExtras();
        final Integer id = bundle.getInt("ID", 0);

        modify.setText(note);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String updateNote = modify.getText().toString();
                myDB.updateNote(id, updateNote);

                Intent goToNote = new Intent(getApplicationContext(), Note.class);
                goToNote.putExtra("bID", id);
                startActivity(goToNote);
            }
        });
    }
}
