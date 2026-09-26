# CSD 420 — Module 9.2

This Java JDBC test was created because the supplied Commands_Used.zip contains SQL commands but no Java test application.

## Run in NetBeans

1. Double-click `/Users/sam/Applications/NetBeans/Start-CSD420.command`.
2. Choose File > Open Project and select this `DatabaseConnectionTest` folder.
3. If prompted, allow NetBeans to activate its installed Java features.
4. Right-click the project and select Run.
5. Enter the assignment-provided database password when prompted.
6. Confirm the Output pane ends with `SUCCESS: Java connected to MySQL and all checks passed.` and `BUILD SUCCESSFUL`.

The local MySQL 5.7.31 instance listens on 127.0.0.1:3307. The database is `databasedb` and the account is `student1`. Passwords are not stored in source code. The JDBC driver is installed locally in `lib/mysql-connector-j-8.0.33.jar`; the class repository ignores JAR files.

The test reports Java, MySQL and JDBC versions, the database and authenticated user, and the result of a SQL query. A temporary table verifies creation, insertion and retrieval and disappears when the connection closes.

## Submission evidence

Capture NetBeans showing the project and successful Output pane. Expand the Output pane so all result lines are readable. Use Shift-Command-4 to capture the relevant area. Include the screenshot in `Dirr-Assignment_9_2_CSD420.docx`.

The assignment requests a single screenshot document. This source project supports the demonstration; it is not an additional required submission.
