package pl.bankapp;

/**
 * Interfejs reprezentujący komponenty, które mogą poddać się procedurze audytu.
 * Obiekty implementujące ten interfejs potrafią zarejestrować swój bieżący stan
 * za pomocą dostarczonego loggera transakcji.
 */
public interface Auditable {

    /**
     * Przeprowadza audyt obiektu i rejestruje jego aktualny stan za pomocą loggera.
     *
     * @param logger logger służący do zapisu informacji z audytu (może być null)
     */
    void audit(TransactionLogger logger);
}