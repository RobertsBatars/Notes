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
│       ├── DeleteNoteActivityTest.java
│       └── TestNotesApplication.java
└── androidTest/                 # UI Tests
    └── java/com/example/notes/
        └── NotesUITest.java
```

## Test Coverage

### Unit Tests

#### MainActivityTest
- Initial empty state verification
- Loading notes from SharedPreferences
- Menu item click handling (Add Note, Delete Note)
- Note item click handling
- Activity recreation

#### AddNoteActivityTest
- Valid note saving
- Empty input validation
- Whitespace-only input validation
- Existing note overwriting
- Toast message verification

#### ViewNoteActivityTest
- Note content display
- Action bar configuration
- Navigation handling
- Missing note handling
- Non-existent note handling

#### DeleteNoteActivityTest
- Initial notes list display
- Note deletion
- Multiple note deletion
- Action bar configuration
- List item layout verification

## Running Tests

### Unit Tests

The unit tests use Robolectric framework and can be run without an Android device. The tests are configured using both robolectric.properties and build.gradle:

#### Robolectric Configuration

```properties
# app/src/test/resources/robolectric.properties
sdk=29
application=com.example.notes.TestNotesApplication
manifest=AndroidManifest.xml
resourceDir=../../src/main/res
assetDir=../../src/main/assets
packageName=com.example.notes
```

```gradle
// app/build.gradle
testOptions {
    unitTests {
        includeAndroidResources = true
        all {
            systemProperty 'robolectric.manifest', project.android.sourceSets.main.manifest.srcFile.absolutePath
            systemProperty 'robolectric.packageName', 'com.example.notes'
            systemProperty 'robolectric.resourceDir', project.android.sourceSets.main.res.srcDirs[0].absolutePath
            systemProperty 'robolectric.assetDir', project.android.sourceSets.main.assets.srcDirs[0].absolutePath
        }
    }
}
```

#### Test Application

The tests use a custom Application class for proper theme handling:

```java
// app/src/test/java/com/example/notes/TestNotesApplication.java
public class TestNotesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setTheme(android.R.style.Theme_Material_Light);
    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(android.R.style.Theme_Material_Light);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
```

#### Running the Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.notes.MainActivityTest"
./gradlew test --tests "com.example.notes.AddNoteActivityTest"
./gradlew test --tests "com.example.notes.ViewNoteActivityTest"
./gradlew test --tests "com.example.notes.DeleteNoteActivityTest"
```

### UI Tests

The UI tests use Espresso framework and require a connected device or emulator:

```bash
# Run all UI tests
./gradlew connectedAndroidTest

# Run specific test class
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.example.notes.NotesUITest
```

## UI Test Coverage

### NotesUITest
- Note Creation
  - Adding notes with valid input
  - Empty input validation
  - Long content handling
  - Toast message verification

- Note Viewing
  - Opening and reading notes
  - Content verification
  - Navigation between screens

- Note Deletion
  - Single note deletion
  - Multiple note deletion
  - List updates verification

- Navigation
  - Screen transitions
  - Back button behavior
  - Menu item interactions

## Test Dependencies

```gradle
dependencies {
    // Unit testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.11.1'
    testImplementation 'androidx.test:core:1.5.0'
    testImplementation 'androidx.test.ext:junit:1.1.5'
    testImplementation 'org.mockito:mockito-core:5.3.1'
    testImplementation 'androidx.test:rules:1.5.0'
    testImplementation 'androidx.test:runner:1.5.2'
    testImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    testImplementation 'androidx.test.espresso:espresso-intents:3.5.1'

    // UI testing
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
    androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
}
```

## Best Practices

1. Run tests in a clean environment:
   ```bash
   ./gradlew clean test
   ./gradlew clean connectedAndroidTest
   ```

2. Keep tests isolated:
   - Each test should clean up its data
   - Use @Before and @After methods
   - Clear SharedPreferences between tests

3. Handle activity lifecycle:
   - Properly create and destroy activities
   - Set themes before finding views
   - Resume activities after initialization

4. Avoid flaky tests:
   - Wait for views to be visible
   - Handle keyboard properly
   - Disable animations in UI tests

## Common Issues

1. Theme-related errors:
   - Set theme before accessing views
   - Use AppCompat themes
   - Initialize views after theme setting

2. SharedPreferences conflicts:
   - Clear data before each test
   - Use unique test data
   - Clean up after tests

3. Activity lifecycle issues:
   - Follow proper create-resume-pause-destroy cycle
   - Clean up resources in tearDown
   - Handle configuration changes

4. UI test stability:
   - Use ViewActions and ViewMatchers
   - Handle asynchronous operations
   - Verify view state before interactions