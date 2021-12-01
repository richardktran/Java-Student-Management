/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentmanagement;

import controllers.CourseController;
import controllers.ScoreController;
import controllers.StudentController;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import models.AdminModel;
import models.CourseModel;
import models.CurrentStudent;
import models.ScoreModel;
import models.StudentModel;

/**
 *
 * @author richa
 */
public class DashboardForm extends javax.swing.JFrame {

    Connection connection = null;
    DefaultTableModel studentTable;
    List<StudentModel> studentList = new ArrayList<>();
    private TableRowSorter<TableModel> rowSorterStudent;

    DefaultTableModel courseTable;
    List<CourseModel> courseList = new ArrayList<>();
    private TableRowSorter<TableModel> rowSorterCourse;
    
    DefaultTableModel scoreTable;
    List<ScoreModel> scoreList = new ArrayList<>();
    private TableRowSorter<TableModel> rowSorterScore;

    /**
     * Creates new form DashboardForm
     */
    public DashboardForm() {
        initComponents();
        initState();
        
    }

    private void initState() {
        DBConnection.connectDB(this);
        connection = DBConnection.connection;
        scaleImage(lbAvatar, "src/images/avatar.png");
        lbName.setText(AdminModel.name);
        pnStudent.setVisible(true);
        pnScore.setVisible(false);
        pnCourse.setVisible(false);
        InitStudentState();
        InitCourseState();
        InitScoreState();
    }

    //<editor-fold defaultstate="collapsed" desc="HELPER FUNCTIONS"> 
    private void scaleImage(JLabel lb, String url) {
        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(lb.getWidth(), lb.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon background = new ImageIcon(img);
        lb.setIcon(background);
    }

    private int getIndexComboBoxFromString(JComboBox cbb, String str) {
        int count = cbb.getItemCount();
        int index = -1;
        for (int i = 0; i < count; i++) {
            String value = cbb.getItemAt(i).toString();
            if (value.equals(str)) {
                index = i;
                break;
            }
        }
        return index;
    }

    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="STUDENT HANDLE"> 
    private void InitStudentState() {
        txtDOBStudent.setFormats("dd-MM-yyyy");
        studentTable = (DefaultTableModel) tbStudent.getModel();
        //Tim kiem
        rowSorterStudent = new TableRowSorter<>(tbStudent.getModel());
        tbStudent.setRowSorter(rowSorterStudent);
        showStudent();
    }

    private void showStudent() {
        studentList = StudentController.findAll();
        studentTable.setRowCount(0);
        studentList.forEach((student) -> {
            studentTable.addRow(new Object[]{
                studentTable.getRowCount() + 1,
                student.getMssv(),
                student.getTen(),
                student.getNgaySinh(),
                student.getLop(),
                student.getNganh(),
                student.getKhoa(),});
        });
    }

    private void clearFormStudent() {
        txtMSSVStudent.setText("");
        txtTenStudent.setText("");
        txtLopStudent.setText("");
        txtNganhStudent.setSelectedIndex(0);
        txtKhoaStudent.setSelectedIndex(0);
        txtDOBStudent.setDate(null);
        tbStudent.clearSelection();
        btnAddUpdateStudent.setText("Thêm");
    }

    private void clickToSelectStudent() {
        int selectedIndex = tbStudent.getSelectedRow();
        if (selectedIndex >= 0) {
            StudentModel std = getStudentFromSelectedIndex(selectedIndex);
            txtMSSVStudent.setText(std.getMssv());
            txtTenStudent.setText(std.getTen());
            txtLopStudent.setText(std.getLop());
            txtDOBStudent.setDate(std.getNgaySinh());
            int indexNganh = getIndexComboBoxFromString(txtNganhStudent, std.getNganh());
            int indexKhoa = getIndexComboBoxFromString(txtKhoaStudent, Integer.toString(std.getKhoa()));
            txtNganhStudent.setSelectedIndex(indexNganh);
            txtKhoaStudent.setSelectedIndex(indexKhoa);
        }
        btnAddUpdateStudent.setText("Cập nhật");
    }
    
    private void clickToOpenScoreDetailForm(){
        int selectedIndex = tbStudent.getSelectedRow();
        if (selectedIndex >= 0) {
            System.out.println(selectedIndex);
            StudentModel std = getStudentFromSelectedIndex(selectedIndex);
            CurrentStudent.currentStudent= std;
            ScoreDetailFrm scoreFrm = new ScoreDetailFrm();
            scoreFrm.setVisible(true);
        }
    }

    private void editStudent() {
        int selectedIndex = tbStudent.getSelectedRow();
        StudentModel std = getStudentFromSelectedIndex(selectedIndex);
        int id = std.getId();
        String mssv = txtMSSVStudent.getText();
        String ten = txtTenStudent.getText();
        String lop = txtLopStudent.getText();
        String nganh = txtNganhStudent.getSelectedItem().toString();
        int khoa = Integer.parseInt(txtKhoaStudent.getSelectedItem().toString());
        Date ngaySinh = txtDOBStudent.getDate();
        StudentModel newStd = new StudentModel(id, mssv, ten, lop, nganh, khoa, ngaySinh);
        try {
            StudentController.update(newStd);
            JOptionPane.showMessageDialog(this, "Cập nhật sinh viên thành công");
            clearFormStudent();
            showStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStudent() throws NumberFormatException {
        String mssv = txtMSSVStudent.getText();
        String ten = txtTenStudent.getText();
        String lop = txtLopStudent.getText();
        String nganh = txtNganhStudent.getSelectedItem().toString();
        int khoa = Integer.parseInt(txtKhoaStudent.getSelectedItem().toString());
        Date ngaySinh = txtDOBStudent.getDate();
        StudentModel std = new StudentModel(mssv, ten, lop, nganh, khoa, ngaySinh);
        try {
            StudentController.insert(std);
            JOptionPane.showMessageDialog(this, "Thêm sinh viên thành công");
            clearFormStudent();
            showStudent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent() throws HeadlessException {
        //Xoa sinh vien
        int selectedIndex = tbStudent.getSelectedRow();
        if (selectedIndex >= 0) {
            StudentModel std = studentList.get(selectedIndex);

            int opt = JOptionPane.showConfirmDialog(this, "Bạn có muốn xoá sinh viên này?");
            if (opt == 0) {
                StudentController.delete(std.getId());
                showStudent();
            }
        }
    }

    private void searchStudent() {
        String text = txtSearchStudent.getText();

        if (text.trim().length() == 0) {
            rowSorterStudent.setRowFilter(null);
        } else {
            rowSorterStudent.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private StudentModel getStudentFromSelectedIndex(int row) {
        String mssv = tbStudent.getValueAt(row, 1).toString();
        StudentModel std = new StudentModel();
        for (StudentModel student : studentList) {
            if (mssv.equals(student.getMssv())) {
                std = student;
                break;
            }
        }
        return std;
    }

    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COURSE HANDLE"> 
    private void InitCourseState() {
        courseTable = (DefaultTableModel) tbCourse.getModel();
        //Tim kiem
        rowSorterCourse = new TableRowSorter<>(tbCourse.getModel());
        tbCourse.setRowSorter(rowSorterCourse);
        showCourse();
    }

    private void showCourse() {
        courseList = CourseController.findAll();
        courseTable.setRowCount(0);
        courseList.forEach((course) -> {
            courseTable.addRow(new Object[]{
                courseTable.getRowCount() + 1,
                course.getMaHP(),
                course.getTenHP(),
                course.getTinChi()
            });
        });
    }

    private void clearFormCourse() {
        txtMHPCourse.setText("");
        txtTenHPCourse.setText("");
        txtTinChiCourse.setText("");
        tbCourse.clearSelection();
        btnAddUpdateCourse.setText("Thêm");
    }

    private void clickToSelectCourse() {
        int selectedIndex = tbCourse.getSelectedRow();
        if (selectedIndex >= 0) {
            CourseModel course = getCourseFromSelectedIndex(selectedIndex);
            txtMHPCourse.setText(course.getMaHP());
            txtTenHPCourse.setText(course.getTenHP());
            txtTinChiCourse.setText(Integer.toString(course.getTinChi()));
        }
        btnAddUpdateCourse.setText("Cập nhật");
    }

    private void editCourse() {
        int selectedIndex = tbCourse.getSelectedRow();
        CourseModel course = getCourseFromSelectedIndex(selectedIndex);
        int id = course.getId();
        String mhp = txtMHPCourse.getText();
        String tenhp = txtTenHPCourse.getText();
        int tinchi = Integer.parseInt(txtTinChiCourse.getText());
        
        CourseModel newCourse = new CourseModel(id, mhp, tenhp, tinchi);
        try {
            CourseController.update(newCourse);
            JOptionPane.showMessageDialog(this, "Cập nhật môn học thành công");
            clearFormCourse();
            showCourse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCourse() throws NumberFormatException {
        String mhp = txtMHPCourse.getText();
        String tenhp = txtTenHPCourse.getText();
        int tinchi = Integer.parseInt(txtTinChiCourse.getText());
        
        CourseModel course = new CourseModel(mhp, tenhp, tinchi);
        try {
            CourseController.insert(course);
            JOptionPane.showMessageDialog(this, "Thêm môn học thành công");
            clearFormCourse();
            showCourse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse() throws HeadlessException {
        int selectedIndex = tbCourse.getSelectedRow();
        if (selectedIndex >= 0) {
            CourseModel course = courseList.get(selectedIndex);

            int opt = JOptionPane.showConfirmDialog(this, "Bạn có muốn xoá môn học này?");
            if (opt == 0) {
                CourseController.delete(course.getId());
                showCourse();
            }
        }
    }

    private void searchCourse() {
        String text = txtSearchCourse.getText();

        if (text.trim().length() == 0) {
            rowSorterCourse.setRowFilter(null);
        } else {
            rowSorterCourse.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private CourseModel getCourseFromSelectedIndex(int row) {
        String mhp = tbCourse.getValueAt(row, 1).toString();
        CourseModel courseIndex = new CourseModel();
        for (CourseModel course : courseList) {
            if (mhp.equals(course.getMaHP())) {
                courseIndex = course;
                break;
            }
        }
        return courseIndex;
    }

    // </editor-fold>
    
    //    <editor-fold defaultstate="collapsed" desc="SCORE HANDLE"> 
    private void InitScoreState() {
        scoreTable = (DefaultTableModel) tbScore.getModel();
        //Tim kiem
        rowSorterScore = new TableRowSorter<>(tbScore.getModel());
        tbScore.setRowSorter(rowSorterScore);
        showScore();
    }

    private void showScore() {
        scoreList = ScoreController.findAll();
        scoreTable.setRowCount(0);
        scoreList.forEach((score) -> {
            scoreTable.addRow(new Object[]{
                scoreTable.getRowCount() + 1,
                score.getSinhvien().getMssv(),
                score.getSinhvien().getTen(),
                score.getMonhoc().getTenHP(),
                score.getDiem(),
                score.getDiemChu(),
                score.getNamhoc(),
                score.getHocky()
            });
        });
    }

    private void clearFormScore() {
        txtMHPScore.setText("");
        txtMSSVScore.setText("");
        txtDiemScore.setText("");
        
        cbHocKyScore.setSelectedIndex(0);
        cbNamHocScore.setSelectedIndex(0);
        tbScore.clearSelection();
        btnAddUpdateScore.setText("Thêm");
    }

    private void clickToSelectScore() {
        int selectedIndex = tbScore.getSelectedRow();
        if (selectedIndex >= 0) {
            ScoreModel score = getScoreFromSelectedIndex(selectedIndex);
            
            txtMHPScore.setText(score.getMonhoc().getMaHP());
            txtMSSVScore.setText(score.getSinhvien().getMssv());
            txtDiemScore.setText(Double.toString(score.getDiem()));
            int indexHocKy = getIndexComboBoxFromString(cbHocKyScore, score.getHocky());
            int indexNamHoc = getIndexComboBoxFromString(cbNamHocScore, score.getNamhoc());
            cbHocKyScore.setSelectedIndex(indexHocKy);
            cbNamHocScore.setSelectedIndex(indexNamHoc);
        }
        btnAddUpdateScore.setText("Cập nhật");
    }
    
    private ScoreModel getScoreFromSelectedIndex(int row) {
        String mssv = tbScore.getValueAt(row, 1).toString();
        String tenHP = tbScore.getValueAt(row, 3).toString();
        ScoreModel scoreIndex = new ScoreModel();
        for (ScoreModel score : scoreList) {
            if (mssv.equals(score.getSinhvien().getMssv()) && tenHP.equals(score.getMonhoc().getTenHP())) {
                scoreIndex = score;
                break;
            }
        }
        return scoreIndex;
    }

    private void editScore() {
        int selectedIndex = tbScore.getSelectedRow();
        ScoreModel score = getScoreFromSelectedIndex(selectedIndex);
        int id = score.getId();
        StudentModel std = StudentController.findByMSSV(txtMSSVScore.getText());
        if(std.getId()==-1){
            JOptionPane.showMessageDialog(this, "Không tồn tại mã số sinh viên này");
            return;
        }
        CourseModel course = CourseController.findByMHP(txtMHPScore.getText());
        
        if(course.getId()==-1){
            JOptionPane.showMessageDialog(this, "Không tồn tại mã học phần này");
            return;
        }
        
        String namhoc = cbNamHocScore.getSelectedItem().toString();
        String hocky = cbHocKyScore.getSelectedItem().toString();
        double diem = Double.parseDouble(txtDiemScore.getText());
        
        ScoreModel newScore = new ScoreModel(id, std, course, diem, namhoc, hocky);
        try {
            ScoreController.update(newScore);
            JOptionPane.showMessageDialog(this, "Cập nhật điểm thành công");
            clearFormScore();
            showScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addScore() throws NumberFormatException {
        StudentModel std = StudentController.findByMSSV(txtMSSVScore.getText());
        if(std.getId()==-1){
            JOptionPane.showMessageDialog(this, "Không tồn tại mã số sinh viên này");
            return;
        }
        CourseModel course = CourseController.findByMHP(txtMHPScore.getText());
        if(course.getId()==-1){
            JOptionPane.showMessageDialog(this, "Không tồn tại mã học phần này");
            return;
        }
        String namhoc = cbNamHocScore.getSelectedItem().toString();
        String hocky = cbHocKyScore.getSelectedItem().toString();
        double diem = Double.parseDouble(txtDiemScore.getText());
        ScoreModel score = new ScoreModel(std, course, diem, namhoc, hocky);
        try {
            ScoreController.insert(score);
            JOptionPane.showMessageDialog(this, "Thêm điểm thành công");
            clearFormScore();
            showScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteScore() throws HeadlessException {
        int selectedIndex = tbScore.getSelectedRow();
        if (selectedIndex >= 0) {
            ScoreModel score = scoreList.get(selectedIndex);

            int opt = JOptionPane.showConfirmDialog(this, "Bạn có muốn xoá điểm của sinh viên này?");
            if (opt == 0) {
                ScoreController.delete(score.getId());
                showScore();
            }
        }
    }

    private void searchScore() {
        String text = txtSearchScore.getText();

        if (text.trim().length() == 0) {
            rowSorterScore.setRowFilter(null);
        } else {
            rowSorterScore.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    

    // </editor-fold>
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        pnSideBar = new javax.swing.JPanel();
        lbAvatar = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        btnStudent = new javax.swing.JButton();
        btnCourse = new javax.swing.JButton();
        btnScore = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnStudent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearchStudent = new javax.swing.JTextField();
        btnSearchStudent = new javax.swing.JButton();
        pnTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbStudent = new javax.swing.JTable();
        pnFormStudent = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMSSVStudent = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTenStudent = new javax.swing.JTextField();
        txtLopStudent = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtKhoaStudent = new javax.swing.JComboBox<>();
        txtNganhStudent = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtDOBStudent = new org.jdesktop.swingx.JXDatePicker();
        btnAddUpdateStudent = new javax.swing.JButton();
        btnClearStudent = new javax.swing.JButton();
        btnDeleteStudent = new javax.swing.JButton();
        pnCourse = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtSearchCourse = new javax.swing.JTextField();
        btnSearchCourse = new javax.swing.JButton();
        pnTable1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCourse = new javax.swing.JTable();
        pnForm1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMHPCourse = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTenHPCourse = new javax.swing.JTextField();
        txtTinChiCourse = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnAddUpdateCourse = new javax.swing.JButton();
        btnClearCourse = new javax.swing.JButton();
        btnDeleteCourse = new javax.swing.JButton();
        pnScore = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtSearchScore = new javax.swing.JTextField();
        btnSearchScore = new javax.swing.JButton();
        pnTable2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbScore = new javax.swing.JTable();
        pnFormScore = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtMSSVScore = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMHPScore = new javax.swing.JTextField();
        txtDiemScore = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnAddUpdateScore = new javax.swing.JButton();
        btnClearScore = new javax.swing.JButton();
        btnDeleteScore = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbHocKyScore = new javax.swing.JComboBox<>();
        cbNamHocScore = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hệ thống quản lý sinh viên");

        bg.setBackground(new java.awt.Color(2, 3, 10));

        pnSideBar.setBackground(new java.awt.Color(4, 9, 33));

        lbAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/avatar.png"))); // NOI18N

        lbName.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        lbName.setForeground(new java.awt.Color(255, 255, 255));
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setText("Trần Đăng Khoa");

        btnStudent.setBackground(new java.awt.Color(129, 97, 197));
        btnStudent.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnStudent.setForeground(new java.awt.Color(255, 255, 255));
        btnStudent.setText("Quản lý sinh viên");
        btnStudent.setBorder(null);
        btnStudent.setContentAreaFilled(false);
        btnStudent.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStudent.setFocusPainted(false);
        btnStudent.setOpaque(true);
        btnStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentActionPerformed(evt);
            }
        });

        btnCourse.setBackground(new java.awt.Color(4, 9, 33));
        btnCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnCourse.setText("Quản lý môn học");
        btnCourse.setBorder(null);
        btnCourse.setContentAreaFilled(false);
        btnCourse.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCourse.setFocusPainted(false);
        btnCourse.setOpaque(true);
        btnCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCourseActionPerformed(evt);
            }
        });

        btnScore.setBackground(new java.awt.Color(4, 9, 33));
        btnScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnScore.setForeground(new java.awt.Color(255, 255, 255));
        btnScore.setText("Quản lý điểm");
        btnScore.setBorder(null);
        btnScore.setContentAreaFilled(false);
        btnScore.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnScore.setFocusPainted(false);
        btnScore.setOpaque(true);
        btnScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnSideBarLayout = new javax.swing.GroupLayout(pnSideBar);
        pnSideBar.setLayout(pnSideBarLayout);
        pnSideBarLayout.setHorizontalGroup(
            pnSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnSideBarLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(lbAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        pnSideBarLayout.setVerticalGroup(
            pnSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSideBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbAvatar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnScore, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(360, Short.MAX_VALUE))
        );

        pnStudent.setBackground(new java.awt.Color(2, 3, 10));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Danh sách sinh viên");

        txtSearchStudent.setBackground(new java.awt.Color(2, 3, 10));
        txtSearchStudent.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        txtSearchStudent.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchStudent.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(76, 78, 88), java.awt.Color.gray));
        txtSearchStudent.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSearchStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchStudentActionPerformed(evt);
            }
        });

        btnSearchStudent.setBackground(new java.awt.Color(129, 97, 197));
        btnSearchStudent.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearchStudent.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchStudent.setText("Tìm kiếm");
        btnSearchStudent.setBorder(null);
        btnSearchStudent.setContentAreaFilled(false);
        btnSearchStudent.setFocusPainted(false);
        btnSearchStudent.setOpaque(true);
        btnSearchStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchStudentActionPerformed(evt);
            }
        });

        pnTable.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);

        tbStudent.setAutoCreateRowSorter(true);
        tbStudent.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tbStudent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "MSSV", "Họ và tên", "Ngày sinh", "Lớp", "Ngành học", "Khoá học"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbStudent.setGridColor(new java.awt.Color(2, 3, 10));
        tbStudent.setOpaque(false);
        tbStudent.setRowHeight(25);
        tbStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbStudentMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbStudentMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbStudent);

        javax.swing.GroupLayout pnTableLayout = new javax.swing.GroupLayout(pnTable);
        pnTable.setLayout(pnTableLayout);
        pnTableLayout.setHorizontalGroup(
            pnTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pnTableLayout.setVerticalGroup(
            pnTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );

        pnFormStudent.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setBackground(new java.awt.Color(4, 9, 33));
        jLabel2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 59, 142));
        jLabel2.setText("MSSV:");

        jLabel3.setBackground(new java.awt.Color(4, 9, 33));
        jLabel3.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 59, 142));
        jLabel3.setText("Họ và tên:");

        jLabel4.setBackground(new java.awt.Color(4, 9, 33));
        jLabel4.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 59, 142));
        jLabel4.setText("Lớp:");

        jLabel5.setBackground(new java.awt.Color(4, 9, 33));
        jLabel5.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 59, 142));
        jLabel5.setText("Ngành học:");

        jLabel6.setBackground(new java.awt.Color(4, 9, 33));
        jLabel6.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 59, 142));
        jLabel6.setText("Khoá học:");

        txtKhoaStudent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "43", "44", "45", "46", "47" }));

        txtNganhStudent.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kỹ thuật phần mềm", "Công nghệ thông tin", "Khoa học máy tính", "Hệ thống thông tin", "Công nghệ thông tin CLC", "Mạng máy tính và truyền thông" }));
        txtNganhStudent.setBorder(null);

        jLabel7.setBackground(new java.awt.Color(4, 9, 33));
        jLabel7.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 59, 142));
        jLabel7.setText("Ngày sinh:");

        btnAddUpdateStudent.setBackground(new java.awt.Color(0, 52, 123));
        btnAddUpdateStudent.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAddUpdateStudent.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUpdateStudent.setText("Thêm");
        btnAddUpdateStudent.setBorder(null);
        btnAddUpdateStudent.setContentAreaFilled(false);
        btnAddUpdateStudent.setFocusPainted(false);
        btnAddUpdateStudent.setOpaque(true);
        btnAddUpdateStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUpdateStudentActionPerformed(evt);
            }
        });

        btnClearStudent.setBackground(new java.awt.Color(193, 125, 0));
        btnClearStudent.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnClearStudent.setForeground(new java.awt.Color(255, 255, 255));
        btnClearStudent.setText("Clear");
        btnClearStudent.setBorder(null);
        btnClearStudent.setContentAreaFilled(false);
        btnClearStudent.setFocusPainted(false);
        btnClearStudent.setOpaque(true);
        btnClearStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearStudentActionPerformed(evt);
            }
        });

        btnDeleteStudent.setBackground(new java.awt.Color(193, 20, 0));
        btnDeleteStudent.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDeleteStudent.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteStudent.setText("Xoá");
        btnDeleteStudent.setBorder(null);
        btnDeleteStudent.setContentAreaFilled(false);
        btnDeleteStudent.setFocusPainted(false);
        btnDeleteStudent.setOpaque(true);
        btnDeleteStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteStudentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnFormStudentLayout = new javax.swing.GroupLayout(pnFormStudent);
        pnFormStudent.setLayout(pnFormStudentLayout);
        pnFormStudentLayout.setHorizontalGroup(
            pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormStudentLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnFormStudentLayout.createSequentialGroup()
                        .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMSSVStudent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDOBStudent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNganhStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFormStudentLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClearStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormStudentLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(59, 59, 59)
                        .addComponent(txtLopStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormStudentLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKhoaStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormStudentLayout.createSequentialGroup()
                        .addComponent(btnAddUpdateStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(100, 100, 100))
        );
        pnFormStudentLayout.setVerticalGroup(
            pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormStudentLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMSSVStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLopStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKhoaStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNganhStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDOBStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(pnFormStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddUpdateStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout pnStudentLayout = new javax.swing.GroupLayout(pnStudent);
        pnStudent.setLayout(pnStudentLayout);
        pnStudentLayout.setHorizontalGroup(
            pnStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStudentLayout.createSequentialGroup()
                .addGap(410, 410, 410)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnStudentLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(pnStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnFormStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnStudentLayout.createSequentialGroup()
                        .addComponent(txtSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        pnStudentLayout.setVerticalGroup(
            pnStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnStudentLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnStudentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(pnTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnFormStudent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pnCourse.setBackground(new java.awt.Color(2, 3, 10));

        jLabel8.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Danh sách học phần");

        txtSearchCourse.setBackground(new java.awt.Color(2, 3, 10));
        txtSearchCourse.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        txtSearchCourse.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchCourse.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(76, 78, 88), java.awt.Color.gray));
        txtSearchCourse.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSearchCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchCourseActionPerformed(evt);
            }
        });

        btnSearchCourse.setBackground(new java.awt.Color(129, 97, 197));
        btnSearchCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearchCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchCourse.setText("Tìm kiếm");
        btnSearchCourse.setBorder(null);
        btnSearchCourse.setContentAreaFilled(false);
        btnSearchCourse.setFocusPainted(false);
        btnSearchCourse.setOpaque(true);
        btnSearchCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchCourseActionPerformed(evt);
            }
        });

        pnTable1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        tbCourse.setAutoCreateRowSorter(true);
        tbCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "Mã học phần", "Tên học phần", "Số tín chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCourse.setGridColor(new java.awt.Color(2, 3, 10));
        tbCourse.setOpaque(false);
        tbCourse.setRowHeight(25);
        tbCourse.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbCourseMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(tbCourse);

        javax.swing.GroupLayout pnTable1Layout = new javax.swing.GroupLayout(pnTable1);
        pnTable1.setLayout(pnTable1Layout);
        pnTable1Layout.setHorizontalGroup(
            pnTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
        );
        pnTable1Layout.setVerticalGroup(
            pnTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );

        pnForm1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setBackground(new java.awt.Color(4, 9, 33));
        jLabel9.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 59, 142));
        jLabel9.setText("Mã học phần:");

        jLabel10.setBackground(new java.awt.Color(4, 9, 33));
        jLabel10.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 59, 142));
        jLabel10.setText("Tên học phần:");

        jLabel11.setBackground(new java.awt.Color(4, 9, 33));
        jLabel11.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 59, 142));
        jLabel11.setText("Số tín chỉ: ");

        btnAddUpdateCourse.setBackground(new java.awt.Color(0, 52, 123));
        btnAddUpdateCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAddUpdateCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUpdateCourse.setText("Thêm");
        btnAddUpdateCourse.setBorder(null);
        btnAddUpdateCourse.setContentAreaFilled(false);
        btnAddUpdateCourse.setFocusPainted(false);
        btnAddUpdateCourse.setOpaque(true);
        btnAddUpdateCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUpdateCourseActionPerformed(evt);
            }
        });

        btnClearCourse.setBackground(new java.awt.Color(193, 125, 0));
        btnClearCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnClearCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnClearCourse.setText("Clear");
        btnClearCourse.setBorder(null);
        btnClearCourse.setContentAreaFilled(false);
        btnClearCourse.setFocusPainted(false);
        btnClearCourse.setOpaque(true);
        btnClearCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearCourseActionPerformed(evt);
            }
        });

        btnDeleteCourse.setBackground(new java.awt.Color(193, 20, 0));
        btnDeleteCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDeleteCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteCourse.setText("Xoá");
        btnDeleteCourse.setBorder(null);
        btnDeleteCourse.setContentAreaFilled(false);
        btnDeleteCourse.setFocusPainted(false);
        btnDeleteCourse.setOpaque(true);
        btnDeleteCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCourseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnForm1Layout = new javax.swing.GroupLayout(pnForm1);
        pnForm1.setLayout(pnForm1Layout);
        pnForm1Layout.setHorizontalGroup(
            pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnForm1Layout.createSequentialGroup()
                .addContainerGap(178, Short.MAX_VALUE)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTenHPCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnForm1Layout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMHPCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnForm1Layout.createSequentialGroup()
                            .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(32, 32, 32)
                            .addComponent(txtTinChiCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(167, 167, 167)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClearCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddUpdateCourse, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnDeleteCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(178, Short.MAX_VALUE))
        );
        pnForm1Layout.setVerticalGroup(
            pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnForm1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMHPCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenHPCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddUpdateCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTinChiCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnCourseLayout = new javax.swing.GroupLayout(pnCourse);
        pnCourse.setLayout(pnCourseLayout);
        pnCourseLayout.setHorizontalGroup(
            pnCourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCourseLayout.createSequentialGroup()
                .addGap(410, 410, 410)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnCourseLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(pnCourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnTable1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnCourseLayout.createSequentialGroup()
                        .addComponent(txtSearchCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearchCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnForm1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
        );
        pnCourseLayout.setVerticalGroup(
            pnCourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCourseLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnCourseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearchCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(pnTable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(pnForm1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pnScore.setBackground(new java.awt.Color(2, 3, 10));

        jLabel12.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Quản lý điểm sinh viên");

        txtSearchScore.setBackground(new java.awt.Color(2, 3, 10));
        txtSearchScore.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        txtSearchScore.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchScore.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(76, 78, 88), java.awt.Color.gray));
        txtSearchScore.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSearchScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchScoreActionPerformed(evt);
            }
        });

        btnSearchScore.setBackground(new java.awt.Color(129, 97, 197));
        btnSearchScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearchScore.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchScore.setText("Tìm kiếm");
        btnSearchScore.setBorder(null);
        btnSearchScore.setContentAreaFilled(false);
        btnSearchScore.setFocusPainted(false);
        btnSearchScore.setOpaque(true);
        btnSearchScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchScoreActionPerformed(evt);
            }
        });

        pnTable2.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(null);

        tbScore.setAutoCreateRowSorter(true);
        tbScore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "MSSV", "Tên sinh viên", "Tên học phần", "Điểm số", "Điểm chữ", "Năm học", "Học kỳ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbScore.setGridColor(new java.awt.Color(2, 3, 10));
        tbScore.setOpaque(false);
        tbScore.setRowHeight(25);
        tbScore.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbScoreMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tbScore);

        javax.swing.GroupLayout pnTable2Layout = new javax.swing.GroupLayout(pnTable2);
        pnTable2.setLayout(pnTable2Layout);
        pnTable2Layout.setHorizontalGroup(
            pnTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1015, Short.MAX_VALUE)
        );
        pnTable2Layout.setVerticalGroup(
            pnTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );

        pnFormScore.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setBackground(new java.awt.Color(4, 9, 33));
        jLabel13.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 59, 142));
        jLabel13.setText("MSSV");

        jLabel14.setBackground(new java.awt.Color(4, 9, 33));
        jLabel14.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 59, 142));
        jLabel14.setText("Mã học phần:");

        jLabel15.setBackground(new java.awt.Color(4, 9, 33));
        jLabel15.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 59, 142));
        jLabel15.setText("Điểm số:");

        btnAddUpdateScore.setBackground(new java.awt.Color(0, 52, 123));
        btnAddUpdateScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAddUpdateScore.setForeground(new java.awt.Color(255, 255, 255));
        btnAddUpdateScore.setText("Thêm");
        btnAddUpdateScore.setBorder(null);
        btnAddUpdateScore.setContentAreaFilled(false);
        btnAddUpdateScore.setFocusPainted(false);
        btnAddUpdateScore.setOpaque(true);
        btnAddUpdateScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUpdateScoreActionPerformed(evt);
            }
        });

        btnClearScore.setBackground(new java.awt.Color(193, 125, 0));
        btnClearScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnClearScore.setForeground(new java.awt.Color(255, 255, 255));
        btnClearScore.setText("Clear");
        btnClearScore.setBorder(null);
        btnClearScore.setContentAreaFilled(false);
        btnClearScore.setFocusPainted(false);
        btnClearScore.setOpaque(true);
        btnClearScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearScoreActionPerformed(evt);
            }
        });

        btnDeleteScore.setBackground(new java.awt.Color(193, 20, 0));
        btnDeleteScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDeleteScore.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteScore.setText("Xoá");
        btnDeleteScore.setBorder(null);
        btnDeleteScore.setContentAreaFilled(false);
        btnDeleteScore.setFocusPainted(false);
        btnDeleteScore.setOpaque(true);
        btnDeleteScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteScoreActionPerformed(evt);
            }
        });

        jLabel16.setBackground(new java.awt.Color(4, 9, 33));
        jLabel16.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 59, 142));
        jLabel16.setText("Học kỳ:");

        jLabel17.setBackground(new java.awt.Color(4, 9, 33));
        jLabel17.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 59, 142));
        jLabel17.setText("Năm học");

        cbHocKyScore.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "Hè" }));

        cbNamHocScore.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2020-2021", "2021-2022", "2022-2023", "2023-2024" }));

        javax.swing.GroupLayout pnFormScoreLayout = new javax.swing.GroupLayout(pnFormScore);
        pnFormScore.setLayout(pnFormScoreLayout);
        pnFormScoreLayout.setHorizontalGroup(
            pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormScoreLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnFormScoreLayout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMSSVScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnFormScoreLayout.createSequentialGroup()
                        .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDiemScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNamHocScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormScoreLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbHocKyScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnFormScoreLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMHPScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(84, 84, 84)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClearScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddUpdateScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDeleteScore, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62))
        );
        pnFormScoreLayout.setVerticalGroup(
            pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormScoreLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMSSVScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMHPScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddUpdateScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbHocKyScore, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbNamHocScore, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(pnFormScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteScore, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiemScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnScoreLayout = new javax.swing.GroupLayout(pnScore);
        pnScore.setLayout(pnScoreLayout);
        pnScoreLayout.setHorizontalGroup(
            pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnScoreLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnTable2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnScoreLayout.createSequentialGroup()
                        .addComponent(txtSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnFormScore, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36))
            .addGroup(pnScoreLayout.createSequentialGroup()
                .addGap(410, 410, 410)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnScoreLayout.setVerticalGroup(
            pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnScoreLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(pnTable2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(pnFormScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayer(pnStudent, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnCourse, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(pnScore, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1082, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(pnCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jLayeredPane1Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(pnScore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(pnSideBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLayeredPane1)
                    .addComponent(pnSideBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScoreActionPerformed
        btnScore.setBackground(new Color(129, 97, 197));
        btnStudent.setBackground(new Color(4, 9, 33));
        btnCourse.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(false);
        pnCourse.setVisible(false);
        pnScore.setVisible(true);
    }//GEN-LAST:event_btnScoreActionPerformed

    private void btnStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentActionPerformed
        btnStudent.setBackground(new Color(129, 97, 197));
        btnCourse.setBackground(new Color(4, 9, 33));
        btnScore.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(true);
        pnCourse.setVisible(false);
        pnScore.setVisible(false);

    }//GEN-LAST:event_btnStudentActionPerformed

    private void btnCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCourseActionPerformed
        btnCourse.setBackground(new Color(129, 97, 197));
        btnStudent.setBackground(new Color(4, 9, 33));
        btnScore.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(false);
        pnCourse.setVisible(true);
        pnScore.setVisible(false);
    }//GEN-LAST:event_btnCourseActionPerformed

    private void btnAddUpdateStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUpdateStudentActionPerformed
        if (btnAddUpdateStudent.getText().equals("Thêm")) {
            addStudent();
        } else {
            editStudent();
        }

    }//GEN-LAST:event_btnAddUpdateStudentActionPerformed


    private void btnAddUpdateCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUpdateCourseActionPerformed
        if (btnAddUpdateCourse.getText().equals("Thêm")) {
            addCourse();
        } else {
            editCourse();
        }
    }//GEN-LAST:event_btnAddUpdateCourseActionPerformed

    private void btnAddUpdateScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUpdateScoreActionPerformed
        if (btnAddUpdateScore.getText().equals("Thêm")) {
            addScore();
        } else {
            editScore();
        }
    }//GEN-LAST:event_btnAddUpdateScoreActionPerformed

    private void btnClearStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearStudentActionPerformed
        clearFormStudent();
    }//GEN-LAST:event_btnClearStudentActionPerformed

    private void btnDeleteStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteStudentActionPerformed
        deleteStudent();
    }//GEN-LAST:event_btnDeleteStudentActionPerformed

    private void tbStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMouseClicked
        
    }//GEN-LAST:event_tbStudentMouseClicked

    private void tbStudentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMousePressed
        clickToSelectStudent();
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();
            clickToOpenScoreDetailForm();
        }
        
    }//GEN-LAST:event_tbStudentMousePressed


    private void btnSearchStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchStudentActionPerformed
        searchStudent();
    }//GEN-LAST:event_btnSearchStudentActionPerformed

    private void txtSearchStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchStudentActionPerformed
        searchStudent();
    }//GEN-LAST:event_txtSearchStudentActionPerformed

    private void btnClearCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearCourseActionPerformed
        clearFormCourse();
    }//GEN-LAST:event_btnClearCourseActionPerformed

    private void tbCourseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCourseMousePressed
        clickToSelectCourse();
    }//GEN-LAST:event_tbCourseMousePressed

    private void btnDeleteCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCourseActionPerformed
        deleteCourse();
    }//GEN-LAST:event_btnDeleteCourseActionPerformed

    private void txtSearchCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchCourseActionPerformed
        searchCourse();
    }//GEN-LAST:event_txtSearchCourseActionPerformed

    private void btnSearchCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchCourseActionPerformed
        searchCourse();
    }//GEN-LAST:event_btnSearchCourseActionPerformed

    private void btnClearScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearScoreActionPerformed
        clearFormScore();
    }//GEN-LAST:event_btnClearScoreActionPerformed

    private void btnDeleteScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteScoreActionPerformed
        deleteScore();
    }//GEN-LAST:event_btnDeleteScoreActionPerformed

    private void tbScoreMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbScoreMousePressed
        clickToSelectScore();
    }//GEN-LAST:event_tbScoreMousePressed

    private void btnSearchScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchScoreActionPerformed
        searchScore();
    }//GEN-LAST:event_btnSearchScoreActionPerformed

    private void txtSearchScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchScoreActionPerformed
        searchScore();
    }//GEN-LAST:event_txtSearchScoreActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DashboardForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnAddUpdateCourse;
    private javax.swing.JButton btnAddUpdateScore;
    private javax.swing.JButton btnAddUpdateStudent;
    private javax.swing.JButton btnClearCourse;
    private javax.swing.JButton btnClearScore;
    private javax.swing.JButton btnClearStudent;
    private javax.swing.JButton btnCourse;
    private javax.swing.JButton btnDeleteCourse;
    private javax.swing.JButton btnDeleteScore;
    private javax.swing.JButton btnDeleteStudent;
    private javax.swing.JButton btnScore;
    private javax.swing.JButton btnSearchCourse;
    private javax.swing.JButton btnSearchScore;
    private javax.swing.JButton btnSearchStudent;
    private javax.swing.JButton btnStudent;
    private javax.swing.JComboBox<String> cbHocKyScore;
    private javax.swing.JComboBox<String> cbNamHocScore;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbAvatar;
    private javax.swing.JLabel lbName;
    private javax.swing.JPanel pnCourse;
    private javax.swing.JPanel pnForm1;
    private javax.swing.JPanel pnFormScore;
    private javax.swing.JPanel pnFormStudent;
    private javax.swing.JPanel pnScore;
    private javax.swing.JPanel pnSideBar;
    private javax.swing.JPanel pnStudent;
    private javax.swing.JPanel pnTable;
    private javax.swing.JPanel pnTable1;
    private javax.swing.JPanel pnTable2;
    private javax.swing.JTable tbCourse;
    private javax.swing.JTable tbScore;
    private javax.swing.JTable tbStudent;
    private org.jdesktop.swingx.JXDatePicker txtDOBStudent;
    private javax.swing.JTextField txtDiemScore;
    private javax.swing.JComboBox<String> txtKhoaStudent;
    private javax.swing.JTextField txtLopStudent;
    private javax.swing.JTextField txtMHPCourse;
    private javax.swing.JTextField txtMHPScore;
    private javax.swing.JTextField txtMSSVScore;
    private javax.swing.JTextField txtMSSVStudent;
    private javax.swing.JComboBox<String> txtNganhStudent;
    private javax.swing.JTextField txtSearchCourse;
    private javax.swing.JTextField txtSearchScore;
    private javax.swing.JTextField txtSearchStudent;
    private javax.swing.JTextField txtTenHPCourse;
    private javax.swing.JTextField txtTenStudent;
    private javax.swing.JTextField txtTinChiCourse;
    // End of variables declaration//GEN-END:variables
}
