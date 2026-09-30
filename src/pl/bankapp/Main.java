package pl.bankapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kacper Łupinski
 * @version 1.0.0
 * @since 2026-09
 *
 * główna klasa aplikacji (start programu), która pokazuje jak działa bank.
 *
 * <p>tworzy konta, robi wpłaty, wypłaty, próby wypłaty bez kasy,
 * rozliczenie miesiąca i robi audyt na koniec.</p>
 *
 * <p>wszystko loguje sie przez logger w bloku try-with-resources.</p>
 */
public class Main {

    /**
     * główna metoda uruchamiajaca aplikacje.
     *
     * @param args argumenty z konsoli (nieużywane)
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