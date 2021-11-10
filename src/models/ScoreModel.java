/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author richa
 */
public class ScoreModel {
    int id;
    StudentModel sinhvien;
    CourseModel monhoc;
    String namhoc, hocky;

    public ScoreModel(StudentModel sinhvien, CourseModel monhoc, String namhoc, String hocky) {
        this.sinhvien = sinhvien;
        this.monhoc = monhoc;
        this.namhoc = namhoc;
        this.hocky = hocky;
    }

    public ScoreModel(int id, StudentModel sinhvien, CourseModel monhoc, String namhoc, String hocky) {
        this.id = id;
        this.sinhvien = sinhvien;
        this.monhoc = monhoc;
        this.namhoc = namhoc;
        this.hocky = hocky;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudentModel getSinhvien() {
        return sinhvien;
    }

    public void setSinhvien(StudentModel sinhvien) {
        this.sinhvien = sinhvien;
    }

    public CourseModel getMonhoc() {
        return monhoc;
    }

    public void setMonhoc(CourseModel monhoc) {
        this.monhoc = monhoc;
    }

    public String getNamhoc() {
        return namhoc;
    }

    public void setNamhoc(String namhoc) {
        this.namhoc = namhoc;
    }

    public String getHocky() {
        return hocky;
    }

    public void setHocky(String hocky) {
        this.hocky = hocky;
    }
    
    
}
