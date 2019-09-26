# App-Exception-Logger-Demo

Logging a UncaugthException in a File or Log console.

Big Thanks to Mr.Rohit Surwase for having such an advanced library of UCE-Handler for reference.

Here, concerning `uce-handler` library, [<b>uncaught_logger</b>][2] contains only <i>console</i> log & <i>file</i> log features

## Usage

For logging exceptions in a file need `WRITE_EXTERNAL_STORAGE` permission, to invoke the <b>FileLogger</b>

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

In `onCreate(...)` of the Application class, you need to attach loggers in uce builder and call `caught()` method

```java

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ConsoleLogger consoleLogger = ConsoleLogger.invoke("ConsoleLogger", ConsoleLogger.DEDUG);
        FileLogger fileLogger = new FileLogger.FileLoggerBuilder()
                .autoGenerateLog(FileLogger.DEFAULT_MAX_LOG_LIMIT)
                .build(this.getPackageName());

        UCE.builder(this)
                .attachLogger(consoleLogger)
                .attachLogger(fileLogger)
                .caught();

    }
}

```

# Logger Features

* <b>Console Logger</b>

Consists of  differents following modes to Logs

```java

ConsoleLogger.DEDUG
ConsoleLogger.ERROR
ConsoleLogger.INFO
ConsoleLogger.VERBOSE
ConsoleLogger.WARNING
ConsoleLogger.ASSERT

```
* <b>File Logger</b>

<b>autoGenerateLog(FileLogger.DEFAULT_MAX_LOG_LIMIT)</b> : is used to produce mutiple-file logs, with limitation of file generation and store in following path

`ExternalStorage/Android/uncaught_logger/apps/<package-name>/Log_yyyy-MM-dd hh:mm:ss:a.txt`

<b>customGenerateLog("/uncaught_logger/","logger.txt")</b> : is used to produce single-file logs, and store in following path

`ExternalStorage/uncaught_logger/logger.txt`

<b>isHidden()</b> : is optinal to hide the logs files.

# Implementation
Download the [@uncaught_logger-release.aar][3] file and copy to the libs folder, libs folder must be added to `project-level.gradle` file

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
}

```
Add @aar file dependancy in `app-level.gradle` file

```gradle

dependencies {
implementation(name:'uncaught_logger-release', ext:'aar')
}

```




 [1]: https://github.com/RohitSurwase/UCE-Handler
 [2]:https://github.com/DineshParmar65412369/App-Exception-Logger-Demo/tree/master/uncaught_logger
 [3]:https://github.com/DineshParmar65412369/App-Exception-Logger-Demo/raw/master/uncaught_logger/aar/uncaught_logger-release.aar

