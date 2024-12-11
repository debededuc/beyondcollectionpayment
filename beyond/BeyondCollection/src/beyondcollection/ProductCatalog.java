package beyondcollection;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class ProductCatalog extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    private ArrayList<CartItem> cart;
    private String loggedInUsername;
    private javax.swing.JButton btnLogout; // Declare the logout button
    private JRadioButton rbtnStandard; // Declare radio buttons
    private JRadioButton rbtnPriority;
    private JButton btnCustomerSide; // Declare button to navigate to customer side

    public ProductCatalog(String loggedInUsername) {
        initComponents();
        this.loggedInUsername = loggedInUsername; 
        Connect();
        loadTableData();
        cart = new ArrayList<>(); 
        btnAddToCart.addActionListener(new AddToCartAction());
        btnRemove.addActionListener(new RemoveFromCartAction());
        btnCheckout.addActionListener(new CheckoutAction());
        btnCheckOrder.addActionListener(new CheckOrderAction());
        btnLogout.addActionListener(new LogoutAction()); // Add action listener for logout
        btnCustomerSide.addActionListener(new CustomerSideAction()); // Add action listener for customer side
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAddToCart = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();
        btnCheckOrder = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        cartTableModel = new javax.swing.JTable();
        btnLogout = new javax.swing.JButton(); 
        btnCustomerSide = new JButton("Customer Support"); 

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Product Catalog");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Product ID", "Product Name", "Category", "Price", "No. of Stocks", "Image"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        btnAddToCart.setText("Add to Cart");
        btnRemove.setText("Remove From Cart");
        btnCheckout.setText("Checkout ");
        btnCheckOrder.setText("Orders");
        btnLogout.setText("Log Out"); 

        cartTableModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object[]{
                "Product ID", "Product Name", "Price"
            }, 0
        ));
        jScrollPane1.setViewportView(cartTableModel);

        rbtnStandard = new JRadioButton("Standard Delivery (up to 7 days)");
        rbtnPriority = new JRadioButton("Priority Delivery (3 days max)");
        ButtonGroup deliveryOptions = new ButtonGroup();
        deliveryOptions.add(rbtnStandard);
        deliveryOptions.add(rbtnPriority);

        JPanel deliveryPanel = new JPanel();
        deliveryPanel.add(rbtnStandard);
        deliveryPanel.add(rbtnPriority);

         // Layout setup
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent (btnAddToCart)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCheckOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCheckout))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnLogout)
                        .addGap(20, 20, 20) 
                        .addComponent(btnCustomerSide)) 
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(deliveryPanel)) 
                )
            )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnAddToCart)
                .addComponent(btnRemove)
                .addComponent(btnCheckOrder)
                .addComponent(btnCheckout)
                .addComponent(btnLogout)
                .addComponent(btnCustomerSide)) 
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(deliveryPanel)) 
        );

        pack();
    }

    private class CustomerSideAction implements ActionListener {
    @Override
        public void actionPerformed(ActionEvent e) {
            CustomerSupport customerSupport = new CustomerSupport(); 
            CustomerSide customerSide = new CustomerSide(customerSupport, loggedInUsername);
            customerSide.setVisible(true); 
            ProductCatalog.this.dispose(); 
        }
    }

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (!cart.isEmpty()) {
            int selectedRow = cartTableModel.getSelectedRow();
            if (selectedRow >= 0) {
                cart.remove(selectedRow);
                updateCartTable();
                JOptionPane.showMessageDialog(this, "Item removed from cart.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "The cart is empty.");
        }
    }                                         

    private void btnAddToCartActionPerformed(java.awt.event.ActionEvent evt) {                                             
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            int productId = (int) model.getValueAt(selectedRow, 0); 
            String productName = (String) model.getValueAt(selectedRow, 1); 
            double productPrice = Double.parseDouble((String) model.getValueAt(selectedRow, 3)); 
            int availableStock = Integer.parseInt((String) model.getValueAt(selectedRow, 4)); 

            int currentQuantityInCart = 0;
            for (CartItem item : cart) {
                if (item.getProductId() == productId) {
                    currentQuantityInCart++;
                }
            }

            if (currentQuantityInCart >= availableStock) {
                JOptionPane.showMessageDialog(this, "Cannot add more than available stock of " + productName + ".");
            } else {
                addToCart(productId, productName, productPrice);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to add to the cart.");
        }
    }                                            

    private void loadTableData() {
        try {
            String query = "SELECT * FROM products";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); 

            while (rs.next()) {
                int id = rs.getInt("PRODUCT_ID");
                String cat = rs.getString("CATEGORY");
                String name = rs.getString("NAME");
                String price = rs.getString("PRICE");
                String qty = rs.getString("NO. OF STOCKS");
                byte[] imageData = rs.getBytes("IMAGE"); 

                model.addRow(new Object[]{id, name, cat, price, qty, imageData});
            }

            rs.close();
            stmt.close();

            jTable1.getColumnModel().getColumn(5).setCellRenderer(new ImageRenderer());
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTable1 .setRowHeight(100);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                byte[] imageData = (byte[]) value;
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(image));
            } else {
                setIcon(null);
            }
            return this;
        }
    }

    public void Connect() {
        String url = "jdbc:mysql://localhost:3306/beyondcollection";
        String mySqlUser   = "root";
        String mySqlPass = "";

        try {
            con = DriverManager.getConnection(url, mySqlUser  , mySqlPass);
            System.out.println("Connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Connection failed: " + e.getMessage());
        }
    }

    class CartItem {
        private int productId;
        private String productName;
        private double productPrice;

        public CartItem(int productId, String productName, double productPrice) {
            this.productId = productId;
            this.productName = productName;
            this.productPrice = productPrice;
        }

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public double getProductPrice() {
            return productPrice;
        }
    }

    private class AddToCartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            btnAddToCartActionPerformed(e);
        }
    }

    private class CheckoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkout();
        }
    }

    private class CheckOrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            checkOrders();
        }
    }

    private class RemoveFromCartAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            btnRemoveActionPerformed(e);
        }
    }

    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginPage().setVisible(true); 
            dispose(); 
        }
    }

    private void addToCart(int productId, String productName, double productPrice) {
        cart.add(new CartItem(productId, productName, productPrice));
        JOptionPane.showMessageDialog(this, productName + " added to cart.");
        updateCartTable();
    }

    private void updateCartTable() {
        DefaultTableModel model = (DefaultTableModel) cartTableModel.getModel();
        model.setRowCount(0);
        for (CartItem item : cart) {
            model.addRow(new Object[]{item.getProductId(), item.getProductName(), item.getProductPrice()});
        }
    }

    private void checkout() {
    if (cart.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Your cart is empty!");
        return;
    }

    StringBuilder message = new StringBuilder("Items in your cart:\n");
    double total = 0;

    for (CartItem item : cart) {
        message.append(item.getProductName()).append(" - $").append(item.getProductPrice()).append("\n");
        total += item.getProductPrice();
    }

    message.append("Total: $").append(total);

    String paymentInput = JOptionPane.showInputDialog(this, "Enter payment amount:");
    if (paymentInput == null || paymentInput.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Payment amount cannot be empty!");
        return;
    }

    double paymentAmount;
    try {
        paymentAmount = Double.parseDouble(paymentInput);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid payment amount!");
        return;
    }

    if (paymentAmount < total) {
        JOptionPane.showMessageDialog(this, "Insufficient payment! Total is $" + total);
        return;
    }

    double change = paymentAmount - total;
    String orderStatus = "Pending";

    int deliveryDays;
    if (rbtnPriority.isSelected()) {
        int response = JOptionPane.showConfirmDialog(this, "Priority delivery incurs an additional charge of $5. Do you want to proceed?", "Confirm Priority Delivery", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.NO_OPTION) {
            return; 
        }
        total += 5; 
        deliveryDays = (int)(Math.random() * (4 - 1)) + 1; 
    } else if (rbtnStandard.isSelected()) {
        deliveryDays = 1 + (int)(Math.random() * 7); 
    } else {
        JOptionPane.showMessageDialog(this, "Please select a delivery option.");
        return;
    }

    // Generate estimated delivery date
    java.util.Date date = new java.util.Date();
    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(java.util.Calendar.DAY_OF_YEAR, deliveryDays);
    java.sql.Date estimatedArrivalDate = new java.sql.Date(calendar.getTimeInMillis());

    String insertOrderSQL = "INSERT INTO orders (product_id, product_name, quantity, total_price, username, status, est_time_of_arrival) VALUES (?, ?, ?, ?, ?, ?, ?)";
    String updateStockSQL = "UPDATE products SET `NO. OF STOCKS` = `NO. OF STOCKS` - 1 WHERE PRODUCT_ID = ?";

    try {
        PreparedStatement pstmt = con.prepareStatement(insertOrderSQL);
        PreparedStatement updateStockPstmt = con.prepareStatement(updateStockSQL);

        for (CartItem item : cart) {
            pstmt.setInt(1, item.getProductId());
            pstmt.setString(2, item.getProductName());
            pstmt.setInt(3, 1); 
            pstmt.setDouble(4, item.getProductPrice());
            pstmt.setString(5, loggedInUsername);
            pstmt.setString(6, orderStatus);
            pstmt.setDate(7, estimatedArrivalDate); // Set the estimated arrival date

            pstmt.executeUpdate();

            updateStockPstmt.setInt(1, item.getProductId());
            updateStockPstmt.executeUpdate();
        }

        cart.clear();
        updateCartTable(); 
        loadTableData();
        
        JOptionPane.showMessageDialog(this, "Order completed successfully!\n" + message.toString() + "\nTotal after delivery charge: $" + total + "\nPayment received: $" + paymentAmount + "\nChange: $" + change + "\nEstimated Delivery Date: " + estimatedArrivalDate, "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);

        pstmt.close();
        updateStockPstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error during checkout: " + e.getMessage());
    }
}
    private void checkOrders() {
        if (loggedInUsername == null || loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must be logged in to check your orders.");
            return;
        }

        StringBuilder message = new StringBuilder("Your Orders:\n");
        String query = "SELECT * FROM orders WHERE username = ?";

        try {
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, loggedInUsername);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) { 
                JOptionPane.showMessageDialog(this, "You have no orders.");
                return;
            }

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");
                String status = rs.getString("status");
                message.append("Product ID: ").append(productId)
                       .append(", Product Name: ").append(productName)
                       .append(", Quantity: ").append(quantity)
                       .append(", Total Price: $").append(totalPrice)
                       .append(", Status: ").append(status).append("\n");
            }

            JOptionPane.showMessageDialog(this, message.toString(), "Order History", JOptionPane.INFORMATION_MESSAGE);
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving orders: " + e.getMessage());
        }
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnCheckOrder;
    private javax.swing.JTable cartTableModel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration                   
}