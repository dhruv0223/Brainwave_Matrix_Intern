import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ATMAccount {
    private String accountNumber;
    private String pin;
    private double balance;
    private String accountHolderName;
    private List<ATMTransaction> transactionHistory;
    
    public ATMAccount(String accountNumber, String pin, double initialBalance, String accountHolderName) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.accountHolderName = accountHolderName;
        this.transactionHistory = new ArrayList<>();
        
        if (initialBalance > 0) {
            transactionHistory.add(new ATMTransaction("DEPOSIT", initialBalance, balance, "Initial deposit"));
        }
    }
    
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }
    
    public boolean withdraw(double amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }
        balance -= amount;
        transactionHistory.add(new ATMTransaction("WITHDRAWAL", amount, balance, "Cash withdrawal"));
        return true;
    }
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new ATMTransaction("DEPOSIT", amount, balance, "Cash deposit"));
        }
    }
    
    public boolean transfer(ATMAccount targetAccount, double amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }
        
        balance -= amount;
        targetAccount.deposit(amount);
        
        transactionHistory.add(new ATMTransaction("TRANSFER_OUT", amount, balance, 
            "Transfer to " + targetAccount.getAccountNumber()));
        targetAccount.addTransaction(new ATMTransaction("TRANSFER_IN", amount, 
            targetAccount.getBalance(), "Transfer from " + this.accountNumber));
        
        return true;
    }
    
    public void addTransaction(ATMTransaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public void changePin(String newPin) {
        this.pin = newPin;
        transactionHistory.add(new ATMTransaction("PIN_CHANGE", 0, balance, "PIN changed"));
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getAccountHolderName() { return accountHolderName; }
    public List<ATMTransaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }
}
