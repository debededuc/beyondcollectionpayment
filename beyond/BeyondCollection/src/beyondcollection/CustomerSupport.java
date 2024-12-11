package beyondcollection;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class CustomerSupport {
    private Connection connection;
    private List<Feedback> feedbackList;
    private List<OrderReturn> orderReturnsList;
    private Queue<Refund> refundQueue;

    public CustomerSupport() {
        this.connection = CustomerSupportConnection.connect();
        this.feedbackList = new ArrayList<>();
        this.orderReturnsList = new ArrayList<>();
        this.refundQueue = new LinkedList<>();
    }

    
    public void addFeedback(String customerName, String message, int rating, String feedbackPurpose) {
        String query = "INSERT INTO Feedback (customerName, message, rating, dateSubmitted, feedbackPurpose) VALUES (?, ?, ?, NOW(), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customerName);
            stmt.setString(2, message);
            stmt.setInt(3, rating);
            stmt.setString(4, feedbackPurpose);
            stmt.executeUpdate();

           
            Feedback feedback = new Feedback(0, customerName, message, rating, new Date(), feedbackPurpose);
            feedbackList.add(feedback);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   
    public void displayFeedback(String feedbackPurpose) {
        List<Feedback> filteredFeedbacks = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            if (feedback.getPurpose().equals(feedbackPurpose)) {
                filteredFeedbacks.add(feedback);
            }
        }
       
        filteredFeedbacks.sort(Comparator.comparingInt(Feedback::getRating).reversed());
        
        
        for (Feedback feedback : filteredFeedbacks) {
            System.out.println(feedback);
        }
    }

   
    public void processOrderReturn(String orderID, String reason) {
        String query = "INSERT INTO OrderReturns (orderID, reason, dateRequested) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderID);
            stmt.setString(2, reason);
            stmt.executeUpdate();

            
            OrderReturn orderReturn = new OrderReturn(0, orderID, reason, new Date(), "In Progress", "Awaiting approval");
            orderReturnsList.add(orderReturn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void getOrderReturnStatus(String orderID) {
        for (OrderReturn orderReturn : orderReturnsList) {
            if (orderReturn.getOrderID().equals(orderID)) {
                System.out.println("Order ID: " + orderReturn.getOrderID());
                System.out.println("Reason: " + orderReturn.getReason());
                System.out.println("Date Requested: " + orderReturn.getDateRequested());
                System.out.println("Status: " + orderReturn.getStatus());
                System.out.println("Admin Response: " + orderReturn.getAdminResponse());
                return;
            }
        }
        System.out.println("No return request found for Order ID: " + orderID);
    }

   
    public int processRefund(String orderID, double amount, int priority) {
       
        Refund refund = new Refund(0, orderID, amount, new Date(), priority);

       
        refundQueue.add(refund);

      
        String query = "INSERT INTO Refunds (orderID, amount, dateRequested, priority) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, orderID);
            stmt.setDouble(2, amount);
            stmt.setInt(3, priority);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
        return refundQueue.size();
    }

   
    public void displayRefundQueue() {
        System.out.println("Current Refund Queue:");
        int position = 1;
        for (Refund refund : refundQueue) {
            refund.setQueuePosition(position++); 
            System.out.println("Position: " + refund.getQueuePosition());
            System.out.println(refund);
        }
    }

   
    public List<Feedback> getFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM Feedback";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Feedback feedback = new Feedback(rs.getInt("id"), rs.getString("customerName"),
                        rs.getString("message"), rs.getInt("rating"), rs.getDate("dateSubmitted"),
                        rs.getString("feedbackPurpose"));
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbackList;
    }

   
    public List<OrderReturn> getOrderReturns() {
        List<OrderReturn> orderReturns = new ArrayList<>();
        String query = "SELECT * FROM OrderReturns";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                OrderReturn orderReturn = new OrderReturn(rs.getInt("id"), rs.getString("orderID"),
                        rs.getString("reason"), rs.getDate("dateRequested"), rs.getString("status"), rs.getString("adminResponse"));
                orderReturns.add(orderReturn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderReturns;
    }

   
    public List<Refund> getRefunds() {
        List<Refund> refunds = new ArrayList<>();
        String query = "SELECT * FROM Refunds";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Refund refund = new Refund(rs.getInt("id"), rs.getString("orderID"),
                        rs.getDouble("amount"), rs.getDate("dateRequested"), rs.getInt("priority"));
                refunds.add(refund);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return refunds;
    }
}
