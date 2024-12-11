package beyondcollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerSide extends JFrame { 
    private CustomerSupport customerSupport;
    private JTextArea displayArea;
    private String loggedInUsername; 

    public CustomerSide(CustomerSupport customerSupport, String loggedInUsername) {
        this.customerSupport = customerSupport;
        this.loggedInUsername = loggedInUsername; // Initialize the logged-in username
        setTitle("Customer Support - Beyond Collections");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1)); 
        String[] options = {"Submit Feedback", "View Feedback", "Request Order Return", "Request Refund", "Exit", "Back to Product Catalog"};
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener(new MenuActionListener(option));
            menuPanel.add(button);
        }
        add(menuPanel, BorderLayout.WEST);

        setVisible(true);
    }

    private class MenuActionListener implements ActionListener {
        private String action;

        public MenuActionListener(String action) {
            this.action = action;
        }
        
        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case "Submit Feedback":
                    submitFeedback();
                    break;
                case "View Feedback":
                    viewFeedback();
                    break;
                case "Request Order Return":
                    requestOrderReturn();
                    break;
                case "Request Refund":
                    requestRefund();
                    break;
                case "Back to Product Catalog":
                    exitToProductCatalog();
                    break; 
            }
        }
    }

    private void exitToProductCatalog(){
        new ProductCatalog(loggedInUsername).setVisible(true); 
        this.dispose();
    }
    
    private void submitFeedback() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        String message = JOptionPane.showInputDialog(this, "Enter your feedback message:");
        String rating = JOptionPane.showInputDialog(this, "Rate your feedback (1-5):");
        String purpose = JOptionPane.showInputDialog(this, "Is this about a product or customer service?");

        int feedbackRating = Integer.parseInt(rating);
        customerSupport.addFeedback(name, message, feedbackRating, purpose);
        displayArea.setText("Thank you for your feedback!\n");

        String alternativeChoice = JOptionPane.showInputDialog(this, "Would you like an alternative option? (y/n)");
        if (alternativeChoice.equalsIgnoreCase("y")) {
            String[] options = {"Receive a gift card", "Receive a product replacement", "Get discounts"};
            String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose an alternative:", "Alternative Options", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            displayArea.append("You chose: " + selectedOption + "\n");
        }
    }

    private void viewFeedback() {
        List<Feedback> feedbackList = customerSupport.getFeedback();
        if (feedbackList.isEmpty()) {
            displayArea.setText("No feedback available at the moment.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Feedback feedback : feedbackList) {
                sb.append(feedback).append("\n\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    private void requestOrderReturn() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        String orderID = JOptionPane.showInputDialog(this, "Enter your order ID:");
        String reason = JOptionPane.showInputDialog(this, "Enter the reason for the return:");

        customerSupport.processOrderReturn(orderID, reason);
        displayArea.setText("Your order return has been processed.\n");

        String alternativeChoice = JOptionPane.showInputDialog(this, "Would you like an alternative option? (y/n)");
        if (alternativeChoice.equalsIgnoreCase("y")) {
            String[] options = {"Receive a gift card", "Receive a product replacement", "Get discounts"};
            String selectedOption = (String) JOptionPane.showInputDialog(this, "Choose an alternative:", "Alternative Options", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            displayArea.append("You chose: " + selectedOption + "\n");
        }
    }

    private void requestRefund() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        String orderID = JOptionPane.showInputDialog(this, "Enter your order ID:");
        String amount = JOptionPane.showInputDialog(this, "Enter the refund amount:");
        String priority = JOptionPane.showInputDialog(this, "Enter the priority (1-5):");

        double refundAmount = Double.parseDouble(amount);
        int refundPriority = Integer.parseInt(priority);
        int queuePosition = customerSupport.processRefund(orderID, refundAmount, refundPriority);
        displayArea.setText("Your refund request is placed at position " + queuePosition + ".\n");
    }

    private void openAdminPanel() {
        JFrame adminFrame = new JFrame("Admin Panel - Bank Account");
        adminFrame.setSize(400, 400);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setLayout(new BorderLayout());

        JPanel bankPanel = new JPanel();
        bankPanel.setLayout(new GridLayout(5, 1));

        bankPanel.add(new JLabel("Bank Account Details:"));
        bankPanel.add(new JLabel("Account Number: 123456789"));
        bankPanel.add(new JLabel("Account Holder: Beyond Collections"));
        bankPanel.add(new JLabel("Bank Name: Kurt Stephen Hombre"));
        bankPanel.add(new JLabel("Routing Number: 0987654321"));

        adminFrame.add(bankPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> adminFrame.dispose());
        adminFrame.add(closeButton, BorderLayout.SOUTH);

        adminFrame.setVisible(true);
    }

    public static void main(String[] args) {
        String loggedInUsername = "";
        new CustomerSide(new CustomerSupport(), loggedInUsername);
    }
}