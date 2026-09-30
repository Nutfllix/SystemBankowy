package pl.bankapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Główna klasa aplikacji (punkt startowy programu), która demonstruje działanie
 * systemu bankowego.
 *
 * <p>Tworzy przykładowe konta (oszczędnościowe oraz rozliczeniowe), przeprowadza
 * symulację operacji dziennych (wpłaty, wypłaty, próby wypłaty przekraczające saldo),
 * przetwarza rozliczenia miesięczne oraz wykonuje końcowy audyt stanu kont.</p>
 *
 * <p>Wszystkie operacje i zdarzenia są rejestrowane przy użyciu obiektu {@link TransactionLogger},
 * zarządzanego automatycznie w bloku try-with-resources.</p>
 */
public class Main {

    /**
     * Główna metoda uruchomieniowa aplikacji.
     *
     * @param args argumenty wiersza poleceń (nie używane w tej aplikacji)
     */
    public static void main(String[] args) {
        try (TransactionLogger logger = new TransactionLogger("app-session")) {

            List<Account> accounts = new ArrayList<>();
            accounts.add(new SavingsAccount("Anna Nowak", 3_000.00, 0.02));
            accounts.add(new CheckingAccount("Jan Kowalski", 1_200.00, 3.50));

            logger.log("=== Operacje dzienne ===");
            accounts.get(0).deposit(500.00, logger);
            accounts.get(1).withdraw(200.00, logger);
            accounts.get(1).withdraw(2000.00, logger);

            logger.log("\n=== Przetwarzanie miesięczne (" + LocalDate.now().getMonth() + ") ===");
            for (Account acc : accounts) {
                acc.processMonth(logger);
            }

            logger.log("\n=== Audyt kont ===");
            for (Account acc : accounts) {
                acc.audit(logger);
                System.out.println(acc);
            }
        }
    }
}