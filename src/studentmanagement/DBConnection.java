/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentmanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author richa
 */
public class DBConnection {
    public static Connection connection=null;
    public static void connectDB(JFrame frame){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURl = "jdbc:sqlserver://localhost:1433;databaseName=java;user=sa;password=annowit;";
            connection = DriverManager.getConnection(dbURl);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Kết nối database thất bại");
            System.exit(0);
        }
    }
}
