package beyondcollection;
import java.util.Date;

public class OrderReturn {
    private int id;
    private String orderID;
    private String reason;
    private Date dateRequested;
    private String status;  
    private String adminResponse; 

    public OrderReturn(int id, String orderID, String reason, Date dateRequested, String status, String adminResponse) {
        this.id = id;
        this.orderID = orderID;
        this.reason = reason;
        this.dateRequested = dateRequested;
        this.status = status;
        this.adminResponse = adminResponse;
    }

  
    public int getId() { return id; }
    public String getOrderID() { return orderID; }
    public String getReason() { return reason; }
    public Date getDateRequested() { return dateRequested; }
    public String getStatus() { return status; }
    public String getAdminResponse() { return adminResponse; }

    public void setId(int id) { this.id = id; }
    public void setOrderID(String orderID) { this.orderID = orderID; }
    public void setReason(String reason) { this.reason = reason; }
    public void setDateRequested(Date dateRequested) { this.dateRequested = dateRequested; }
    public void setStatus(String status) { this.status = status; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }
}

