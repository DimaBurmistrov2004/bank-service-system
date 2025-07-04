package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class AnalyticsService {

    public BigDecimal getMonthlySpendingByCategory(BankAccount bankAccount, String category) {

        // Проверка входных данных на существование
        if (bankAccount == null || category.isEmpty()) {
            return BigDecimal.ZERO;
        }

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        BigDecimal amount = BigDecimal.ZERO;

        // Проверка 3 условий: Транзакция типа PAYMENT, ищем нужную категорию в транзакции,
        // транзакции младше одного месяца
        for (Transaction transaction : bankAccount.getTransactions()) {
            if (transaction.getType() == TransactionType.PAYMENT
                    && transaction.getCategory().equals(category)
                    && !transaction.getCreatedDate().isBefore(oneMonthAgo)) {
                amount = amount.add(transaction.getValue());
            }
        }
        return amount;
    }

    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {

        // Проверка входных данных на существование
        if (user == null || categories.isEmpty()) {
            return Collections.emptyMap();
        }

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        Map<String, BigDecimal> monthlySpendingByCategories = new HashMap<>();

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {

                // Проверка 3 условий: Транзакция типа PAYMENT, ищем нужную категорию в транзакции,
                // транзакции младше одного месяца
                if (transaction.getType() == TransactionType.PAYMENT
                        && categories.contains(transaction.getCategory())
                        && !transaction.getCreatedDate().isBefore(oneMonthAgo)) {
                    BigDecimal currentSum = monthlySpendingByCategories.
                            getOrDefault(transaction.getCategory(), BigDecimal.ZERO);
                    BigDecimal newSum = currentSum.add(transaction.getValue());
                    monthlySpendingByCategories.put(transaction.getCategory(), newSum);
                }
            }
        }

        return monthlySpendingByCategories;
    }

    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {

        // Проверка входных данных на существование
        if (user == null) {
            return new LinkedHashMap<>();
        }

        LinkedHashMap<String, List<Transaction>> TransactionHistorySorted = new LinkedHashMap<>();

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {

                // Проверка, что транзакция типа PAYMENT
                if (transaction.getType() == TransactionType.PAYMENT) {
                    List<Transaction> transactions = new ArrayList<>();
                    transactions.add(transaction);
                    transactions.sort(new Comparator<Transaction>() {
                        @Override
                        public int compare(Transaction o1, Transaction o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }
                    });
                    TransactionHistorySorted.put(transaction.getCategory(), transactions);
                }
            }
        }

        return TransactionHistorySorted;
    }

    public List<Transaction> getLastNTransaction(User user, int n) {

        // Проверка входных данных на существование
        if (user == null) {
            return new ArrayList<>();
        }

        List<Transaction> lastNTransaction = new ArrayList<>();

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {
                if (transaction != null) {
                    lastNTransaction.add((transaction));
                }
            }
        }

        lastNTransaction.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getCreatedDate().compareTo(o1.getCreatedDate());
            }
        });

        return lastNTransaction.subList(0, n);
    }

    public PriorityQueue<Transaction> getTopNLargestTransactions(User user, int n) {

        // Проверка входных данных на существование
        if (user == null) {
            return new PriorityQueue<>();
        }

        PriorityQueue<Transaction> topNLargestTransactions = new PriorityQueue<>();

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {
                if (transaction == null) {
                    return new PriorityQueue<>();
                }
                if (transaction.getType() == TransactionType.PAYMENT) {
                    topNLargestTransactions.add(transaction);

                    if (topNLargestTransactions.size() > n) {
                        topNLargestTransactions.poll();
                    }
                }
            }
        }

        return topNLargestTransactions;
    }
}
