package pl.bankapp;

/**
 * Reprezentuje konto oszczędnościowe, które dziedziczy po klasie {@link Account}.
 * Konto to generuje zysk w postaci comiesięcznych odsetek naliczanych na podstawie rocznej stopy procentowej.
 */
public class SavingsAccount extends Account {

    /** Roczna stopa procentowa (np. 0.02 oznacza 2% w skali roku). */
    private final double annualInterestRate;

    /**
     * Tworzy nowe konto oszczędnościowe o określonym właścicielu, saldzie początkowym oraz rocznej stopie procentowej.
     *
     * @param owner              nazwa właściciela konta (nie może być pusta ani null)
     * @param initialBalance     początkowa kwota na koncie (nie może być ujemna)
     * @param annualInterestRate roczna stopa procentowa (nie może być ujemna)
     * @throws IllegalArgumentException jeśli roczna stopa procentowa jest ujemna (walidacja właściciela i salda odbywa się w klasie bazowej)
     */
    public SavingsAccount(String owner, double initialBalance, double annualInterestRate) {
        super(owner, initialBalance);
        if (annualInterestRate < 0) throw new IllegalArgumentException("Stopa nie może być ujemna.");
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * Przetwarza rozliczenie miesięczne dla konta oszczędnościowego.
     * Oblicza miesięczną stopę procentową (dzieląc roczną stopę przez 12),
     * nalicza odsetki od aktualnego salda, aktualizuje stan konta oraz rejestruje zdarzenie w loggerze.
     *
     * @param logger logger rejestrujący przebieg operacji miesięcznych (może być null)
     */
    @Override
    public void processMonth(TransactionLogger logger) {
        double monthlyRate = annualInterestRate / 12.0;
        double interest = balance * monthlyRate;
        balance += interest;
        if (logger != null) logger.log("[" + getClass().getSimpleName() + "] Naliczone odsetki: +" + pretty(interest) + " -> saldo: " + pretty(balance));
    }
}