package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.TextView;
// TestNotesApplication is in the same package
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.*;
import static android.content.Context.MODE_PRIVATE;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(
    sdk = {29},
    application = TestNotesApplication.class,
    manifest = "AndroidManifest.xml"
)
public class ViewNoteActivityTest {
    private ViewNoteActivity activity;
    private SharedPreferences prefs;
    private ActivityController<ViewNoteActivity> controller;
    private TextView textViewNoteName;
    private TextView textViewNoteContent;
    private static final String TEST_NOTE_NAME = "Test Note";
    private static final String TEST_NOTE_CONTENT = "Test Content";

    @Before
    public void setUp() {
        // Set up SharedPreferences with test data
        prefs = RuntimeEnvironment.getApplication()
                .getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString(TEST_NOTE_NAME, TEST_NOTE_CONTENT);
        editor.apply();

        // Create intent with test note name
        Intent intent = new Intent(RuntimeEnvironment.getApplication(), ViewNoteActivity.class);
        intent.putExtra("note_name", TEST_NOTE_NAME);

        // Create activity with intent
        controller = Robolectric.buildActivity(ViewNoteActivity.class, intent);
        controller.create();
        activity = controller.get();
        activity.setTheme(android.R.style.Theme_Material_Light);

        // Initialize views before resuming
        textViewNoteName = activity.findViewById(R.id.textViewNoteName);
        textViewNoteContent = activity.findViewById(R.id.textViewNoteContent);

        controller.resume();
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
        prefs.edit().clear().apply();
    }

    @Test
    public void testNoteDisplayed() {
        assertEquals(TEST_NOTE_NAME, textViewNoteName.getText().toString());
        assertEquals(TEST_NOTE_CONTENT, textViewNoteContent.getText().toString());
    }

    @Test
    public void testActionBarTitle() {
        assertEquals(TEST_NOTE_NAME, activity.getSupportActionBar().getTitle());
    }

    @Test
    public void testActionBarBackButton() {
        assertTrue((activity.getSupportActionBar().getDisplayOptions() 
            & android.app.ActionBar.DISPLAY_HOME_AS_UP) != 0);
    }

    @Test
    public void testNavigateUp() {
        assertTrue(activity.onSupportNavigateUp());
        assertTrue(activity.isFinishing());
    }

    @Test
    public void testNonExistentNote() {
        Intent intent = new Intent(RuntimeEnvironment.getApplication(), ViewNoteActivity.class);
        intent.putExtra("note_name", "Non Existent Note");

        ActivityController<ViewNoteActivity> controller = Robolectric.buildActivity(ViewNoteActivity.class, intent);
        controller.create();
        ViewNoteActivity newActivity = controller.get();
        newActivity.setTheme(android.R.style.Theme_Material_Light);

        TextView textViewNoteName = newActivity.findViewById(R.id.textViewNoteName);
        TextView textViewNoteContent = newActivity.findViewById(R.id.textViewNoteContent);

        controller.resume();

        assertEquals("Non Existent Note", textViewNoteName.getText().toString());
        assertEquals("", textViewNoteContent.getText().toString());
    }

    @Test
    public void testMissingNoteNameExtra() {
        Intent intent = new Intent(RuntimeEnvironment.getApplication(), ViewNoteActivity.class);

        ActivityController<ViewNoteActivity> controller = Robolectric.buildActivity(ViewNoteActivity.class, intent);
        controller.create();
        ViewNoteActivity newActivity = controller.get();
        newActivity.setTheme(android.R.style.Theme_Material_Light);

        TextView textViewNoteName = newActivity.findViewById(R.id.textViewNoteName);
        TextView textViewNoteContent = newActivity.findViewById(R.id.textViewNoteContent);

        controller.resume();

        assertEquals("", textViewNoteName.getText().toString());
        assertEquals("", textViewNoteContent.getText().toString());
    }
}