# Notes App Testing Documentation

This document describes how to run the tests for the Notes Android application.

## Project Structure

The test files are organized as follows:

```
app/src/
├── test/                        # Unit Tests
│   └── java/com/example/notes/
│       ├── MainActivityTest.java
│       ├── AddNoteActivityTest.java
│       ├── ViewNoteActivityTest.java
│       └── DeleteNoteActivityTest.java
└── androidTest/                 # UI Tests
    └── java/com/example/notes/
        └── NotesUITest.java
```

## Unit Tests

The unit tests use Robolectric framework to test the application logic without needing an Android device or emulator. They cover:

- MainActivity
  - Initial empty state
  - Loading notes from preferences
  - Menu item clicks
  - Note item clicks

- AddNoteActivity
  - Saving valid notes
  - Empty input validation
  - Whitespace-only validation
  - Overwriting existing notes

- ViewNoteActivity
  - Note display
  - Action bar configuration
  - Navigation
  - Edge cases (non-existent notes)

- DeleteNoteActivity
  - Initial notes list
  - Note deletion
  - Multiple note deletion
  - UI elements verification

### Running Unit Tests

To run all unit tests:
```bash
./gradlew test
```

To run tests for a specific class:
```bash
./gradlew test --tests "com.example.notes.MainActivityTest"
./gradlew test --tests "com.example.notes.AddNoteActivityTest"
./gradlew test --tests "com.example.notes.ViewNoteActivityTest"
./gradlew test --tests "com.example.notes.DeleteNoteActivityTest"
```

## UI Tests

The UI tests use Espresso framework to test the user interface interactions. They cover:

- Note Creation Flow
  - Adding a new note
  - Input validation
  - Saving and displaying

- Note Viewing Flow
  - Opening note details
  - Content verification
  - Navigation

- Note Deletion Flow
  - Deleting notes
  - UI updates
  - List state verification

- Navigation Flow
  - Screen transitions
  - Back button behavior

### Running UI Tests

To run all UI tests (requires a connected device or emulator):
```bash
./gradlew connectedAndroidTest
```

To run tests for a specific class:
```bash
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.notes.NotesUITest
```

## Test Dependencies

The following testing dependencies are used:

```gradle
dependencies {
    // Unit testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.11.1'

    // UI testing
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## Best Practices

1. Always run unit tests before UI tests
2. Ensure the emulator/device has enough storage space
3. Keep the device screen on during UI tests
4. Clear app data before running tests
5. Run tests in a clean environment

## Common Issues

1. UI tests failing due to keyboard:
   - Use `closeSoftKeyboard()` after input operations

2. Flaky tests due to animations:
   - Disable animations in developer options

3. Tests timing out:
   - Increase test timeout in gradle.properties

4. Device-specific issues:
   - Test on multiple API levels and screen sizes