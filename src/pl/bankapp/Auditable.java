package pl.bankapp;

/**
 * interfejs dla rzeczy, które można przetestować w audycie.
 * obiekty potrafią zapisać swój stan przez loggera.
 */
public interface Auditable {

    /**
     * robi audyt obiektu i zapisuje stan w loggerze.
     *
     * @param logger logger do zapisu info z audytu (może być null)
     */
    void audit(TransactionLogger logger);
}