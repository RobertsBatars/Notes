package com.example.notes;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;
import static android.content.Context.MODE_PRIVATE;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(
    sdk = {29},
    application = android.app.Application.class,
    manifest = "src/main/AndroidManifest.xml"
)
public class AddNoteActivityTest {
    private AddNoteActivity activity;
    private SharedPreferences prefs;
    private EditText editTextNoteName;
    private EditText editTextNoteContent;
    private Button buttonSave;
    private ActivityController<AddNoteActivity> controller;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(AddNoteActivity.class);
        controller.create();
        activity = controller.get();
        activity.setTheme(android.R.style.Theme_Material_Light);

        prefs = RuntimeEnvironment.getApplication()
                .getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        editTextNoteName = activity.findViewById(R.id.editTextNoteName);
        editTextNoteContent = activity.findViewById(R.id.editTextNoteContent);
        buttonSave = activity.findViewById(R.id.buttonSave);

        controller.resume();
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
        prefs.edit().clear().apply();
    }

    @Test
    public void testSaveNoteWithValidInput() {
        String testNoteName = "Test Note";
        String testNoteContent = "Test Content";

        editTextNoteName.setText(testNoteName);
        editTextNoteContent.setText(testNoteContent);
        buttonSave.performClick();

        // Verify note was saved to SharedPreferences
        String savedContent = prefs.getString(testNoteName, "");
        assertEquals(testNoteContent, savedContent);

        // Verify success toast was shown
        assertEquals(activity.getString(R.string.note_saved), 
                    ShadowToast.getTextOfLatestToast());

        // Verify activity was finished
        assertTrue(activity.isFinishing());
    }

    @Test
    public void testSaveNoteWithEmptyName() {
        editTextNoteName.setText("");
        editTextNoteContent.setText("Test Content");
        buttonSave.performClick();

        // Verify warning toast was shown
        assertEquals(activity.getString(R.string.empty_fields_warning), 
                    ShadowToast.getTextOfLatestToast());

        // Verify activity was not finished
        assertFalse(activity.isFinishing());
    }

    @Test
    public void testSaveNoteWithEmptyContent() {
        editTextNoteName.setText("Test Note");
        editTextNoteContent.setText("");
        buttonSave.performClick();

        // Verify warning toast was shown
        assertEquals(activity.getString(R.string.empty_fields_warning), 
                    ShadowToast.getTextOfLatestToast());

        // Verify activity was not finished
        assertFalse(activity.isFinishing());
    }

    @Test
    public void testSaveNoteWithWhitespaceOnly() {
        editTextNoteName.setText("   ");
        editTextNoteContent.setText("   ");
        buttonSave.performClick();

        // Verify warning toast was shown
        assertEquals(activity.getString(R.string.empty_fields_warning), 
                    ShadowToast.getTextOfLatestToast());

        // Verify activity was not finished
        assertFalse(activity.isFinishing());
    }

    @Test
    public void testOverwriteExistingNote() {
        String testNoteName = "Test Note";
        String originalContent = "Original Content";
        String newContent = "New Content";

        // Save original note
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(testNoteName, originalContent);
        editor.apply();

        // Save new note with same name
        editTextNoteName.setText(testNoteName);
        editTextNoteContent.setText(newContent);
        buttonSave.performClick();

        // Verify note was overwritten
        String savedContent = prefs.getString(testNoteName, "");
        assertEquals(newContent, savedContent);
    }
}