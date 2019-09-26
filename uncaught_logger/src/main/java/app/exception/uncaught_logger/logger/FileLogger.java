package app.exception.uncaught_logger.logger;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FileLogger extends AndroidLogger {

    public static final int DEFAULT_MAX_LOG_LIMIT = 5;


    private static FileLogger _logger = null;

    private File _logFile = null;
    private FileLoggerBuilder builder;
    private OnFileError fileError;

    public static class FileLoggerBuilder {


        private int maxLogFile = 0;
        private String filePath = "";
        private String fileName;
        private String packageName;
        private boolean isHidden = false;

        public FileLoggerBuilder() {
            if (_logger != null) {
                _logger.invalidate();
            }
        }

        public FileLoggerBuilder autoGenerateLog(int limit) {
            maxLogFile = limit;
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:a", Locale.US);
            String strCurrentDate = dateFormat.format(currentDate);
            strCurrentDate = strCurrentDate.replace(" ", "_");
            fileName = "Log_" + strCurrentDate + ".txt";
            return this;
        }

        public FileLoggerBuilder customGenerateLog(String path, String name) {
            filePath = path;
            fileName = name;
            return this;
        }

        public FileLoggerBuilder isHidden() {
            isHidden = true;
            return this;
        }


        public FileLogger build(String packageName) {
            this.packageName = packageName;
            return FileLogger.invoke(this);
        }


    }


    private static FileLogger invoke(FileLoggerBuilder builder) {

        if (_logger == null) {

            synchronized (FileLogger.class) {

                if (_logger == null) {

                    _logger = new FileLogger(builder);
                }

            }
        }

        return _logger;

    }

    public void withErrorListener(OnFileError fileError) {
        this.fileError = fileError;
    }

    private FileLogger(FileLoggerBuilder builder) {
        this.builder = builder;

        if (isSDPresent() && isExternalStorageWritable()) {
            create(builder.filePath, builder.fileName);
        }


    }

    private void createFile(String fileName) {
        try {


            _logFile = new File(builder.filePath + fileName);

            _logFile.createNewFile();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    @SuppressWarnings("deprecation")
    private void createDirs(String paths) {
        File dirs = new File(Environment.getExternalStorageDirectory(), paths);
        if (!dirs.exists()) {
            dirs.mkdirs();
        } else {

            if (builder.maxLogFile != 0) {
                int files = Objects.requireNonNull(dirs.listFiles()).length;

                if (files > builder.maxLogFile) {
                    deleteRecursive(dirs);
                    createDirs(paths);
                }
            }

        }

        builder.filePath = dirs.getAbsolutePath() + File.separator;


    }

    private void createLogFile(String name) {
        createFile((name == null || name.isEmpty()) ? builder.fileName : name);
    }

    private void createLogFileDirs(String dirs) {

        if (dirs == null || dirs.isEmpty()) {
            builder.filePath = File.separator
                    + "Android/uncaught_logger/apps/"
                    + builder.packageName
                    + File.separator;

            if (builder.isHidden)
                builder.filePath = builder.filePath.replace("uncaught_logger", ".uncaught_logger");


        } else {
            builder.filePath = dirs;
            if (builder.isHidden)
                builder.filePath = builder.filePath.replace(builder.filePath, "."+builder.filePath);

        }

        createDirs(builder.filePath);
    }


    private void create(String dirs, String fileName) {
        createLogFileDirs(dirs);
        createLogFile(fileName);
    }


    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    private boolean isSDPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    private boolean isIOError = false;

    private void fileLog(String error) {

        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(_logFile, true));
            buf.append(error);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (fileError != null)
                fileError.onError(e.getMessage());
            else
                Log.e("AndroidLogger", "This app does not have write storage permission to save log file.");
            isIOError = true;
        }

    }

    @Override
    public void log(String data) {

        if (isIOError)
            return;

        fileLog(data);
    }

    @Override
    public void invalidate() {
        _logger = null;
        System.gc();
    }

    public interface OnFileError {
        void onError(String error);
    }

}
