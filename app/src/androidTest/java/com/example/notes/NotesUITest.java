package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(AndroidJUnit4.class)
public class NotesUITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = 
        new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void clearNotes() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    @Test
    public void testAddNote() {
        // Click add note button
        onView(withId(R.id.action_add_note))
            .perform(click());

        // Enter note details
        onView(withId(R.id.editTextNoteName))
            .perform(typeText("Test Note"), closeSoftKeyboard());
        onView(withId(R.id.editTextNoteContent))
            .perform(typeText("Test Content"), closeSoftKeyboard());

        // Save note
        onView(withId(R.id.buttonSave))
            .perform(click());

        // Verify note appears in list
        onView(withText("Test Note"))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testViewNote() {
        // First add a note
        onView(withId(R.id.action_add_note))
            .perform(click());
        onView(withId(R.id.editTextNoteName))
            .perform(typeText("View Test Note"), closeSoftKeyboard());
        onView(withId(R.id.editTextNoteContent))
            .perform(typeText("View Test Content"), closeSoftKeyboard());
        onView(withId(R.id.buttonSave))
            .perform(click());

        // Click on the note
        onView(withText("View Test Note"))
            .perform(click());

        // Verify note details are displayed
        onView(withId(R.id.textViewNoteName))
            .check(matches(withText("View Test Note")));
        onView(withId(R.id.textViewNoteContent))
            .check(matches(withText("View Test Content")));
    }

    @Test
    public void testDeleteNote() {
        // First add a note
        onView(withId(R.id.action_add_note))
            .perform(click());
        onView(withId(R.id.editTextNoteName))
            .perform(typeText("Delete Test Note"), closeSoftKeyboard());
        onView(withId(R.id.editTextNoteContent))
            .perform(typeText("Delete Test Content"), closeSoftKeyboard());
        onView(withId(R.id.buttonSave))
            .perform(click());

        // Go to delete screen
        onView(withId(R.id.action_delete_note))
            .perform(click());

        // Click delete button for the note
        onView(withId(R.id.buttonDelete))
            .perform(click());

        // Go back to main screen
        pressBack();

        // Verify note is gone
        onView(withId(R.id.listViewNotes))
            .check(matches(not(hasDescendant(withText("Delete Test Note")))));
    }

    @Test
    public void testEmptyNoteValidation() {
        // Click add note button
        onView(withId(R.id.action_add_note))
            .perform(click());

        // Try to save empty note
        onView(withId(R.id.buttonSave))
            .perform(click());

        // Verify error toast is shown
        onView(withText(R.string.empty_fields_warning))
            .inRoot(withDecorView(not(is(activityRule.getScenario().getResult().getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testNavigationFlow() {
        // Add Note Screen
        onView(withId(R.id.action_add_note))
            .perform(click());
        pressBack();

        // Delete Note Screen
        onView(withId(R.id.action_delete_note))
            .perform(click());
        pressBack();

        // Verify we're back at main screen
        onView(withId(R.id.listViewNotes))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testLongNoteContent() {
        // Click add note button
        onView(withId(R.id.action_add_note))
            .perform(click());

        String longContent = String.join("", java.util.Collections.nCopies(100, "Long "));

        // Enter note details
        onView(withId(R.id.editTextNoteName))
            .perform(typeText("Long Note"), closeSoftKeyboard());
        onView(withId(R.id.editTextNoteContent))
            .perform(typeText(longContent), closeSoftKeyboard());

        // Save note
        onView(withId(R.id.buttonSave))
            .perform(click());

        // Click on the note
        onView(withText("Long Note"))
            .perform(click());

        // Verify long content is displayed correctly
        onView(withId(R.id.textViewNoteContent))
            .check(matches(withText(longContent)));
    }
}