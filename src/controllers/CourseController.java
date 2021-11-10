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
import models.CourseModel;
import models.StudentModel;
import studentmanagement.DBConnection;

/**
 *
 * @author richa
 */
public class CourseController {
    Connection connection = null;

    public static List<CourseModel> findAll() {
        List<CourseModel> courseList = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM course";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                CourseModel course = new CourseModel(
                        resultSet.getInt("id"),
                        resultSet.getString("mhp"),
                        resultSet.getString("tenhp"),
                        resultSet.getInt("tinchi")
                );
                courseList.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseList;
    }

    public static void insert(CourseModel course) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "INSERT INTO course(mhp, tenhp, tinchi) values(?,?,?)";
            statement = connection.prepareCall(sql);
            statement.setString(1, course.getMaHP());
            statement.setString(2, course.getTenHP());
            statement.setInt(3, course.getTinChi());
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void update(CourseModel course) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "UPDATE course set mhp=?, tenhp=?, tinchi=? where id=?";
            statement = connection.prepareCall(sql);
            statement.setString(1, course.getMaHP());
            statement.setString(2, course.getTenHP());
            statement.setInt(3, course.getTinChi());
            statement.setInt(4, course.getId());
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void delete(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "DELETE course where id=?";
            statement = connection.prepareCall(sql);
            statement.setInt(1, id);
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
