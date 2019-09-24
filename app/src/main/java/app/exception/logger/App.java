package app.exception.logger;

import android.app.Application;

import app.exception.uncaught_logger.UCE;
import app.exception.uncaught_logger.logger.ConsoleLogger;
import app.exception.uncaught_logger.logger.FileLogger;

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
