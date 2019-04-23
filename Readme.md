# Simple Java Boards

Simple forum engine written in Java.

## Features

### Conversation

 * Top-level categories;
 * Boards (forums) inside categories with unlimited nesting support;
 * Topics inside boards and posts inside topics;
 * Pagination support for topics and posts;
 * Attachments support;
 * Simple image attachments gallery for post; 

### Users & roles system

 * Users list with pagination;
 * User groups support;
 * Different customizable permissions for groups;
 * User profile with avatar support;
 * User registration with email activation;
 * Password reset with optional secret question check;

### UI

 * Mobile support (320x480 and up);
 * Firefox, Chrome, IE10+ capable;

### Administration

 * Full conversation in-place editing support;
 * Categories, Boards (Forums), Topics, Posts CRUD;
 * Profile editing support, include secured password changing and secret answer/question changing;

## Technology stack

### Backend

 * Spring MVC;
 * JPA (Hibernate);
 * Postgresql;
 * Spring Security;

### Frontend

Frontend uses server-side rendering.

 * Freemarker;
 * Bootstrap;
 * Jquery;
 * Font-awesome;

### Build & Devops

 * Maven;
 * Liquibase;
 * maven-cargo-plugin for fast deployment and run;

## Installation

### Database setup

 * Prepare Postgresql database server, ensure it is up and running;
 * Create Postgresql database and user with full permissions on this database;

By default, ```sjb``` is used in configuration as a user name, password and database name. 
You can change this values in ```resources/properties/database.properties``` on your own.
Test environment has its own ```database.properties```.

### Mail setup

You have to setup mail host, port, user and password in ```resources/properties/mail.properties```
to user registration work.

### File uploads

```files.upload.storage.path``` parameter in ```application.properties``` sets absolute path for uploads.
It have to be set to existing directory with rwx access to user who runs app.

### Logging

```log.full.path``` parameter inside ```logback.xml``` and ```logback-test.xml``` sets up path where app writes logs.
These parameters have to be set to existing directory with rwx access to user who runs app.

### Build & Run

Java 11 required for this operations.

```
$ git clone https://github.com/beljaeff/simple-java-boards.git
$ cd simple-java-boards
$ mvn clean install cargo:run
```

After that you can open ```http://localhost:9090/``` in your browser and work with forum.
Default administrator account have login ```Admin``` and password ```password```.

## Screenshots
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/desktop/main.png" />
<br /><br />
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/desktop/board.png" />
<br /><br />
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/desktop/topic.png" />
<br /><br />
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/desktop/sign-in.png" />
<br /><br />
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/desktop/sign-up.png" />
<br /><br />
<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/mobile/main.png" width="300px" />

<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/mobile/board.png" width="300px" />

<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/mobile/topic.png" width="300px" />

<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/mobile/sign-in.png" width="300px" />

<img src="https://raw.githubusercontent.com/beljaeff/simple-java-boards/master/screenshots/mobile/sign-up.png" width="300px" />

## TODO

 * Different moderating levels (global moderators, board moderators, topic moderators);
 * Private messages;
 * Search;
 * Social integration;
 * Karma support;
 * Private boards;
 * Themes support;
 * Permission management;
 * ... and many more :)

## License

GPL.
