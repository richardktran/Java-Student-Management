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
public class CurrentStudent {
    public static StudentModel currentStudent=new StudentModel();

    public static StudentModel getStudent() {
        return new StudentModel(currentStudent);
    }
    
    public static void setStudent(StudentModel std){
        currentStudent = new StudentModel(std);
    }
    
}
