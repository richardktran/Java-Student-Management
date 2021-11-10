/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentmanagement;

import controllers.StudentController;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionListener;
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
import models.StudentModel;
import utils.DateTimeHelpers;

/**
 *
 * @author richa
 */
public class DashboardForm extends javax.swing.JFrame {

    Connection connection = null;
    DefaultTableModel studentTable;
    List<StudentModel> studentList = new ArrayList<>();
    private TableRowSorter<TableModel> rowSorterStudent;

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
    }

    /* HELPERS */
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
    
    
    /* HELPERS */

    

    
    /* STUDENT HANDLE */
    private void InitStudentState() {
        txtDOB.setFormats("dd-MM-yyyy");
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
        txtMSSV.setText("");
        txtName.setText("");
        txtClass.setText("");
        txtNganh.setSelectedIndex(0);
        txtKhoa.setSelectedIndex(0);
        txtDOB.setDate(null);
        tbStudent.clearSelection();
        btnAdd.setText("Thêm");
    }
    
    private void clickToSelectStudent() {
        int selectedIndex = tbStudent.getSelectedRow();
        if (selectedIndex >= 0) {
            StudentModel std = getStudentFromSelectedIndex(selectedIndex);
            txtMSSV.setText(std.getMssv());
            txtName.setText(std.getTen());
            txtClass.setText(std.getLop());
            txtDOB.setDate(std.getNgaySinh());
            int indexNganh = getIndexComboBoxFromString(txtNganh, std.getNganh());
            int indexKhoa = getIndexComboBoxFromString(txtKhoa, Integer.toString(std.getKhoa()));
            txtNganh.setSelectedIndex(indexNganh);
            txtKhoa.setSelectedIndex(indexKhoa);
        }
        btnAdd.setText("Cập nhật");
    }
    
    private void editStudent() {
        int selectedIndex = tbStudent.getSelectedRow();
        StudentModel std = getStudentFromSelectedIndex(selectedIndex);
        int id = std.getId();
        String mssv = txtMSSV.getText();
        String ten = txtName.getText();
        String lop = txtClass.getText();
        String nganh = txtNganh.getSelectedItem().toString();
        int khoa = Integer.parseInt(txtKhoa.getSelectedItem().toString());
        Date ngaySinh = txtDOB.getDate();
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
        String mssv = txtMSSV.getText();
        String ten = txtName.getText();
        String lop = txtClass.getText();
        String nganh = txtNganh.getSelectedItem().toString();
        int khoa = Integer.parseInt(txtKhoa.getSelectedItem().toString());
        Date ngaySinh = txtDOB.getDate();
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
        String text = txtSearch.getText();
        
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
    
    /* STUDENT HANDLE */
    
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
        btnHome = new javax.swing.JButton();
        btnStudent = new javax.swing.JButton();
        btnCourse = new javax.swing.JButton();
        btnScore = new javax.swing.JButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pnStudent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        pnTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbStudent = new javax.swing.JTable();
        pnForm = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtMSSV = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtClass = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtKhoa = new javax.swing.JComboBox<>();
        txtNganh = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtDOB = new org.jdesktop.swingx.JXDatePicker();
        btnAdd = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        pnCourse = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtSearchCourse = new javax.swing.JTextField();
        btnSearchCourse = new javax.swing.JButton();
        pnTable1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCourse = new javax.swing.JTable();
        pnForm1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtMHP = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtTenHP = new javax.swing.JTextField();
        txtTinChi = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnAddCourse = new javax.swing.JButton();
        btnClearCourse = new javax.swing.JButton();
        btnDeleteCourse = new javax.swing.JButton();
        pnScore = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtSearchScore = new javax.swing.JTextField();
        btnSearchScore = new javax.swing.JButton();
        pnTable2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbScore = new javax.swing.JTable();
        pnForm2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtMSSVScore = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtMHPScore = new javax.swing.JTextField();
        txtDiem = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btnAdd2 = new javax.swing.JButton();
        btnClear2 = new javax.swing.JButton();
        btnDelete2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bg.setBackground(new java.awt.Color(2, 3, 10));

        pnSideBar.setBackground(new java.awt.Color(4, 9, 33));

        lbAvatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/avatar.png"))); // NOI18N

        lbName.setFont(new java.awt.Font("Consolas", 1, 18)); // NOI18N
        lbName.setForeground(new java.awt.Color(255, 255, 255));
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setText("Trần Đăng Khoa");

        btnHome.setBackground(new java.awt.Color(129, 97, 197));
        btnHome.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnHome.setForeground(new java.awt.Color(255, 255, 255));
        btnHome.setText("Trang chủ");
        btnHome.setBorder(null);
        btnHome.setContentAreaFilled(false);
        btnHome.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHome.setFocusPainted(false);
        btnHome.setOpaque(true);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        btnStudent.setBackground(new java.awt.Color(4, 9, 33));
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
            .addComponent(btnHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnScore, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );

        pnStudent.setBackground(new java.awt.Color(2, 3, 10));

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Danh sách sinh viên");

        txtSearch.setBackground(new java.awt.Color(2, 3, 10));
        txtSearch.setFont(new java.awt.Font("Open Sans", 1, 12)); // NOI18N
        txtSearch.setForeground(new java.awt.Color(255, 255, 255));
        txtSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(76, 78, 88), java.awt.Color.gray));
        txtSearch.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnSearch.setBackground(new java.awt.Color(129, 97, 197));
        btnSearch.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearch.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch.setText("Tìm kiếm");
        btnSearch.setBorder(null);
        btnSearch.setContentAreaFilled(false);
        btnSearch.setFocusPainted(false);
        btnSearch.setOpaque(true);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
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
                true, false, false, false, false, true, true
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

        pnForm.setBackground(new java.awt.Color(255, 255, 255));

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

        txtKhoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "43", "44", "45", "46", "47" }));

        txtNganh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kỹ thuật phần mềm", "Công nghệ thông tin", "Khoa học máy tính", "Hệ thống thông tin", "Công nghệ thông tin CLC", "Mạng máy tính và truyền thông" }));
        txtNganh.setBorder(null);

        jLabel7.setBackground(new java.awt.Color(4, 9, 33));
        jLabel7.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 59, 142));
        jLabel7.setText("Ngày sinh:");

        btnAdd.setBackground(new java.awt.Color(0, 52, 123));
        btnAdd.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.setBorder(null);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(193, 125, 0));
        btnClear.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("Clear");
        btnClear.setBorder(null);
        btnClear.setContentAreaFilled(false);
        btnClear.setFocusPainted(false);
        btnClear.setOpaque(true);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(193, 20, 0));
        btnDelete.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xoá");
        btnDelete.setBorder(null);
        btnDelete.setContentAreaFilled(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnFormLayout = new javax.swing.GroupLayout(pnForm);
        pnForm.setLayout(pnFormLayout);
        pnFormLayout.setHorizontalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMSSV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDOB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNganh, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(36, 36, 36)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(59, 59, 59)
                        .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFormLayout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(100, 100, 100))
        );
        pnFormLayout.setVerticalGroup(
            pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMSSV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtClass, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNganh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(pnFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(pnForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnStudentLayout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(pnTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnForm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        btnSearchCourse.setBackground(new java.awt.Color(129, 97, 197));
        btnSearchCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearchCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchCourse.setText("Tìm kiếm");
        btnSearchCourse.setBorder(null);
        btnSearchCourse.setContentAreaFilled(false);
        btnSearchCourse.setFocusPainted(false);
        btnSearchCourse.setOpaque(true);

        pnTable1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        tbCourse.setAutoCreateRowSorter(true);
        tbCourse.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã học phần", "Tên học phần", "Số tín chỉ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbCourse.setGridColor(new java.awt.Color(2, 3, 10));
        tbCourse.setOpaque(false);
        jScrollPane2.setViewportView(tbCourse);

        javax.swing.GroupLayout pnTable1Layout = new javax.swing.GroupLayout(pnTable1);
        pnTable1.setLayout(pnTable1Layout);
        pnTable1Layout.setHorizontalGroup(
            pnTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
        );
        pnTable1Layout.setVerticalGroup(
            pnTable1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTable1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
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

        btnAddCourse.setBackground(new java.awt.Color(0, 52, 123));
        btnAddCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAddCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnAddCourse.setText("Thêm");
        btnAddCourse.setBorder(null);
        btnAddCourse.setContentAreaFilled(false);
        btnAddCourse.setFocusPainted(false);
        btnAddCourse.setOpaque(true);
        btnAddCourse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCourseActionPerformed(evt);
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

        btnDeleteCourse.setBackground(new java.awt.Color(193, 20, 0));
        btnDeleteCourse.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDeleteCourse.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteCourse.setText("Xoá");
        btnDeleteCourse.setBorder(null);
        btnDeleteCourse.setContentAreaFilled(false);
        btnDeleteCourse.setFocusPainted(false);
        btnDeleteCourse.setOpaque(true);

        javax.swing.GroupLayout pnForm1Layout = new javax.swing.GroupLayout(pnForm1);
        pnForm1.setLayout(pnForm1Layout);
        pnForm1Layout.setHorizontalGroup(
            pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnForm1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnForm1Layout.createSequentialGroup()
                        .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addComponent(txtTinChi, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtTenHP, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnForm1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(167, 167, 167)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClearCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddCourse, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnDeleteCourse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnForm1Layout.setVerticalGroup(
            pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnForm1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMHP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTenHP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddCourse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(pnForm1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTinChi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        btnSearchScore.setBackground(new java.awt.Color(129, 97, 197));
        btnSearchScore.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnSearchScore.setForeground(new java.awt.Color(255, 255, 255));
        btnSearchScore.setText("Tìm kiếm");
        btnSearchScore.setBorder(null);
        btnSearchScore.setContentAreaFilled(false);
        btnSearchScore.setFocusPainted(false);
        btnSearchScore.setOpaque(true);

        pnTable2.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(null);

        tbScore.setAutoCreateRowSorter(true);
        tbScore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "MSSV", "Tên sinh viên", "Tên học phần", "Điểm số", "Điểm chữ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbScore.setGridColor(new java.awt.Color(2, 3, 10));
        tbScore.setOpaque(false);
        jScrollPane3.setViewportView(tbScore);

        javax.swing.GroupLayout pnTable2Layout = new javax.swing.GroupLayout(pnTable2);
        pnTable2.setLayout(pnTable2Layout);
        pnTable2Layout.setHorizontalGroup(
            pnTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
        );
        pnTable2Layout.setVerticalGroup(
            pnTable2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTable2Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        pnForm2.setBackground(new java.awt.Color(255, 255, 255));

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

        btnAdd2.setBackground(new java.awt.Color(0, 52, 123));
        btnAdd2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnAdd2.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd2.setText("Thêm");
        btnAdd2.setBorder(null);
        btnAdd2.setContentAreaFilled(false);
        btnAdd2.setFocusPainted(false);
        btnAdd2.setOpaque(true);
        btnAdd2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd2ActionPerformed(evt);
            }
        });

        btnClear2.setBackground(new java.awt.Color(193, 125, 0));
        btnClear2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnClear2.setForeground(new java.awt.Color(255, 255, 255));
        btnClear2.setText("Clear");
        btnClear2.setBorder(null);
        btnClear2.setContentAreaFilled(false);
        btnClear2.setFocusPainted(false);
        btnClear2.setOpaque(true);

        btnDelete2.setBackground(new java.awt.Color(193, 20, 0));
        btnDelete2.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnDelete2.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete2.setText("Xoá");
        btnDelete2.setBorder(null);
        btnDelete2.setContentAreaFilled(false);
        btnDelete2.setFocusPainted(false);
        btnDelete2.setOpaque(true);

        javax.swing.GroupLayout pnForm2Layout = new javax.swing.GroupLayout(pnForm2);
        pnForm2.setLayout(pnForm2Layout);
        pnForm2Layout.setHorizontalGroup(
            pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnForm2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnForm2Layout.createSequentialGroup()
                        .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtMHPScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnForm2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtMSSVScore, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(167, 167, 167)
                .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnClear2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd2, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addComponent(btnDelete2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnForm2Layout.setVerticalGroup(
            pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnForm2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMSSVScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMHPScore, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(pnForm2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnScoreLayout = new javax.swing.GroupLayout(pnScore);
        pnScore.setLayout(pnScoreLayout);
        pnScoreLayout.setHorizontalGroup(
            pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnScoreLayout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(pnScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnTable2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnScoreLayout.createSequentialGroup()
                        .addComponent(txtSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearchScore, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnForm2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(pnForm2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
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
        btnHome.setBackground(new Color(4, 9, 33));
        btnStudent.setBackground(new Color(4, 9, 33));
        btnCourse.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(false);
        pnCourse.setVisible(false);
        pnScore.setVisible(true);
    }//GEN-LAST:event_btnScoreActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        btnHome.setBackground(new Color(129, 97, 197));
        btnStudent.setBackground(new Color(4, 9, 33));
        btnCourse.setBackground(new Color(4, 9, 33));
        btnScore.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(true);
        pnCourse.setVisible(false);
        pnScore.setVisible(false);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentActionPerformed
        btnStudent.setBackground(new Color(129, 97, 197));
        btnHome.setBackground(new Color(4, 9, 33));
        btnCourse.setBackground(new Color(4, 9, 33));
        btnScore.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(true);
        pnCourse.setVisible(false);
        pnScore.setVisible(false);

    }//GEN-LAST:event_btnStudentActionPerformed

    private void btnCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCourseActionPerformed
        btnCourse.setBackground(new Color(129, 97, 197));
        btnHome.setBackground(new Color(4, 9, 33));
        btnStudent.setBackground(new Color(4, 9, 33));
        btnScore.setBackground(new Color(4, 9, 33));

        pnStudent.setVisible(false);
        pnCourse.setVisible(true);
        pnScore.setVisible(false);
    }//GEN-LAST:event_btnCourseActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (btnAdd.getText().equals("Thêm")) {
            addStudent();
        } else {
            editStudent();
        }

    }//GEN-LAST:event_btnAddActionPerformed

    

    private void btnAddCourseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCourseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddCourseActionPerformed

    private void btnAdd2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdd2ActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFormStudent();
        
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteStudent();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tbStudentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMouseClicked

    }//GEN-LAST:event_tbStudentMouseClicked

    private void tbStudentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbStudentMousePressed
        clickToSelectStudent();
    }//GEN-LAST:event_tbStudentMousePressed

    

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchStudent();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        searchStudent();
    }//GEN-LAST:event_txtSearchActionPerformed

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
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAdd2;
    private javax.swing.JButton btnAddCourse;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClear2;
    private javax.swing.JButton btnClearCourse;
    private javax.swing.JButton btnCourse;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDelete2;
    private javax.swing.JButton btnDeleteCourse;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnScore;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchCourse;
    private javax.swing.JButton btnSearchScore;
    private javax.swing.JButton btnStudent;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JPanel pnForm;
    private javax.swing.JPanel pnForm1;
    private javax.swing.JPanel pnForm2;
    private javax.swing.JPanel pnScore;
    private javax.swing.JPanel pnSideBar;
    private javax.swing.JPanel pnStudent;
    private javax.swing.JPanel pnTable;
    private javax.swing.JPanel pnTable1;
    private javax.swing.JPanel pnTable2;
    private javax.swing.JTable tbCourse;
    private javax.swing.JTable tbScore;
    private javax.swing.JTable tbStudent;
    private javax.swing.JTextField txtClass;
    private org.jdesktop.swingx.JXDatePicker txtDOB;
    private javax.swing.JTextField txtDiem;
    private javax.swing.JComboBox<String> txtKhoa;
    private javax.swing.JTextField txtMHP;
    private javax.swing.JTextField txtMHPScore;
    private javax.swing.JTextField txtMSSV;
    private javax.swing.JTextField txtMSSVScore;
    private javax.swing.JTextField txtName;
    private javax.swing.JComboBox<String> txtNganh;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSearchCourse;
    private javax.swing.JTextField txtSearchScore;
    private javax.swing.JTextField txtTenHP;
    private javax.swing.JTextField txtTinChi;
    // End of variables declaration//GEN-END:variables
}
