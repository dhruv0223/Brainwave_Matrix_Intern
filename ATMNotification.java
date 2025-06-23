import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMNotification extends JDialog {
    private boolean success;
    private String message;
    private String details;
    
    public ATMNotification(JFrame parent, boolean success, String message, String details) {
        super(parent, "Transaction Status", true);
        this.success = success;
        this.message = message;
        this.details = details;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(success ? new Color(34, 139, 34) : new Color(220, 20, 60));
        headerPanel.setPreferredSize(new Dimension(400, 60));
        
        JLabel statusLabel = new JLabel(success ? "✓ SUCCESS" : "✗ ERROR");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(statusLabel);
        
        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel detailsLabel = new JLabel("<html><center>" + details + "</center></html>");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setForeground(Color.GRAY);
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        contentPanel.add(detailsLabel, BorderLayout.SOUTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.addActionListener(e -> dispose());
        buttonPanel.add(okButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        // Layout is handled in initializeComponents
    }
    
    private void setupEventHandlers() {
        // Auto-close after 3 seconds
        Timer timer = new Timer(3000, e -> dispose());
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void showSuccess(JFrame parent, String message, String details) {
        new ATMNotification(parent, true, message, details).setVisible(true);
    }
    
    public static void showError(JFrame parent, String message, String details) {
        new ATMNotification(parent, false, message, details).setVisible(true);
    }
}
