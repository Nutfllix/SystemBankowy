package pl.bankapp;

/**
 * klasa abstrakcyjna reprezentujaca ogólne konto bankowe.
 * odpowiada za saldo, wpłaty, wypłaty, audyt oraz operacje miesięczne.
 * implementuje interfejs {@link Auditable}.
 *
 * <p>każde konto musi miec właściciela i nieujemne saldo startowe.</p>
 */
public abstract class Account implements Auditable {

    /** właściciel konta. */
    private final String owner;

    /** aktualne saldo na koncie. */
    protected double balance;

    /**
     * tworzy nowe konto o podanym właścielu i saldzie początkowym.
     *
     * @param owner nazwa właściciela (nie może być pusta ani null)
     * @param initialBalance początkowa kasa na koncie (nie może być ujemna)
     * @throws IllegalArgumentException jeśli właściciel jest pusty lub saldo na minusie
     */
    public Account(String owner, double initialBalance) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("Owner nie może być pusty.");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Saldo początkowe nie może być ujemne.");
        }
        this.owner = owner;
        this.balance = initialBalance;
    }

    /**
     * wpłaca podaną kwotę na konto.
     *
     * @param amount kwota do wpłaty (musi być większa od zera)
     * @param logger logger do zapisu transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota jest mniejsza lub równa zero
     */
    public void deposit(double amount, TransactionLogger logger) {
        requirePositive(amount, "Kwota wpłaty musi być dodatnia.");
        balance += amount;
        if (logger != null) logger.log(owner + " | Wpłata: +" + pretty(amount) + " -> saldo: " + pretty(balance));
    }

    /**
     * wypłaca kasę z konta, jeśli są wystarczajace środki.
     * jak nie ma kasy, to przerywa operacje i loguje ostrzeżenie.
     *
     * @param amount kwota wypłaty (musi być większa od zera)
     * @param logger logger do zapisu transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota jest mniejsza lub równa zero
     */
    public void withdraw(double amount, TransactionLogger logger) {
        requirePositive(amount, "Kwota wypłaty musi być dodatnia.");
        if (amount > balance) {
            if (logger != null) logger.warn(owner + " | Odmowa wypłaty: brak środków (" + pretty(amount) + " > " + pretty(balance) + ")");
            return;
        }
        balance -= amount;
        if (logger != null) logger.log(owner + " | Wypłata: -" + pretty(amount) + " -> saldo: " + pretty(balance));
    }

    /**
     * przetwarza rozliczenie miesieczne (np. odsetki, opłaty).
     * zależy to od konkretnego typu konta.
     *
     * @param logger logger do zapisu operacji miesięcznych (może być null)
     */
    public abstract void processMonth(TransactionLogger logger);

    /**
     * robi audyt konta, zapisując info o typie, właścicielu i saldzie.
     *
     * @param logger logger do zapisu raportu audytu (może być null)
     */
    @Override
    public void audit(TransactionLogger logger) {
        if (logger != null) logger.log("[AUDYT] " + getClass().getSimpleName() + " właściciel: " + getOwner() + ", saldo: " + pretty(balance));
    }

    /**
     * pomocnicza metoda sprawdzająca czy kwota jest dodatnia.
     *
     * @param amount sprawdzana kwota
     * @param message komunikat o błędzie
     * @throws IllegalArgumentException jeśli kwota jest mniejsza lub równa zero
     */
    protected static void requirePositive(double amount, String message) {
        if (amount <= 0) throw new IllegalArgumentException(message);
    }

    /**
     * formatuje liczbę do ładnego stringa z walutą PLN.
     *
     * @param v wartość do sformatowania
     * @return sformatowany tekst (np. "150.00 PLN")
     */
    protected static String pretty(double v) {
        return String.format("%.2f PLN", v);
    }

    /**
     * zwraca nazwę właściciela konta.
     *
     * @return właściciel konta
     */
    public String getOwner() {
        return owner;
    }

    /**
     * zwraca aktualne saldo na koncie.
     *
     * @return aktualne saldo
     */
    public double getBalance() {
        return balance;
    }

    /**
     * zwraca tekstową reprezentacje obiektu konta.
     *
     * @return opis konta
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner='" + owner + "', balance=" + pretty(balance) + "}";
    }
}