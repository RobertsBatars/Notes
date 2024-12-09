package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
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

class TestMenuItem implements MenuItem {
    private final int itemId;

    TestMenuItem(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public int getGroupId() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public MenuItem setTitle(CharSequence title) {
        return this;
    }

    @Override
    public MenuItem setTitle(int title) {
        return this;
    }

    @Override
    public CharSequence getTitle() {
        return "";
    }

    @Override
    public MenuItem setTitleCondensed(CharSequence title) {
        return this;
    }

    @Override
    public CharSequence getTitleCondensed() {
        return "";
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        return this;
    }

    @Override
    public MenuItem setIcon(int iconRes) {
        return this;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    @Override
    public MenuItem setIntent(Intent intent) {
        return this;
    }

    @Override
    public Intent getIntent() {
        return null;
    }

    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return this;
    }

    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        return this;
    }

    @Override
    public char getNumericShortcut() {
        return 0;
    }

    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        return 0;
    }

    @Override
    public MenuItem setCheckable(boolean checkable) {
        return this;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public MenuItem setChecked(boolean checked) {
        return this;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public MenuItem setVisible(boolean visible) {
        return this;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public MenuItem setEnabled(boolean enabled) {
        return this;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean hasSubMenu() {
        return false;
    }

    @Override
    public SubMenu getSubMenu() {
        return null;
    }

    @Override
    public MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        return this;
    }

    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
    }

    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        return this;
    }

    @Override
    public MenuItem setActionView(int resId) {
        return this;
    }

    @Override
    public View getActionView() {
        return null;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return this;
    }

    @Override
    public ActionProvider getActionProvider() {
        return null;
    }

    @Override
    public boolean expandActionView() {
        return false;
    }

    @Override
    public boolean collapseActionView() {
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        return false;
    }

    @Override
    public MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
        return this;
    }
}