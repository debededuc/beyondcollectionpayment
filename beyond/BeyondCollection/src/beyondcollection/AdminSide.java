package beyondcollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminSide extends JFrame { // Extend JFrame
    private CustomerSupport customerSupport;
    private JTextArea displayArea;

    public AdminSide(CustomerSupport customerSupport) {
        this.customerSupport = customerSupport;
        setTitle("Admin Dashboard - Beyond Collections");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5, 1));
        String[] options = {"View Feedback", "View Order Returns", "View Refunds", "Sort Feedbacks", "Exit"};
        for (String option : options) {
            JButton button = new JButton(option);
            button.addActionListener(new MenuActionListener(option));
            menuPanel.add(button);
        }
        add(menuPanel, BorderLayout.WEST);

        setVisible(true); // Show the frame
    }

    private class MenuActionListener implements ActionListener {
        private String action;

        public MenuActionListener(String action) {
            this.action = action;
        }

        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case "View Feedback":
                    viewFeedback();
                    break;
                case "View Order Returns":
                    viewOrderReturns();
                    break;
                case "View Refunds":
                    viewRefunds();
                    break;
                case "Sort Feedbacks":
                    sortFeedbacks();
                    break;
                case "Back to Dashboard":
                    new AdminDashboard().setVisible(true);
                    AdminSide.this.dispose();
                    break;
            }
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

    private void viewOrderReturns() {
        List<OrderReturn> orderReturns = customerSupport.getOrderReturns();
        if (orderReturns.isEmpty()) {
            displayArea.setText("No order returns available at the moment.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (OrderReturn orderReturn : orderReturns) {
                sb.append(orderReturn).append("\n\n");
            }
            displayArea.setText(sb.toString());

            String orderID = JOptionPane.showInputDialog(this, "Enter the order ID to update status:");
            OrderReturn orderReturn = findOrderReturnById(orderID);

            if (orderReturn != null) {
                String[] statusOptions = {"Pending", "Approved", "Rejected"};
                String newStatus = (String) JOptionPane.showInputDialog(this, 
                        "Select new status for Order ID: " + orderID, 
                        "Update Status", 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, 
                        statusOptions, 
                        orderReturn.getStatus());

                String adminResponse = JOptionPane.showInputDialog(this, "Enter your response to the customer:");

                orderReturn.setStatus(newStatus);
                orderReturn.setAdminResponse(adminResponse);

                displayArea.append("\nOrder return status updated.\n");
                displayArea.append("Order ID: " + orderID + "\nNew Status: " + newStatus + "\nResponse: " + adminResponse);
            } else {
                displayArea.setText("Order ID not found.");
            }
        }
    }

    private void viewRefunds() { 
        List<Refund> refundList = customerSupport.getRefunds();
        
        String bankDetails = "Bank Account Details:\n" +
                             "Name: Kurt Stephen Hombre\n" +
                             "Bank Number: 09984560302\n" +
                             "Bank Balance: 53,345\n\n";

        if (refundList.isEmpty()) {
            displayArea.setText(bankDetails + "No refunds available at the moment.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(bankDetails); // Add bank details to the display
            sb.append("Refunds:\n");
            for (Refund refund : refundList) {
                sb.append(refund).append("\n\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    private void sortFeedbacks() {
        List <Feedback> feedbackList = customerSupport.getFeedback();
        if (feedbackList.isEmpty()) {
            displayArea.setText("No feedback available to sort.");
        } else {
            SortingAlgorithms.sortFeedbackByPurposeAndRating(feedbackList);
            StringBuilder sb = new StringBuilder();
            for (Feedback feedback : feedbackList) {
                sb.append(feedback).append("\n\n");
            }
            displayArea.setText("Sorted Feedback:\n" + sb.toString());
        }
    }

    private OrderReturn findOrderReturnById(String orderID) {
        for (OrderReturn orderReturn : customerSupport.getOrderReturns()) {
            if (orderReturn.getOrderID().equals(orderID)) {
                return orderReturn;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new AdminSide(new CustomerSupport());
    }
}