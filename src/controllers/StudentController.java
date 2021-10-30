/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.StudentModel;

/**
 *
 * @author richa
 */
public class StudentController {
    public static List<StudentModel> findAll() {
        List<StudentModel> studentList = new ArrayList<>();
        
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURl = "jdbc:sqlserver://localhost:1433;databaseName=java;user=sa;password=annowit;";
            connection = DriverManager.getConnection(dbURl);
            String sql = "SELECT * FROM student";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                StudentModel std = new StudentModel(
                        resultSet.getInt("id"),
                        resultSet.getString("mssv"),
                        resultSet.getString("ten"),
                        resultSet.getString("lop"),
                        resultSet.getString("nganh"),
                        resultSet.getInt("khoa"),
                        resultSet.getDate("ngaySinh")
                );
                studentList.add(std);
            }
            connection.close();
            
        } catch (Exception ex) {
            ex.printStackTrace();

        } 
        
        return studentList;
    }
}
