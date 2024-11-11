package com.example.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Map;
import android.content.SharedPreferences;

public class DeleteNoteActivity extends AppCompatActivity {
    private ListView listViewNotesToDelete;
    private CustomNotesAdapter adapter;
    private ArrayList<String> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.delete_note);
        }

        listViewNotesToDelete = findViewById(R.id.listViewNotesToDelete);
        notesList = new ArrayList<>();
        adapter = new CustomNotesAdapter();
        listViewNotesToDelete.setAdapter(adapter);

        loadNotes();
    }

    private void loadNotes() {
        notesList.clear();
        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();
        notesList.addAll(allEntries.keySet());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CustomNotesAdapter extends ArrayAdapter<String> {
        CustomNotesAdapter() {
            super(DeleteNoteActivity.this, R.layout.list_item_delete_note, notesList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item_delete_note, parent, false);
            }

            TextView textView = convertView.findViewById(R.id.textViewNoteName);
            ImageButton deleteButton = convertView.findViewById(R.id.buttonDelete);

            String noteName = notesList.get(position);
            textView.setText(noteName);

            deleteButton.setOnClickListener(v -> {
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(noteName);
                editor.apply();

                Toast.makeText(DeleteNoteActivity.this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
                loadNotes();
            });

            return convertView;
        }
    }
}