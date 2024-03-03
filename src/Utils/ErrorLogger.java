package Utils;

import java.util.logging.*;

public class ErrorLogger {

    private static final Logger logger = Logger.getLogger(ErrorLogger.class.getName());

    static {
        try {
            // Konfigurera loggfilhanteraren

            // Tar bort log-meddelanden i konsolen.
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            if (handlers[0] instanceof ConsoleHandler) {
                rootLogger.removeHandler(handlers[0]);
            }

            // Skapar en fil där alla loggar hamnar
            FileHandler handler = new FileHandler("all_errors.log");
            logger.addHandler(handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logError(String message) {
        logger.log(Level.WARNING, message);
    }
}
