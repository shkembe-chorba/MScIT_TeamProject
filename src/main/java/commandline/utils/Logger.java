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
 *
 * This class aims to encapsulate a lot of the java util Logger setup.
 */
public class Logger {

        // The global logger name for fetching from a singleton using a static method.
        public static final String LOGGER_NAME = "commandline";
        // The string which seperates each log.
        public static final String LOGGER_DIVIDER_STRING = "--------";

        private java.util.logging.Logger javaLogger;
        private String outputFilepath;

        /**
         * Creates a logger object which will remain active as long as it is in scope. Used within
         * the main method to ensure it is not garbage collected.
         *
         * @param outputFilepath The target logfile destination path.
         */
        public Logger(String outputFilepath) {
                // Remove default logging behaviour which outputs to System.err
                LogManager.getLogManager().reset();

                // Store this, so it is inside the object to prevent it being garbage collected
                javaLogger = java.util.logging.Logger.getLogger(LOGGER_NAME);

                this.outputFilepath = outputFilepath;
        }

        /**
         * Creates the target log file and enables printing to it using the Logger static methods.
         *
         * @throws SecurityException When there is a security exception while creating the file.
         * @throws IOException       When there is a filesystem problem while creating the file.
         */
        public void enable() throws SecurityException, IOException {

                Handler loggerHandler = new FileHandler(outputFilepath);
                // The default format for messages is to append LOGGER_DIVIDER_STRING
                // and new lines after each log.
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
         * A static method which outputs a log to the target log file. This is static so it can be
         * used in many different classes while there is the 'master object' within the application
         * scope.
         *
         * @param logMessage The message to include in the log
         */
        public static void log(String logMessage) {
                java.util.logging.Logger.getLogger(LOGGER_NAME).info(logMessage);
        }

        /**
         * A static method which outputs a log to the target log file. This is static so it can be
         * used in many different classes while there is the 'master object' within the application
         * scope.
         *
         * @param headerMessage A heading to include above the log
         * @param logMessage    The body of the log message
         */
        public static void log(String headerMessage, String logMessage) {
                log(String.format("%s\n\n%s", headerMessage, logMessage));
        }

}
