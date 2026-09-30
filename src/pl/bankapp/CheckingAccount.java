package pl.bankapp;

/**
 * Reprezentuje konto bieżące (rozliczeniowe), które dziedziczy po klasie {@link Account}.
 * Konto to pobiera dodatkową, stałą opłatę za każdą zrealizowaną wypłatę gotówki.
 */
public class CheckingAccount extends Account {

    /** Stała opłata pobierana przy każdej operacji wypłaty. */
    private final double withdrawalFee;

    /**
     * Tworzy nowe konto bieżące o określonym właścicielu, saldzie początkowym oraz opłacie za wypłatę.
     *
     * @param owner          nazwa właściciela konta (nie może być pusta ani null)
     * @param initialBalance początkowa kwota na koncie (nie może być ujemna)
     * @param withdrawalFee  opłata pobierana przy każdej wypłacie (nie może być ujemna)
     * @throws IllegalArgumentException jeśli opłata za wypłatę jest ujemna (walidacja właściciela i salda odbywa się w klasie bazowej)
     */
    public CheckingAccount(String owner, double initialBalance, double withdrawalFee) {
        super(owner, initialBalance);
        if (withdrawalFee < 0) throw new IllegalArgumentException("Opłata nie może być ujemna.");
        this.withdrawalFee = withdrawalFee;
    }

    /**
     * Dokonuje wypłaty określonej kwoty z konta powiększonej o stałą opłatę za wypłatę.
     * Jeśli suma kwoty wypłaty i opłaty przekracza dostępne saldo, operacja jest odrzucana,
     * a zdarzenie logowane jako ostrzeżenie.
     *
     * @param amount kwota wypłaty (musi być większa od zera)
     * @param logger logger rejestrujący przebieg transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota wypłaty jest mniejsza lub równa zero
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
     * Przetwarza rozliczenie miesięczne dla konta bieżącego.
     * W tej implementacji konto nie nalicza automatycznych opłat miesięcznych, co zostaje odpowiednio zahasłowane.
     *
     * @param logger logger rejestrujący przebieg operacji miesięcznych (może być null)
     */
    @Override
    public void processMonth(TransactionLogger logger) {
        if (logger != null) logger.log("[" + getClass().getSimpleName() + "] Brak miesięcznych opłat w tym miesiącu.");
    }
}