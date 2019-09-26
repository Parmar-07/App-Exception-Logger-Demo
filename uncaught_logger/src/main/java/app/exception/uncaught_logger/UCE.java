package app.exception.uncaught_logger;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;

import app.exception.uncaught_logger.logger.AndroidLogger;
import app.exception.uncaught_logger.logger.ConsoleLogger;
import app.exception.uncaught_logger.logger.FileLogger;


public class UCE implements UCEBuilder, FileLogger.OnFileError {


    private UCE() {
    }


    private Application application;
    private String appPackage = "";
    private AndroidLogger _logger;
    private static WeakReference<Activity> lastActivity = new WeakReference<>(null);


    public static UCEBuilder builder(Application application) {
        return new UCE().init(application);
    }

    @Override
    public UCEBuilder init(Application application) {
        this.application = application;
        appPackage = application.getPackageName();
        this.application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                lastActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
        return this;
    }

    @Override
    public UCE attachLogger(AndroidLogger logger) {
        this._logger = logger;
        if (_logger instanceof FileLogger) {
            ((FileLogger) _logger).withErrorListener(this);
        }
        return this;
    }

    @Override
    public void invalidate() {

        application.unregisterActivityLifecycleCallbacks(null);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        _logger.invalidate();

    }


    public void caught() {

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

                try {


                    ApplicationInfo appInfo = application.getApplicationInfo();
                    String appName = application.getPackageManager().getApplicationLabel(appInfo).toString();
                    PackageInfo packInfo = application.getPackageManager().getPackageInfo(appPackage, 0);
                    _logger.log("****************************************");
                    _logger.log("App Name : " + appName);
                    _logger.log("App Version: " + packInfo.versionName);
                    _logger.log("****************************************");

                    if (lastActivity.get() != null) {
                        _logger.log("Last Activity was : " + Objects.requireNonNull(lastActivity.get().getClass().getSimpleName()));
                    }


                    _logger.log("Unhandled Exception : ");
                    _logger.log(throwable.getClass().getName());

                    _logger.log("Exception Cause: ");
                    if (throwable.getCause()!=null){
                        _logger.log(throwable.getCause().getMessage());
                    }else{
                        _logger.log(throwable.getLocalizedMessage());
                    }

                    _logger.log("Exception Message: ");
                    _logger.log(throwable.getMessage());

                    _logger.log("_____________________________________________");
                    _logger.log("Exception Stacktrace: ");
                    _logger.log(Objects.requireNonNull(Arrays.toString(throwable.getStackTrace())));
                    _logger.log("_____________________________________________");

                    _logger.log("****************************************");

                    invalidate();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    _logger.log("****************PackageManager.NameNotFoundException************************");

                }


            }
        });


    }

    @Override
    public void onError(String error) {
       ConsoleLogger logger = ConsoleLogger.invoke("UCE Error :",ConsoleLogger.INFO);
       logger.log(error);
       logger.invalidate();
       invalidate();
    }
}
