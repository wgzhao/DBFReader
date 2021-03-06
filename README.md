DBFReader
=========

An easy-to-use tool to show and convert the content of dBASE III, IV, and 5.0 files. It reads dBASE databases and shows the content with an option to convert it to an SQL file.
Written in Java.

Features
--------

* Read dBASE (III, IV, and 5.0) databases and view its content
* Search the database
* Export the database to an SQL file

Requirements
------------

* Java 1.6+

Compile
--------

```shell
git clone https://github.com/wgzhao/dbfreader.git
cd dbfreader
mvn clean package
```

Run
----

```shell
java -jar target/dbf-reader-*.jar
```

Screen shots
------------

![Screen shot 1](https://raw.github.com/wgzhao/DBFReader/master/ScreenShot1.png "Screen shot 1")

![Screen shot 2](https://raw.github.com/wgzhao/DBFReader/master/ScreenShot2.png "Screen shot 2")
