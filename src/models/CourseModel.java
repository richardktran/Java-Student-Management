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
public class CourseModel {
    int id;
    String maHP, tenHP;
    int tinChi;

    public CourseModel(String maHP, String tenHP, int tinChi) {
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.tinChi = tinChi;
    }

    public CourseModel(int id, String maHP, String tenHP, int tinChi) {
        this.id = id;
        this.maHP = maHP;
        this.tenHP = tenHP;
        this.tinChi = tinChi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaHP() {
        return maHP;
    }

    public void setMaHP(String maHP) {
        this.maHP = maHP;
    }

    public String getTenHP() {
        return tenHP;
    }

    public void setTenHP(String tenHP) {
        this.tenHP = tenHP;
    }

    public int getTinChi() {
        return tinChi;
    }

    public void setTinChi(int tinChi) {
        this.tinChi = tinChi;
    }
    
}
