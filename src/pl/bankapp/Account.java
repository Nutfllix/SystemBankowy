package pl.bankapp;

/**
 * Klasa abstrakcyjna reprezentująca ogólne konto bankowe w systemie.
 * Zapewnia podstawową funkcjonalność obsługi salda, wpłat, wypłat, audytu
 * oraz operacji miesięcznych. Implementuje interfejs {@link Auditable}.
 *
 * <p>Każde konto musi posiadać przypisanego właściciela oraz nieujemne saldo początkowe.</p>
 */
public abstract class Account implements Auditable {

    /** Właściciel konta. */
    private final String owner;

    /** Aktualne saldo na koncie. */
    protected double balance;

    /**
     * Tworzy nowe konto bankowe o określonym właścicielu i saldzie początkowym.
     *
     * @param owner nazwa właściciela konta (nie może być pusta ani null)
     * @param initialBalance początkowa kwota na koncie (nie może być ujemna)
     * @throws IllegalArgumentException jeśli właściciel jest pusty/null lub saldo początkowe jest ujemne
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
     * Dokonuje wpłaty określonej kwoty na konto.
     *
     * @param amount kwota wpłaty (musi być większa od zera)
     * @param logger logger rejestrujący przebieg transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota wpłaty jest mniejsza lub równa zero
     */
    public void deposit(double amount, TransactionLogger logger) {
        requirePositive(amount, "Kwota wpłaty musi być dodatnia.");
        balance += amount;
        if (logger != null) logger.log(owner + " | Wpłata: +" + pretty(amount) + " -> saldo: " + pretty(balance));
    }

    /**
     * Dokonuje wypłaty określonej kwoty z konta, jeśli dostępne środki są wystarczające.
     * W przypadku braku środków operacja jest przerywana, a zdarzenie logowane jako ostrzeżenie.
     *
     * @param amount kwota wypłaty (musi być większa od zera)
     * @param logger logger rejestrujący przebieg transakcji (może być null)
     * @throws IllegalArgumentException jeśli kwota wypłaty jest mniejsza lub równa zero
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
     * Przetwarza rozliczenie miesięczne dla konta (np. naliczanie odsetek, opłat).
     * Sposób przetwarzania zależy od konkretnego typu konta (implementacji podklasy).
     *
     * @param logger logger rejestrujący przebieg operacji miesięcznych (może być null)
     */
    public abstract void processMonth(TransactionLogger logger);

    /**
     * Przeprowadza audyt stanu konta, zapisując informacje o typie, właścicielu oraz saldzie.
     *
     * @param logger logger wykorzystany do zapisania raportu audytu (może być null)
     */
    @Override
    public void audit(TransactionLogger logger) {
        if (logger != null) logger.log("[AUDYT] " + getClass().getSimpleName() + " właściciel: " + getOwner() + ", saldo: " + pretty(balance));
    }

    /**
     * Pomocnicza metoda sprawdzająca, czy podana kwota jest dodatnia.
     *
     * @param amount  sprawdzana kwota
     * @param message komunikat błędu wyrzucany w przypadku niespełnienia warunku
     * @throws IllegalArgumentException jeśli kwota jest mniejsza lub równa zero
     */
    protected static void requirePositive(double amount, String message) {
        if (amount <= 0) throw new IllegalArgumentException(message);
    }

    /**
     * Formatuje wartość numeryczną do czytelnego ciągu znaków z walutą PLN.
     *
     * @param v wartość numeryczna do sformatowania
     * @return sformatowany ciąg znaków (np. "150.00 PLN")
     */
    protected static String pretty(double v) {
        return String.format("%.2f PLN", v);
    }

    /**
     * Zwraca nazwę właściciela konta.
     *
     * @return właściciel konta
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Zwraca aktualne saldo na koncie.
     *
     * @return aktualne saldo
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Zwraca czytelną reprezentację tekstową obiektu konta.
     *
     * @return opis konta w formacie tekstowym
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{owner='" + owner + "', balance=" + pretty(balance) + "}";
    }
}