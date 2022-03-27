### computer requirements
* Java Software Developer's Kit (SE) 1.8 or higher
* sbt 1.3.4 or higher. Note: if you downloaded this project as a zip file from <https://developer.lightbend.com>, the file includes an sbt distribution for your convenience.

To check your Java version, enter the following in a command window:

```bash
java -version
```

To check your sbt version, enter the following in a command window:

```bash
sbt sbtVersion
```

If you do not have the required versions, follow these links to obtain them:

* [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [sbt](http://www.scala-sbt.org/download.html)


### Demo Installation
* Launch the demo using `sbt run`

### Demo requirements
#### Completed
- create DemoController
- install i18n
- create UserController
- create User model
- create db migrations

#### todo
- create validation for the UserController
- translate validation
- install JWT
- create a middleware
- create PostController
- create Post model
- create db seeds
- add .env for secrets
- create `user has many posts` relationship
- create demos for joins, db transactions, logs
- create some tests
- figure how to reset the test db before each test
- figure how to run the tests as transactions, so the data is not commited in the db
- install swagger : https://github.com/iheartradio/play-swagger