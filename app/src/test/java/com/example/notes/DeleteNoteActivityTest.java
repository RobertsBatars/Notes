package com.example.notes;

import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;
import static android.content.Context.MODE_PRIVATE;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk = {29})
public class DeleteNoteActivityTest {
    private DeleteNoteActivity activity;
    private SharedPreferences prefs;
    private ListView listViewNotesToDelete;

    @Before
    public void setUp() {
        // Set up SharedPreferences with test data
        prefs = RuntimeEnvironment.getApplication()
                .getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("Test Note 1", "Content 1");
        editor.putString("Test Note 2", "Content 2");
        editor.apply();

        activity = Robolectric.buildActivity(DeleteNoteActivity.class)
                .create()
                .resume()
                .get();

        listViewNotesToDelete = activity.findViewById(R.id.listViewNotesToDelete);
    }

    @Test
    public void testInitialNotesList() {
        assertEquals(2, listViewNotesToDelete.getAdapter().getCount());
        assertTrue(listViewNotesToDelete.getAdapter().getItem(0).toString().startsWith("Test Note"));
        assertTrue(listViewNotesToDelete.getAdapter().getItem(1).toString().startsWith("Test Note"));
    }

    @Test
    public void testDeleteNote() {
        View firstNoteView = listViewNotesToDelete.getAdapter().getView(0, null, listViewNotesToDelete);
        ImageButton deleteButton = firstNoteView.findViewById(R.id.buttonDelete);
        TextView noteNameText = firstNoteView.findViewById(R.id.textViewNoteName);
        String noteName = noteNameText.getText().toString();

        // Click delete button
        deleteButton.performClick();

        // Verify note was deleted from SharedPreferences
        assertFalse(prefs.contains(noteName));

        // Verify toast message
        assertEquals(activity.getString(R.string.note_deleted), 
                    ShadowToast.getTextOfLatestToast());

        // Verify list was updated
        assertEquals(1, listViewNotesToDelete.getAdapter().getCount());
    }

    @Test
    public void testDeleteAllNotes() {
        // Delete all notes one by one
        while (listViewNotesToDelete.getAdapter().getCount() > 0) {
            View noteView = listViewNotesToDelete.getAdapter().getView(0, null, listViewNotesToDelete);
            ImageButton deleteButton = noteView.findViewById(R.id.buttonDelete);
            deleteButton.performClick();
        }

        // Verify all notes are deleted
        assertEquals(0, listViewNotesToDelete.getAdapter().getCount());
        assertEquals(0, prefs.getAll().size());
    }

    @Test
    public void testActionBarTitle() {
        assertEquals(activity.getString(R.string.delete_note), 
                    activity.getSupportActionBar().getTitle());
    }

    @Test
    public void testActionBarBackButton() {
        int displayOptions = activity.getSupportActionBar().getDisplayOptions();
        assertTrue((displayOptions & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0);
    }

    @Test
    public void testNavigateUp() {
        MenuItem homeItem = new TestMenuItem(android.R.id.home);
        assertTrue(activity.onOptionsItemSelected(homeItem));
        assertTrue(activity.isFinishing());
    }

    @Test
    public void testNoteViewLayout() {
        View noteView = listViewNotesToDelete.getAdapter().getView(0, null, listViewNotesToDelete);
        
        assertNotNull(noteView.findViewById(R.id.textViewNoteName));
        assertNotNull(noteView.findViewById(R.id.buttonDelete));
    }
}

