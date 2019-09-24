# App-Exception-Logger-Demo

Logging a UncaugthException in a File or Log console.

Big Thanks to <b>Mr.Rohit Surwase</b> to having a such a advacned library of [UCE-Handler][1] for reference.

Here, with respect to `uce-handler` library, [<b>uncaught_logger</b>][2] contains only <i>console</i> log & <i>file</i> log features

## Usage

Need `WRITE_EXTERNAL_STORAGE` permission, <b>FileLogger</b> is invoked

```xml 
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

Invoke the library to use, from `Application` in override the `onCreate(...)` method, we can attach both the logger at a time.
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

- `fileLogger.autoGenerateLog(FileLogger.DEFAULT_MAX_LOG_LIMIT)` generating a auto-log files with limitations
path : <b><i>ExternalStorage/Android/data/<package-name>/errorLog/Log_yyyy-MM-dd hh:mm:ss:a.txt</i></b>


- `fileLogger.customGenrateLog("/uncaught_logger/","logger.txt")` generating a specific single file as given path
path : <b><i>ExternalStorage/uncaught_logger/logger.txt</i></b>

# Download
Download the [@uncaught_logger-release.aar][3] .aar file & implemeted the `app.gradle` file


 [1]: https://github.com/RohitSurwase/UCE-Handler
 [2]:https://github.com/DineshParmar65412369/App-Exception-Logger-Demo/tree/master/uncaught_logger
 [3]:https://github.com/DineshParmar65412369/App-Exception-Logger-Demo/raw/master/uncaught_logger/aar/uncaught_logger-release.aar
 
