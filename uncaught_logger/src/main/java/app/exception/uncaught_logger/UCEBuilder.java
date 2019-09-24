package app.exception.uncaught_logger;

import android.app.Application;

import app.exception.uncaught_logger.logger.AndroidLogger;


public interface UCEBuilder {

    /**
     * @param application invoking the application instance to initialize
     * @return
     */
    UCEBuilder init(Application application);

    /**
     * @param logger Passing {@linkplain app.exception.uncaught_logger.logger.ConsoleLogger,app.exception.uncaught_logger.logger.FileLogger}
     *               Logger System
     * @return {@linkplain UCE}
     */
    UCE attachLogger(AndroidLogger logger);

    void invalidate();

}
