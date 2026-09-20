# CSD 420 Module 7 Assignment 7.2

Sam Dirr | September 20, 2026

## Files

- `DirrCircleStyles.java`: JavaFX application with four circles.
- `mystyle.css`: shared white fill/black stroke class and red/green IDs.
- `DirrCircleStylesTest.java`: executable tests of circle count, stylesheet loading, style classes, IDs, computed fills, and black outlines.
- `run.command`: local Mac launcher using the existing IntelliJ Java 21 runtime and JavaFX SDK 21.0.12.

## Run on Sam's Mac

Double-click `run.command` to compile and open the application. From Terminal, run tests with:

```sh
cd /Users/sam/Documents/GitHub/csd-420/module-7
./run.command test
```

A successful test run prints `All circle style tests passed.` and exit status 0. An unsuccessful check throws an AssertionError and exits with a nonzero status. The tests require a graphical desktop session.

The launcher creates compiled files in a temporary directory and removes them afterward. For a different installation, set `ASSIGNMENT_JAVA_HOME` to a JDK directory and `ASSIGNMENT_FX_LIB` to a JavaFX SDK lib directory.

## IntelliJ configuration

Use Java 21 and add the JavaFX SDK lib directory as a project library. Mark module-7 as a Sources Root. Run DirrCircleStyles with VM options:

```text
--module-path /Users/sam/Documents/JavaFX/javafx-sdk-21.0.12/lib --add-modules javafx.controls
```

Keep mystyle.css at the root of the runtime classpath beside the compiled classes. IntelliJ must copy the CSS resource into its compilation output; the included launcher does this automatically.

## How it works

All circles receive the plaincircle style class, giving them white fill and black stroke. The third and fourth circles receive redcircle and greencircle IDs. The ID selectors override their fill colors while retaining the class's black stroke. Colors are defined in the external CSS, not in Java.

The unavailable textbook image has not been reproduced. The layout follows the written requirements: two white circles, a red circle, and a green circle in a row.

## Verification and submission

Both Java sources compiled successfully with Java 21 and JavaFX 21.0.12. Graphical testing in the Codex execution session could not start because no macOS screen was available. Run the tests locally before submission; their runtime result has not yet been verified.

Capture the running application, the successful test output, and Java/JavaFX configuration in IntelliJ. Combine the screenshots in Dirr-Assignment_7_2_CSD420.docx with your name, date, and assignment number. The screenshot document is still pending. Check the instructor's Documentation Requirements when available.

Review the files in GitHub Desktop, commit and push, then create dirr_mod_7_csd420.zip containing the required Java, CSS, and screenshot document files. Include the repository URL in the submission comments.
