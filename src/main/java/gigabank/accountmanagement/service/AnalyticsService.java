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

        BigDecimal totalSum = BigDecimal.ZERO;

        // Проверка входных данных на существование
        if (bankAccount == null || category.isEmpty()) {
            return totalSum;
        }

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        // Проверка 3 условий: Транзакция типа PAYMENT, ищем нужную категорию в транзакции,
        // транзакции младше одного месяца
        for (Transaction transaction : bankAccount.getTransactions()) {
            if (TransactionType.PAYMENT.equals(transaction.getType())
                    && transaction.getCategory().equals(category)
                    && !transaction.getCreatedDate().isBefore(oneMonthAgo)) {
                totalSum = totalSum.add(transaction.getValue());
            }
        }
        return totalSum;
    }

    public Map<String, BigDecimal> getMonthlySpendingByCategories(User user, Set<String> categories) {

        Map<String, BigDecimal> resultMap = new HashMap<>();

        // Проверка входных данных на существование
        if (user == null || categories.isEmpty()) {
            return resultMap;
        }

        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {

                // Проверка 3 условий: Транзакция типа PAYMENT, ищем нужную категорию в транзакции,
                // транзакции младше одного месяца
                if (TransactionType.PAYMENT.equals(transaction.getType())
                        && categories.contains(transaction.getCategory())
                        && !transaction.getCreatedDate().isBefore(oneMonthAgo)) {
                    resultMap.merge(transaction.getCategory(), transaction.getValue(), BigDecimal::add);
                }
            }
        }

        return resultMap;
    }

    public LinkedHashMap<String, List<Transaction>> getTransactionHistorySortedByAmount(User user) {

        LinkedHashMap<String, List<Transaction>> resultMap = new LinkedHashMap<>();

        // Проверка входных данных на существование
        if (user == null) {
            return resultMap;
        }

        List<Transaction> transactions = new ArrayList<>();

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {
                if (TransactionType.PAYMENT.equals(transaction.getType())) {
                    transactions.add(transaction);
                }
            }
        }
        transactions.sort(Comparator.comparing(Transaction::getValue));

        for (Transaction transaction : transactions) {
            resultMap.computeIfAbsent(transaction.getCategory(), n -> new ArrayList<>()).add(transaction);
        }

        return null;
    }

    public List<Transaction> getLastNTransaction(User user, int n) {

        List<Transaction> lastNTransaction = new ArrayList<>();

        // Проверка входных данных на существование
        if (user == null) {
            return lastNTransaction;
        }

        List<Transaction> transactions = new ArrayList<>();
        for (BankAccount account : user.getBankAccounts()) {
            transactions.addAll(account.getTransactions());
        }

        transactions.sort(Comparator.comparing(Transaction::getCreatedDate));

        for (int i = 0; i < Math.min(n, transactions.size()); i++) {
            lastNTransaction.add(transactions.get(i));
        }

        return lastNTransaction;
    }

    public PriorityQueue<Transaction> getTopNLargestTransactions(User user, int n) {

        PriorityQueue<Transaction> topNLargestTransactions =
                new PriorityQueue<>(Comparator.comparing(Transaction::getValue));

        // Проверка входных данных на существование
        if (user == null) {
            return topNLargestTransactions;
        }

        for (BankAccount account : user.getBankAccounts()) {
            for (Transaction transaction : account.getTransactions()) {
                    if (TransactionType.PAYMENT.equals(transaction.getType())) {
                        if (topNLargestTransactions.size() < n) {
                            topNLargestTransactions.offer(transaction);
                        } else if (topNLargestTransactions.peek() != null
                                && topNLargestTransactions.peek().getValue().compareTo(transaction.getValue()) < 0) {
                            topNLargestTransactions.poll();
                            topNLargestTransactions.offer(transaction);
                        }
                    }
                }
            }

        return topNLargestTransactions;
    }
}
