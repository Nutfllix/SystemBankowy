package pl.bankapp;

/**
 * Odpowiada za rejestrowanie i logowanie zdarzeń, komunikatów informacyjnych oraz ostrzeżeń
 * w trakcie trwania sesji aplikacji bankowej.
 * Implementuje interfejs {@link AutoCloseable}, co umożliwia automatyczne zwalnianie zasobów
 * (np. w blokach try-with-resources).
 */
public class TransactionLogger implements AutoCloseable {

    /** Nazwa bieżącej sesji logowania. */
    private final String sessionName;

    /** Flaga określająca, czy logger jest aktualnie otwarty i gotowy do pracy. */
    private boolean open = true;

    /**
     * Tworzy i inicjalizuje nową sesję loggera transakcji.
     *
     * @param sessionName nazwa sesji logowania
     */
    public TransactionLogger(String sessionName) {
        this.sessionName = sessionName;
        System.out.println("Logger [" + sessionName + "] START");
    }

    /**
     * Rejestruje standardowy komunikat informacyjny.
     *
     * @param msg treść wiadomości do zapisu
     * @throws IllegalStateException jeśli logger został już zamknięty
     */
    public void log(String msg) {
        if (!open) throw new IllegalStateException("Logger jest zamknięty.");
        System.out.println("[INFO] " + msg);
    }

    /**
     * Rejestruje komunikat ostrzegawczy (np. odmowa wykonania operacji).
     *
     * @param msg treść ostrzeżenia do zapisu
     * @throws IllegalStateException jeśli logger został już zamknięty
     */
    public void warn(String msg) {
        if (!open) throw new IllegalStateException("Logger jest zamknięty.");
        System.out.println("[WARN] " + msg);
    }

    /**
     * Zamyka sesję loggera i zwalnia zajmowane przez niego zasoby.
     * Po wywołaniu tej metody ponowne próby logowania spowodują rzucenie wyjątku.
     */
    @Override
    public void close() {
        if (open) {
            open = false;
            System.out.println("Logger [" + sessionName + "] STOP (zasoby zwolnione)");
        }
    }
}