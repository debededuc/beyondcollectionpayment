package beyondcollection;
import java.util.Date;  

public class Feedback {
    private int id;
    private String customerName;
    private String message;
    private int rating;
    private String purpose; 
    private Date dateSubmitted;

    public Feedback(int id, String customerName, String message, int rating, Date dateSubmitted, String purpose) {
        this.id = id;
        this.customerName = customerName;
        this.message = message;
        this.rating = rating;
        this.purpose = purpose;
        this.dateSubmitted = dateSubmitted;
    }

    public String toString() {
        return "Name: " + customerName + "\nFeedback: " + message + "\nRating: " + rating + "\nPurpose: " + purpose;
    }
    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getMessage() { return message; }
    public int getRating() { return rating; }
    public String getPurpose() { return purpose; }
    public Date getDateSubmitted() { return dateSubmitted; }

    public void setId(int id) { this.id = id; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setMessage(String message) { this.message = message; }
    public void setRating(int rating) { this.rating = rating; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public void setDateSubmitted(Date dateSubmitted) { this.dateSubmitted = dateSubmitted; }
}

