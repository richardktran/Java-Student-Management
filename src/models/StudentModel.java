/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Date;

/**
 *
 * @author richa
 */
public class StudentModel {
    int id, khoa;
    String mssv, ten, lop, nganh;
    Date ngaySinh;

    public StudentModel() {
    }

    public StudentModel(String mssv, String ten, String lop, String nganh, int khoa, Date ngaySinh) {
        this.mssv = mssv;
        this.ten = ten;
        this.lop = lop;
        this.nganh = nganh;
        this.khoa = khoa;
        this.ngaySinh = ngaySinh;
    }
    

    public StudentModel(int id, String mssv, String ten, String lop, String nganh, int khoa, Date ngaySinh) {
        this.id = id;
        this.mssv = mssv;
        this.ten = ten;
        this.lop = lop;
        this.nganh = nganh;
        this.khoa = khoa;
        this.ngaySinh = ngaySinh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getNganh() {
        return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    public int getKhoa() {
        return khoa;
    }

    public void setKhoa(int khoa) {
        this.khoa = khoa;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
    
    
    
}
