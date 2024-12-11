/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beyondcollection;

import java.sql.*;


public class CustomerSupportConnection {
    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/beyondcollection";
        String user = "root";
        String password = "root"; 
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
