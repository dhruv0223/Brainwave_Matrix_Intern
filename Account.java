import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Account {
    private String accountNumber;
    private String pin;
    private double balance;
    private String accountHolderName;
    private List<Transaction> transactionHistory;
    
    public Account(String accountNumber, String pin, double initialBalance, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.accountHolderName = accountHolderName;
        this.transactionHistory = new ArrayList<>();
        
        // Add initial deposit transaction
        if (initialBalance > 0) {
            transactionHistory.add(new Transaction("DEPOSIT", initialBalance, balance, "Initial deposit"));
        }
    }
    
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }
    
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add(new Transaction("WITHDRAWAL", amount, balance, "Cash withdrawal"));
            return true;
        }
        return false;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new Transaction("DEPOSIT", amount, balance, "Cash deposit"));
        }
    }
    
    public boolean transfer(Account targetAccount, double amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }
        
        balance -= amount;
        targetAccount.deposit(amount);
        
        transactionHistory.add(new Transaction("TRANSFER_OUT", amount, balance, 
            "Transfer to " + targetAccount.getAccountNumber()));
        targetAccount.addTransaction(new Transaction("TRANSFER_IN", amount, 
            targetAccount.getBalance(), "Transfer from " + this.accountNumber));
        
        return true;
    }
    
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public void changePin(String newPin) {
        this.pin = newPin;
        transactionHistory.add(new Transaction("PIN_CHANGE", 0, balance, "PIN changed"));
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getAccountHolderName() { return accountHolderName; }
    public List<Transaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
}
