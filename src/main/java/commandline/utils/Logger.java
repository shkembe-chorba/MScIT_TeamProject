package commandline.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * Sets up a Logger which is enabled as long as the object remains in scope. Usual use will be to
 * call the static method Logger.log(message), which will output to the target destination file.
 */
public class Logger {

        private static final String LOGGER_DIVIDER_STRING = "--------";
        private static java.util.logging.Logger javaLogger;
        private String outputFilepath;

        /**
         * Creates a logger object which will remain active as long as it is in scope. Best used
         * within the main method to ensure it is not garbage collected.
         *
         * @param outputFilepath The target logfile destination path.
         * @param loggerName     A unique identifier to be used if using other
         *                       java.util.logging.Logger objects.
         */
        public Logger(String outputFilepath, String loggerName) {
                javaLogger = java.util.logging.Logger.getLogger(loggerName);

                this.outputFilepath = outputFilepath;

                // Remove default logging behaviour which outputs to System.err
                LogManager.getLogManager().reset();

        }

        /**
         * The same as {@link #Logger(String, String)}, but defaults the logger name to
         * "commandline".
         *
         * @param outputFilepath The target logfile destination path.
         */
        public Logger(String outputFilepath) {
                this(outputFilepath, "commandline");
        }

        /**
         * Creates the target log file.
         *
         * @throws SecurityException When there is a security exception while creating the file.
         * @throws IOException       When there is a filesystem problem while creating the file.
         */
        public void enable() throws SecurityException, IOException {

                Handler loggerHandler = new FileHandler(outputFilepath);
                // The default format for messages is to append LOGGER_DIVIDER_STRING
                loggerHandler.setFormatter(new Formatter() {
                        @Override
                        public String format(LogRecord record) {
                                return String.format("%s\n%s\n", record.getMessage(),
                                                LOGGER_DIVIDER_STRING);
                        }
                });
                javaLogger.addHandler(loggerHandler);
                javaLogger.setLevel(java.util.logging.Level.INFO);
        }

        /**
         * Disable logging globally for this logger.
         */
        public void disable() {
                javaLogger.setLevel(java.util.logging.Level.OFF);
        }

        /**
         * A static method which outputs a log to the target log file. This is static so it can be
         * used in many different classes while there is the 'master object' within the application
         * scope.
         *
         * @param logMessage
         */
        public static void log(String logMessage) throws NullPointerException {

                if (javaLogger != null) {
                        javaLogger.info(logMessage);
                } else {
                        throw new NullPointerException(
                                        "No Logger object has been created in global scope");
                }

        }



}
