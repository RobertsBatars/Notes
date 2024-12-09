package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.widget.ListView;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;
import static android.content.Context.MODE_PRIVATE;

@RunWith(RobolectricTestRunner.class)
@org.robolectric.annotation.Config(
    sdk = {29},
    application = android.app.Application.class,
    manifest = "AndroidManifest.xml"
)
public class MainActivityTest {
    private MainActivity activity;
    private SharedPreferences prefs;
    private ActivityController<MainActivity> controller;
    private ListView listView;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        controller.create();
        activity = controller.get();
        activity.setTheme(android.R.style.Theme_Material_Light);

        prefs = RuntimeEnvironment.getApplication()
                .getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        listView = activity.findViewById(R.id.listViewNotes);

        controller.resume();
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
        prefs.edit().clear().apply();
    }

    @Test
    public void testInitialListViewIsEmpty() {
        assertEquals(0, listView.getAdapter().getCount());
    }

    @Test
    public void testLoadNotesFromPreferences() {
        // Add a test note
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Test Note", "Test Content");
        editor.apply();

        // Recreate activity to reload notes
        controller.pause();
        controller.stop();
        controller.destroy();
        
        controller = Robolectric.buildActivity(MainActivity.class);
        controller.create();
        activity = controller.get();
        activity.setTheme(android.R.style.Theme_Material_Light);
        listView = activity.findViewById(R.id.listViewNotes);
        controller.resume();

        assertEquals(1, listView.getAdapter().getCount());
        assertEquals("Test Note", listView.getAdapter().getItem(0));
    }

    @Test
    public void testAddNoteMenuItemClick() {
        activity.onOptionsItemSelected(new TestMenuItem(R.id.action_add_note));
        
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        
        assertEquals(AddNoteActivity.class.getName(), 
                    nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void testDeleteNoteMenuItemClick() {
        activity.onOptionsItemSelected(new TestMenuItem(R.id.action_delete_note));
        
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent nextStartedActivity = shadowActivity.getNextStartedActivity();
        
        assertEquals(DeleteNoteActivity.class.getName(), 
                    nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void testNoteItemClick() {
        // Add a test note
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Test Note", "Test Content");
        editor.apply();

        // Recreate activity to reload notes
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();

        ListView listView = activity.findViewById(R.id.listViewNotes);
        listView.performItemClick(
            listView.getChildAt(0),
            0,
            listView.getAdapter().getItemId(0));

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        
        assertEquals(ViewNoteActivity.class.getName(), 
                    startedIntent.getComponent().getClassName());
        assertEquals("Test Note", 
                    startedIntent.getStringExtra("note_name"));
    }
}

