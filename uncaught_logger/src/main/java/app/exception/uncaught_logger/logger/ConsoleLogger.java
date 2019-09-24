package app.exception.uncaught_logger.logger;

import android.util.Log;

public class ConsoleLogger extends AndroidLogger {


    public static final char DEDUG = 'd';
    public static final char ERROR = 'e';
    public static final char INFO = 'i';
    public static final char VERBOSE = 'v';
    public static final char WARNING = 'w';
    public static final char ASSERT = 'a';


    private char console;
    private String TAG;

    private static ConsoleLogger _logger = null;

    public static ConsoleLogger invoke(String TAG, char console) {

        if (_logger == null) {

            synchronized (ConsoleLogger.class) {

                if (_logger == null) {

                    _logger = new ConsoleLogger(TAG,console);
                }

            }
        }

        return _logger;

    }

    private ConsoleLogger(String TAG, char console) {
        this.console = console;
        this.TAG = TAG;
    }


    @Override
    public void log(String data) {

        switch (console) {
            case DEDUG:
                Log.d(TAG, data);
                break;

            case ERROR:
                Log.e(TAG, data);
                break;

            case WARNING:
                Log.w(TAG, data);
                break;


            case INFO:
                Log.i(TAG, data);
                break;

            case VERBOSE:
                Log.v(TAG, data);
                break;

            case ASSERT:
                System.out.println(data);
                break;


            default:
                Log.d(TAG, data);
                break;
        }

    }

    @Override
    public void invalidate() {
        _logger = null;
        System.gc();
    }

}
