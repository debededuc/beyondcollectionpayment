package beyondcollection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoyaltyPointsManager extends JFrame {

    private JTextField userIdField;
    private JTextField amountSpentField;
    private JTextField pointsToRedeemField;
    private JButton earnPointsButton;
    private JButton redeemPointsButton;
    private JLabel resultLabel;

    // Database connection variables
    private Connection con;

    public LoyaltyPointsManager() {
        // Set up the frame
        setTitle("Loyalty Points Manager");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // User ID Label and TextField
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(50, 50, 100, 30);
        add(userIdLabel);

        userIdField = new JTextField();
        userIdField.setBounds(150, 50, 200, 30);
        add(userIdField);

        // Amount Spent Label and TextField
        JLabel amountSpentLabel = new JLabel("Amount Spent:");
        amountSpentLabel.setBounds(50, 100, 100, 30);
        add(amountSpentLabel);

        amountSpentField = new JTextField();
        amountSpentField.setBounds(150, 100, 200, 30);
        add(amountSpentField);

        // Points to Redeem Label and TextField
        JLabel pointsToRedeemLabel = new JLabel("Points to Redeem:");
        pointsToRedeemLabel.setBounds(50, 150, 120, 30);
        add(pointsToRedeemLabel);

        pointsToRedeemField = new JTextField();
        pointsToRedeemField.setBounds(150, 150, 200, 30);
        add(pointsToRedeemField);

        // Earn Points Button
        earnPointsButton = new JButton("Earn Points");
        earnPointsButton.setBounds(150, 200, 120, 30);
        add(earnPointsButton);

        // Redeem Points Button
        redeemPointsButton = new JButton("Redeem Points");
        redeemPointsButton.setBounds(300, 200, 120, 30);
        add(redeemPointsButton);

        // Result Label
        resultLabel = new JLabel("");
        resultLabel.setBounds(50, 250, 400, 30);
        add(resultLabel);

        // Add action listener for earn points button
        earnPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                earnPointsAction();
            }
        });

        // Add action listener for redeem points button
        redeemPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redeemPointsAction();
            }
        });

        // Initialize database connection
        initializeDatabaseConnection();
    }

    private void earnPointsAction() {
        try {
            // Get input from the text fields
            int userId = Integer.parseInt(userIdField.getText());
            double amountSpent = Double.parseDouble(amountSpentField.getText());

            // Call the earnPoints method
            earnPoints(userId, amountSpent);

            // Display success message
            resultLabel.setText("Points earned successfully!");
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input! Please enter valid numbers.");
        } catch (Exception ex) {
            resultLabel.setText("Error: " + ex.getMessage());
        }
    }

    private void redeemPointsAction() {
        try {
            // Get input from the text fields
            int userId = Integer.parseInt(userIdField.getText());
            int pointsToRedeem = Integer.parseInt(pointsToRedeemField.getText());

            // Call the redeemPoints method
            if (redeemPoints(userId, pointsToRedeem)) {
                resultLabel.setText("Points redeemed successfully!");
            } else {
                resultLabel.setText("Failed to redeem points.");
            }
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input! Please enter valid numbers.");
        } catch (Exception ex) {
            resultLabel.setText("Error: " + ex.getMessage());
        }
    }

    // Method to redeem points
    private boolean redeemPoints(int userId, int pointsToRedeem) {
        try {
            // Check the current points of the user
            String checkQuery = "SELECT points FROM users WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(checkQuery);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int currentPoints = rs.getInt("points");

                if (currentPoints >= pointsToRedeem) {
                    // Deduct points from the user's account
                    String updateQuery = "UPDATE users SET points = points - ? WHERE id = ?";
                    pst = con.prepareStatement(updateQuery);
                    pst.setInt(1, pointsToRedeem);
                    pst.setInt(2, userId);
                    pst.executeUpdate();

                    // Log the points redeemed
                    String logQuery = "INSERT INTO loyalty_points_log (user_id, points, action, description) VALUES (?, ?, 'redeem', ?)";
                    pst = con.prepareStatement(logQuery);
                    pst.setInt(1, userId);
                    pst.setInt(2, pointsToRedeem);
                    pst.setString(3, "Points redeemed for discount.");
                    pst.executeUpdate();

                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient points!");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error redeeming points: " + e.getMessage());
        }
        return false;
    }

    private void initializeDatabaseConnection() {
        try {
            // Replace with your actual database connection details
            String url = "jdbc:mysql://localhost:3306/your_database"; // Replace with your database URL
            String user = "your_username";  // Replace with your database username
            String password = "your_password";  // Replace with your database password
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Run the frame
        SwingUtilities.invokeLater(() -> {
            LoyaltyPointsManager frame = new LoyaltyPointsManager();
            frame.setVisible(true);
        });
    }

    // Method to earn points
    public void earnPoints(int userId, double amountSpent) {
        // Assuming 1 point per 10 units of currency
        int pointsEarned = (int) (amountSpent / 10);

        // Update the database with earned points for the user
        try {
            String query = "UPDATE users SET points = points + ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, pointsEarned);
            pst.setInt(2, userId);
            pst.executeUpdate();

            // Log the points earned
            String logQuery = "INSERT INTO loyalty_points_log (user_id, points, action, description) VALUES (?, ?, 'earn', ?)";
            pst = con.prepareStatement(logQuery);
            pst.setInt(1, userId);
            pst.setInt(2, pointsEarned);
            pst.setString(3, "Points earned from amount spent.");
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error earning points: " + e.getMessage());
        }
    }
}
