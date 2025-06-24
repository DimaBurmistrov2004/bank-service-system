import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BankService {

    // Метод для создания аккаунта
    public BankAccount createAccount(User user, String accountNumber) {
        BankAccount account = new BankAccount(user, accountNumber);
        user.addAccounts(account);
        return account;
    }

    // Метод для перевода средств между счетами
    public void transfer(BankAccount source, BankAccount target, BigDecimal amount) {

        // Проверка достаточно ли средств на балансе
        if (source.getBalance().compareTo(amount) < 0) {
            System.out.println("На счете недостаточно средств");
        }

        // Списание со счета отправителя и пополнение счета получателя
        source.withdraw(amount);
        target.deposit(amount);

        // Добавление операции в список
        Transaction transfer = new Transaction(amount, "TRANSFER", LocalDateTime.now(),
                source, target);

        source.addTransaction(transfer);
        target.addTransaction(transfer);
    }

    // Метод для получения истории транзакций
    public List<Transaction> getTransactionHistory(BankAccount account) {
        return account.getTransactions();
    }

    // Метод для просмотра общего баласна аккаунтов
    public void getTotalBalance(User user) {
        BigDecimal total = BigDecimal.ZERO;

        for (BankAccount account : user.getAccounts()) {
            total = total.add(account.getBalance());
        }

        System.out.println("Сумма балансов пользователя" + " " + user.getName() + ": " + total);
    }
}
