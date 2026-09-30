package pl.bankapp;

/**
 * reprezentuje konto bieżące, które dziedziczy po {@link Account}.
 * pobiera dodatkową, stałą opłatę za każdą wypłatę gotówki.
 */
public class CheckingAccount extends Account {

    /** stała opłata pobierana przy każdej wypłacie. */
    private final double withdrawalFee;

    /**
     * tworzy nowe konto bieżące z właścicielem, saldem startowym i opłatą za wypłatę.
     *
     * @param owner nazwa właściciela (nie może być pusta ani null)
     * @param initialBalance początkowa kasa na koncie (nie może być ujemna)
     * @param withdrawalFee opłata za wypłatu (nie może być ujemna)
     * @throws IllegalArgumentException jeśli opłata jest ujemna
     */
    public CheckingAccount(String owner, double initialBalance, double withdrawalFee) {
        super(owner, initialBalance);
        if (withdrawalFee < 0) throw new IllegalArgumentException("Opłata nie może być ujemna.");
        this.withdrawalFee = withdrawalFee;
    }

    /**
     * wypłaca kasę powiększoną o stałą opłatę za wypłate.
     * jak brakuje środków na kwotę razem z opłatą, to odrzuca transakcje i loguje ostrzeżenie.
     *
     * @param amount kwota wypłaty (musi być większa od zera)
     * @param logger logger do zapisu transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota jest mniejsza lub równa zero
     */
    @Override
    public void withdraw(double amount, TransactionLogger logger) {
        requirePositive(amount, "Kwota wypłaty musi być dodatnia.");
        double total = amount + withdrawalFee;
        if (total > balance) {
            if (logger != null) logger.warn("Odmowa wypłaty z " + getClass().getSimpleName()
                    + ": kwota + opłata (" + pretty(total) + ") przekracza saldo " + pretty(balance));
            return;
        }
        balance -= total;
        if (logger != null) logger.log(getClass().getSimpleName() + " | Wypłata: -" + pretty(amount)
                + " (opłata " + pretty(withdrawalFee) + ") -> saldo: " + pretty(balance));
    }

    /**
     * przetwarza rozliczenie miesieczne dla konta bieżącego.
     * w tym koncie nie ma miesięcznych opłat, więc tylko to loguje.
     *
     * @param logger logger do zapisu operacji miesięcznych (może być null)
     */
    @Override
    public void processMonth(TransactionLogger logger) {
        if (logger != null) logger.log("[" + getClass().getSimpleName() + "] Brak miesięcznych opłat w tym miesiącu.");
    }
}