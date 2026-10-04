# CSD 420 Module 10.2 — Fan Manager

Sam Dirr · October 4, 2026

## Open and run in NetBeans

1. Start NetBeans using `/Users/sam/Applications/NetBeans/Start-CSD420.command`. This also starts the existing local MySQL server if needed.
2. Choose **File → Open Project** and select `/Users/sam/Documents/GitHub/csd-420/module-10/FanManager`.
3. Right-click **CSD420 Module 10.2 Fan Manager** and choose **Run**.
4. Enter the assignment-provided password for `student1` when prompted.
5. Enter ID `1`, `2`, or `3` and click **Display**.
6. Edit First Name, Last Name, or Favorite Team, then click **Update**.
7. Click **Display** again to verify the saved values. Restore practice values after manual testing if desired.

The database is `databasedb`. This Mac uses MySQL on port **3307**. The instructor may use port **3306**; override the URL with Ant's `-Ddb.url=jdbc:mysql://localhost:3306/databasedb` when needed. No source edits are required. The program accepts `CSD420_DB_PASSWORD` as an alternative to its password prompt; credentials are not committed in source files.

## Files and behavior

- `src/FanManager.java`: Swing interface, validation, Display/Update listeners, application entry point, connection lifecycle.
- `src/FanStore.java`: fan data record and the two database operations.
- `src/JdbcFanStore.java`: parameterized SELECT and UPDATE statements scoped to the supplied ID. No schema creation or deletion.
- `test/FanManagerTest.java`: clicks the real Swing buttons on the Swing event thread with a controlled fake database; checks fields, validation, errors, and recovery.
- `test/JdbcFanStoreTest.java`: tests the real database in a transaction, rolling back all inserted test rows and updates. Requires InnoDB.
- `setup-local.sql`: optional, separate home database setup, already run on this Mac. Do not run it against the instructor's populated database.
- `lib/mysql-connector-j-8.0.33.jar`: local driver reused from Module 9. Repository rules ignore JARs; another computer must supply Connector/J in this folder.
- `nbproject/project.xml` and `build.xml`: NetBeans free-form Ant project configuration, matching Module 9.

ID accepts the full signed 32-bit integer range. The three text fields accept up to 25 characters; empty values are permitted by the supplied schema. Changing the ID requires displaying that record before updating, preventing stale fields from overwriting a different fan. Missing records and database failures produce feedback in the window. Queries have timeouts; during a slow query the small synchronous Swing application can briefly pause.

## Automated tests

In NetBeans, right-click the project and choose **Test** for the interface suite. Alternatively, expand `build.xml` and run its `test` target.

For both suites in Terminal (enter the supplied password when prompted below):

```zsh
cd /Users/sam/Documents/GitHub/csd-420/module-10/FanManager
export JAVA_HOME=/Users/sam/Applications/Java/jdk-21.0.11+10/Contents/Home
read -s 'CSD420_DB_PASSWORD?Database password: '
export CSD420_DB_PASSWORD
/Users/sam/Applications/NetBeans/netbeans/extide/ant/bin/ant test integration-test
unset CSD420_DB_PASSWORD
```

Verified October 4, 2026: **30 interface/validation checks**, **9 MySQL integration checks**, **BUILD SUCCESSFUL**. Simulated database-outage messages in the interface test output are expected. Test records were rolled back and the three practice records remained intact. The Swing panel was rendered offscreen for a layout check. Launching a desktop window from the automation process failed, so opening the window in NetBeans remains a manual check.

## Before submission

Review Module 10 in GitHub Desktop, commit and push using your usual workflow, then create a ZIP if the submission page requires it. Include the Java program and test source. The supplied instructions do not specify a ZIP or screenshot requirement. Commit, push, and packaging have not been performed automatically.
