package commandline.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class Logger {

        private static final String LOGGER_DIVIDER_STRING = "--------";
        private static java.util.logging.Logger javaLogger;
        private static Handler loggerHandler;
        private static String outputFilepath;

        public Logger(String outputFilepath, String loggerName) {

                this.outputFilepath = outputFilepath;
                // Remove default logging output to System.err
                LogManager.getLogManager().reset();
                javaLogger = java.util.logging.Logger.getLogger(loggerName);
        }

        public Logger(String outputFilepath) {
                this(outputFilepath, "commandline");
        }

        public void enable() throws SecurityException, IOException {
                // Create a Logger object in main scope (so it isn't garbage collected).
                loggerHandler = new FileHandler(outputFilepath);
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

        public void disable() {
                javaLogger.setLevel(java.util.logging.Level.OFF);
        }

        // As a static method for utility
        public static void log(String logMessage) {
                javaLogger.info(logMessage);
        }



}
