package app.exception.uncaught_logger.logger;

public abstract class AndroidLogger {

    /**
     * @param data is to log in Logger System
     */
    public abstract void log(String data);

    /**
     * clear the logger from memoryLeak
     */
    public abstract void invalidate();

}
