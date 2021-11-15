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
            String sql = "SELECT * FROM score";
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