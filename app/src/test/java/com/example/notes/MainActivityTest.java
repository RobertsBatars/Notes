package com.example.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;
import static android.content.Context.MODE_PRIVATE;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class)
                .create()
                .resume()
                .get();
        prefs = RuntimeEnvironment.getApplication()
                .getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
    }

    @Test
    public void testInitialListViewIsEmpty() {
        ListView listView = activity.findViewById(R.id.listViewNotes);
        assertEquals(0, listView.getAdapter().getCount());
    }

    @Test
    public void testLoadNotesFromPreferences() {
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

// Helper class for menu item testing
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