package pl.bankapp;

/**
 * reprezentuje konto oszczędnościowe, które dziedziczy po {@link Account}.
 * zarabia na siebie dzięki comiesięcznym odsetkom z rocznego oprocentowania.
 */
public class SavingsAccount extends Account {

    /** roczna stopa procentowa (np. 0.02 to 2%). */
    private final double annualInterestRate;

    /**
     * tworzy nowe konto oszczędnościowe z właścicielem, saldem startowym i oprocentowaniem.
     *
     * @param owner nazwa właściciela (nie może być pusta ani null)
     * @param initialBalance początkowa kasa na koncie (nie może być ujemna)
     * @param annualInterestRate stopa procentowa (nie może być ujemna)
     * @throws IllegalArgumentException jeśli stopa procentowa jest ujemna
     */
    public SavingsAccount(String owner, double initialBalance, double annualInterestRate) {
        super(owner, initialBalance);
        if (annualInterestRate < 0) throw new IllegalArgumentException("Stopa nie może być ujemna.");
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * przetwarza rozliczenie miesieczne dla konta oszczędnościowego.
     * dzieli roczne oprocentowanie na 12 miesiecy, nalicza odsetki do salda i loguje wynik.
     *
     * @param logger logger do zapisu operacji miesięcznych (może być null)
     */
    @Override
    public void processMonth(TransactionLogger logger) {
        double monthlyRate = annualInterestRate / 12.0;
        double interest = balance * monthlyRate;
        balance += interest;
        if (logger != null) logger.log("[" + getClass().getSimpleName() + "] Naliczone odsetki: +" + pretty(interest) + " -> saldo: " + pretty(balance));
    }
}