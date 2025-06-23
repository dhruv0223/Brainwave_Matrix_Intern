import java.text.SimpleDateFormat;
import java.util.Date;

public class ATMTransaction {
    private String type;
    private double amount;
    private double balanceAfter;
    private Date timestamp;
    private String description;
    
    public ATMTransaction(String type, double amount, double balanceAfter, String description) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = new Date();
        this.description = description;
    }
    
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%-12s | $%8.2f | $%10.2f | %s | %s", 
            type, amount, balanceAfter, sdf.format(timestamp), description);
    }
    
    // Getters
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public Date getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
}
