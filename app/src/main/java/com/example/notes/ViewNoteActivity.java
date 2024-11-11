package com.example.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        String noteName = getIntent().getStringExtra("note_name");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(noteName);
        }

        TextView textViewNoteName = findViewById(R.id.textViewNoteName);
        TextView textViewNoteContent = findViewById(R.id.textViewNoteContent);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String noteContent = prefs.getString(noteName, "");

        textViewNoteName.setText(noteName);
        textViewNoteContent.setText(noteContent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}