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
import javafx.util.Pair;
import models.ScoreModel;
import models.StudentModel;
import studentmanagement.DBConnection;

/**
 *
 * @author richa
 */
public class ScoreController {
    Connection connection = null;

    public static List<ScoreModel> findAll() {
        List<ScoreModel> scoreList = new ArrayList<>();
        Statement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM score ORDER BY id DESC";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ScoreModel score = new ScoreModel(
                        resultSet.getInt("id"),
                        StudentController.findById(resultSet.getInt("sinhvien_id")),
                        CourseController.findById(resultSet.getInt("monhoc_id")),
                        resultSet.getDouble("diem"),
                        resultSet.getString("namhoc"),
                        resultSet.getString("hocky")
                );
                scoreList.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreList;
    }
    
    public static List<ScoreModel> findScoreStudent(StudentModel student, String namhoc, String hocky) {
        List<ScoreModel> scoreList = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM score WHERE sinhvien_id=? AND namhoc=? and hocky=?";
            
            statement = connection.prepareCall(sql);
            statement.setInt(1, student.getId());
            statement.setString(2, namhoc);
            statement.setString(3, hocky);
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ScoreModel score = new ScoreModel(
                        resultSet.getInt("id"),
                        StudentController.findById(resultSet.getInt("sinhvien_id")),
                        CourseController.findById(resultSet.getInt("monhoc_id")),
                        resultSet.getDouble("diem"),
                        resultSet.getString("namhoc"),
                        resultSet.getString("hocky")
                );
                scoreList.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreList;
    }
    
    public static List<ScoreModel> findScoreStudentAll(StudentModel student) {
        List<ScoreModel> scoreList = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "SELECT * FROM score WHERE sinhvien_id=?";
            
            statement = connection.prepareCall(sql);
            statement.setInt(1, student.getId());
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                ScoreModel score = new ScoreModel(
                        resultSet.getInt("id"),
                        StudentController.findById(resultSet.getInt("sinhvien_id")),
                        CourseController.findById(resultSet.getInt("monhoc_id")),
                        resultSet.getDouble("diem"),
                        resultSet.getString("namhoc"),
                        resultSet.getString("hocky")
                );
                System.out.println(score.getDiem());
                scoreList.add(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scoreList;
    }
    
    
    public static List<Pair<String, String>> findHocKyNamHoc(StudentModel student) {
        List<Pair<String, String>> namhocList = new ArrayList<Pair<String, String>>();
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "select DISTINCT namhoc, hocky from score WHERE sinhvien_id=? ";
            
            statement = connection.prepareCall(sql);
            statement.setInt(1, student.getId());
            
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String nh = resultSet.getString("namhoc");
                String hk = resultSet.getString("hocky");
                Pair<String,String> pair = new Pair<>(nh, hk); 
                namhocList.add(pair);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return namhocList;
    }

    public static void insert(ScoreModel score) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "INSERT INTO score(sinhvien_id, monhoc_id, diem, namhoc,hocky) values(?,?,?,?,?)";
            statement = connection.prepareCall(sql);
            statement.setInt(1, score.getSinhvien().getId());
            statement.setInt(2, score.getMonhoc().getId());
            statement.setDouble(3, score.getDiem());
            statement.setString(4, score.getNamhoc());
            statement.setString(5, score.getHocky());
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void update(ScoreModel score) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "UPDATE score set sinhvien_id=?, monhoc_id=?, diem=?, namhoc=?, hocky=? where id=?";
            statement = connection.prepareCall(sql);
            statement.setInt(1, score.getSinhvien().getId());
            statement.setInt(2, score.getMonhoc().getId());
            statement.setDouble(3, score.getDiem());
            statement.setString(4, score.getNamhoc());
            statement.setString(5, score.getHocky());
            statement.setInt(6, score.getId());
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void delete(int id) {
        PreparedStatement statement = null;
        try {
            Connection connection = DBConnection.connection;
            String sql = "DELETE score where id=?";
            statement = connection.prepareCall(sql);
            statement.setInt(1, id);
            statement.execute();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
