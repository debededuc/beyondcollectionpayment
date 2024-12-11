package beyondcollection;
import java.util.Date;

public class Refund {
    private int id;
    private String orderID;
    private double amount;
    private Date dateRequested;
    private int priority;
    private int queuePosition; 
    public Refund(int id, String orderID, double amount, Date dateRequested, int priority) {
        this.id = id;
        this.orderID = orderID;
        this.amount = amount;
        this.dateRequested = dateRequested;
        this.priority = priority;
    }
    
    public String toString() {
        return "Refund ID: " + id + 
               "\nOrder ID: " + orderID + 
               "\nAmount: " + amount + 
               "\nDate Requested: " + dateRequested + 
               "\nPriority: " + priority + 
               "\nQueue Position: " + queuePosition;
    }

    public int getId() { return id; }
    public String getOrderID() { return orderID; }
    public double getAmount() { return amount; }
    public Date getDateRequested() { return dateRequested; }
    public int getPriority() { return priority; }
    public int getQueuePosition() { return queuePosition; }

    public void setId(int id) { this.id = id; }
    public void setOrderID(String orderID) { this.orderID = orderID; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDateRequested(Date dateRequested) { this.dateRequested = dateRequested; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setQueuePosition(int queuePosition) { this.queuePosition = queuePosition; }
}
