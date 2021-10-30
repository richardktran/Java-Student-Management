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
    StudentModel student;
    CourseModel course;

    public ScoreModel(StudentModel student, CourseModel course) {
        this.student = student;
        this.course = course;
    }

    public ScoreModel(int id, StudentModel student, CourseModel course) {
        this.id = id;
        this.student = student;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public CourseModel getCourse() {
        return course;
    }

    public void setCourse(CourseModel course) {
        this.course = course;
    }
    
}
