/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.StudentModel;
import studentmanagement.DBConnection;

/**
 *
 * @author richa
 */
public class StudentController {

    Connection connection = null;

    public static List<StudentModel> findAll() {
        List<StudentModel> studentList = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM student ORDER BY id DESC";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentList;
    }
    
    public static StudentModel findById(int id) {
        List<StudentModel> studentList = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM student WHERE id="+id;
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(studentList.size()>0){
            return studentList.get(0);
        } else{
            return new StudentModel(-1);
        }
    }
    
    public static StudentModel findByMSSV(String mssv) {
        List<StudentModel> studentList = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM student WHERE mssv='"+mssv+"'";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(studentList.size()>0){
            return studentList.get(0);
        } else{
            return new StudentModel(-1);
        }
    }

    public static void insert(StudentModel std) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "INSERT INTO student(mssv, ten, khoa, nganh, lop, ngaySinh) values(?,?,?,?,?,?)";
            statement = connection.prepareCall(sql);
            statement.setString(1, std.getMssv());
            statement.setString(2, std.getTen());
            statement.setInt(3, std.getKhoa());
            statement.setString(4, std.getNganh());
            statement.setString(5, std.getLop());
            java.sql.Date sqlDate = new java.sql.Date(std.getNgaySinh().getTime());
            statement.setDate(6, sqlDate);
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void update(StudentModel std) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "UPDATE student set mssv=?, ten=?, khoa=?, nganh=?, lop=?, ngaySinh=? where id=?";
            statement = connection.prepareCall(sql);
            statement.setString(1, std.getMssv());
            statement.setString(2, std.getTen());
            statement.setInt(3, std.getKhoa());
            statement.setString(4, std.getNganh());
            statement.setString(5, std.getLop());
            java.sql.Date sqlDate = new java.sql.Date(std.getNgaySinh().getTime());
            statement.setDate(6, sqlDate);
            statement.setInt(7, std.getId());
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void delete(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "DELETE student where id=?";
            statement = connection.prepareCall(sql);
            statement.setInt(1, id);
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
