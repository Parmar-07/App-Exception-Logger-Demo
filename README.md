# App-Exception-Logger-Demo

Logging a UncaugthException in a File or Log console.

Big Thanks to Mr.Rohit Surwase for having such an advanced library of UCE-Handler for reference.

Here, concerning `uce-handler` library, [<b>uncaught_logger</b>][2] contains only <i>console</i> log & <i>file</i> log features

## Usage

For logging exceptions in a file need `WRITE_EXTERNAL_STORAGE` permission, to invoke the <b>FileLogger</b> 

```xml 
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

In on `onCreate(...)` of the Application class instance, you need to attach loggers in uce builder and call `caught()`

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

To produce an auto file logger with using the  `fileLogger.autoGenerateLog(FileLogger.DEFAULT_MAX_LOG_LIMIT)` method to pass the value of the limitation as a parameter and log file will generate in this path :

<b><i>`ExternalStorage/Android/data/<package-name>/errorLog/Log_yyyy-MM-dd hh:mm:ss:a.txt`</i></b>

 To produce a custom file path for single file log using`fileLogger.customGenrateLog("/uncaught_logger/","logger.txt")` method passing the path & filename as a parameters

<b><i>`ExternalStorage/uncaught_logger/logger.txt`</i></b>

# Implementation
Download the [@uncaught_logger-release.aar][3] file and copy to the libs folder, libs folder must be added the `project-level.gradle` file 

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
 
