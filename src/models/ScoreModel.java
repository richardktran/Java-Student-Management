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
    double diem;
    String namhoc, hocky;

    public ScoreModel() {
    }
    
    

    public ScoreModel(StudentModel sinhvien, CourseModel monhoc, double diem, String namhoc, String hocky) {
        this.sinhvien = sinhvien;
        this.monhoc = monhoc;
        this.diem = diem;
        this.namhoc = namhoc;
        this.hocky = hocky;
    }

    public ScoreModel(int id, StudentModel sinhvien, CourseModel monhoc, double diem, String namhoc, String hocky) {
        this.id = id;
        this.sinhvien = sinhvien;
        this.monhoc = monhoc;
        this.diem = diem;
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

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
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

    public String getDiemChu(){
        if(this.diem>=9.0){
            return "A";
        }
        if(this.diem>=8.0){
            return "B+";
        }
        if(this.diem>=7.0){
            return "B";
        }
        if(this.diem>=6.5){
            return "C+";
        }
        if(this.diem>=5.5){
            return "C";
        }
        
        if(this.diem>=5.0){
            return "D+";
        }
        if(this.diem>=4.0){
            return "D";
        }
        
        return "F";
    }

    
    
}
