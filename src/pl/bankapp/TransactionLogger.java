package pl.bankapp;

/**
 * odpowiada za zapisywanie logów, info i ostrzeżeń w trakcie działania aplikacji.
 * implementuje {@link AutoCloseable}, wiec działa automatycznie w try-with-resources.
 */
public class TransactionLogger implements AutoCloseable {

    /** nazwa aktualnej sesji logowania. */
    private final String sessionName;

    /** flaga czy logger jest otwarty i działa. */
    private boolean open = true;

    /**
     * tworzy nową sesje loggera.
     *
     * @param sessionName nazwa sesji
     */
    public TransactionLogger(String sessionName) {
        this.sessionName = sessionName;
        System.out.println("Logger [" + sessionName + "] START");
    }

    /**
     * zapisuje zwykłą wiadomosc informacyjną.
     *
     * @param msg treść wiadomości
     * @throws IllegalStateException jeśli logger jest już zamknięty
     */
    public void log(String msg) {
        if (!open) throw new IllegalStateException("Logger jest zamknięty.");
        System.out.println("[INFO] " + msg);
    }

    /**
     * zapisuje ostrzeżenie (np. jak cos poszło nie tak).
     *
     * @param msg treść ostrzeżenia
     * @throws IllegalStateException jeśli logger jest już zamknięty
     */
    public void warn(String msg) {
        if (!open) throw new IllegalStateException("Logger jest zamknięty.");
        System.out.println("[WARN] " + msg);
    }

    /**
     * zamyka logger i zwalnia zasoby.
     * po zamknięciu nie da sie już nic zapisać.
     */
    @Override
    public void close() {
        if (open) {
            open = false;
            System.out.println("Logger [" + sessionName + "] STOP (zasoby zwolnione)");
        }
    }
}