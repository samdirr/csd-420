# CSD 420 Module 8 Assignment 8.2

Sam Dirr | September 20, 2026

## Run

Double-click run.command to compile and launch SamThreeThreads on Sam's Mac.
The launcher uses IntelliJ's bundled Java 21 runtime and the installed JavaFX
21.0.12 SDK. It stores compiled classes in a temporary directory and removes
them afterward. Override ASSIGNMENT_JAVA_HOME and ASSIGNMENT_FX_LIB for another
installation.

To run the automated tests from Terminal:

```sh
cd /Users/sam/Documents/GitHub/csd-420/module-8
./run.command test
```

## Program design

SamThreeThreads is the JavaFX application. SamCharacterGenerator creates three
worker threads named Letters, Digits, and Symbols. Each worker generates 10,000
random characters from its own alphabet. Semaphores give the threads alternating
turns, ensuring the output is mixed instead of grouped by thread.

Each generated character is immediately sent to the display callback. The callback
uses Platform.runLater to append that single character safely on the JavaFX thread.
A latch makes the worker wait until the append completes before passing its turn.
No worker accumulates its output into a batch. JavaFX controls its own screen
repaint timing, so the display may paint multiple changes in a screen refresh.
The completed text contains exactly 30,000 characters. Closing the window
interrupts the workers.

## Tests

SamThreeThreadsTest runs without a graphical display and checks:

- Random character membership for all three alphabets and a single-character alphabet.
- Three distinct worker threads, 30,000 callbacks, and exactly 10,000 characters per category.
- Alternating categories throughout the output.
- Completion, duplicate start rejection, timeout, and cancellation.
- Invalid counts, null output callbacks, invalid alphabets, and invalid lifecycle calls.

Java compilation and these automated generator tests passed. The JavaFX window
and its lifecycle still require a local visual check because this Codex execution
session cannot access the macOS display. These tests do not verify the text area.

## IntelliJ

Use a Java 21 SDK, add the installed JavaFX lib folder as a project library, and
mark module-8 as a Sources Root. Run SamThreeThreads with these VM options:

```text
--module-path /Users/sam/Documents/JavaFX/javafx-sdk-21.0.12/lib --add-modules javafx.controls
```

## Remaining submission steps

Run the application and confirm the completion message says 30,000 characters.
Capture the window, successful test output, and Java/JavaFX configuration. Combine
the screenshots in Dirr-Assignment_8_2_CSD420.docx with your name, date, and
assignment number. The screenshot Word document is pending. Check any additional
instructor Documentation Requirements when available.

Review, commit, and push the assignment through GitHub Desktop. Then create
 dirr_mod_8_csd420.zip with the Java source files and required screenshot document.
The ZIP name follows the assignment example. Include the GitHub repository URL
in the submission comments.
