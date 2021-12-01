/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentmanagement;

import controllers.ExportController;
import controllers.ScoreController;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import models.CurrentStudent;
import models.ScoreModel;
import models.StudentModel;

/**
 *
 * @author richa
 */
public class ScoreDetailFrm extends javax.swing.JFrame {
    Connection connection = null;
    StudentModel currentStudent = null;
    String namhoc, hocky;
    DefaultTableModel scoreTable;
    List<ScoreModel> scoreList = new ArrayList<>();
    private TableRowSorter<TableModel> rowSorterScore;
    /**
     * Creates new form ScoreDetailFrm
     */
    public ScoreDetailFrm() {
        initComponents();
        initState();
    }
    
    private void initState() {
        DBConnection.connectDB(this);
        connection = DBConnection.connection;
        scoreTable = (DefaultTableModel) tbScore.getModel();
        //Tim kiem
        rowSorterScore = new TableRowSorter<>(tbScore.getModel());
        tbScore.setRowSorter(rowSorterScore);
        currentStudent = CurrentStudent.getStudent();
        String hoten = currentStudent.getTen();
        String mssv = currentStudent.getMssv();
        lbHoTen.setText(hoten+" - "+mssv);
        
        namhoc= cbNamHoc.getSelectedItem().toString();
        hocky = cbHocKy.getSelectedItem().toString();
        showScore();
    }
    
    private void getNamhoc(){
        namhoc= cbNamHoc.getSelectedItem().toString();
    }
    private void getHocKy(){
        hocky = cbHocKy.getSelectedItem().toString();
    }
    
    private void showScore(){
        double total = 0;
        int tinchi = 0;
        getNamhoc();
        getHocKy();
        scoreList = ScoreController.findScoreStudent(currentStudent, namhoc, hocky);
        scoreTable.setRowCount(0);
        scoreList.forEach((score) -> {
            scoreTable.addRow(new Object[]{
                scoreTable.getRowCount() + 1,
                score.getMonhoc().getMaHP(),
                score.getMonhoc().getTenHP(),
                score.getMonhoc().getTinChi(),
                score.getDiem(),
                score.getDiemHe4(),
                score.getDiemChu(),
            });
        });
        
        for(int i=0; i<scoreList.size(); i++){
            ScoreModel score = scoreList.get(i);
            total = total + (score.getDiemHe4()*score.getMonhoc().getTinChi());
            tinchi = tinchi + score.getMonhoc().getTinChi();
        }
        
        double trungbinh = total/(double)tinchi;
        trungbinh = (double) Math.round(trungbinh * 100) / 100;
        lbTrungbinh.setText(Double.toString(trungbinh));
        lbTongTinChi.setText(Integer.toString(tinchi));
        
    }
    
    private void xuatBangDiemHocKy(){
        try {
            List<Pair<String, String>> namhocList = new ArrayList<Pair<String, String>>();
            
            List<String> hockyList = new ArrayList<String>();
            hockyList.add(hocky);

            List<String> namList = new ArrayList<String>();
            namList.add(namhoc);
            
            for (int i = 0; i < namList.size(); i++) {
                
                for (int j = 0; j < hockyList.size(); j++) {
                    String nh = namList.get(i);
                    String hk = hockyList.get(j);
                    Pair<String, String> pair = new Pair<>(nh, hk);
                    namhocList.add(pair);
                }
                
            }
            
            // Lấy từ cơ sở dữ liệu
            ExportController.exportAll(currentStudent, namhocList);
        } catch (IOException ex) {
            Logger.getLogger(ScoreDetailFrm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void xuatBangDiemFull(){
        try {
            
            List<Pair<String, String>> namhocList = new ArrayList<Pair<String, String>>();
            
            List<String> hockyList = new ArrayList<String>();
            hockyList.add("1");
            hockyList.add("2");
            hockyList.add("Hè");

            List<String> namList = new ArrayList<String>();
            namList.add("2020-2021");
            namList.add("2021-2022");
            namList.add("2022-2023");
            namList.add("2023-2024");
            
            
            for (int i = 0; i < namList.size(); i++) {
                
                for (int j = 0; j < hockyList.size(); j++) {
                    String nh = namList.get(i);
                    String hk = hockyList.get(j);
                    Pair<String, String> pair = new Pair<>(nh, hk);
                    namhocList.add(pair);
                }
                
            }
            
            
            // Lấy từ cơ sở dữ liệu
            ExportController.exportAll(currentStudent, namhocList);
        } catch (IOException ex) {
            Logger.getLogger(ScoreDetailFrm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbNamHoc = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbHocKy = new javax.swing.JComboBox<>();
        btnLietKe = new javax.swing.JButton();
        lbHoTen = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbHKNH = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbScore = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lbTrungbinh = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lbTongTinChi = new javax.swing.JLabel();
        btnExportAll = new javax.swing.JButton();
        btnExportOne = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        bg.setBackground(new java.awt.Color(2, 3, 10));

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel3.setText("Năm học:");

        cbNamHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2020-2021", "2021-2022", "2022-2023", "2023-2024" }));
        cbNamHoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNamHocActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel4.setText("Học kỳ:");

        cbHocKy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "Hè" }));

        btnLietKe.setBackground(new java.awt.Color(0, 52, 123));
        btnLietKe.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnLietKe.setForeground(new java.awt.Color(255, 255, 255));
        btnLietKe.setText("Liệt kê");
        btnLietKe.setBorder(null);
        btnLietKe.setContentAreaFilled(false);
        btnLietKe.setFocusPainted(false);
        btnLietKe.setOpaque(true);
        btnLietKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLietKeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cbNamHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbHocKy, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(btnLietKe, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbHocKy, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLietKe, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbNamHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbHoTen.setFont(new java.awt.Font("Open Sans", 1, 24)); // NOI18N
        lbHoTen.setForeground(new java.awt.Color(255, 255, 255));
        lbHoTen.setText("TRẦN ĐĂNG KHOA - B1805879");

        jLabel1.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Xem điểm học kỳ");

        lbHKNH.setFont(new java.awt.Font("Open Sans", 1, 18)); // NOI18N
        lbHKNH.setForeground(new java.awt.Color(255, 255, 255));
        lbHKNH.setText("Xem điểm Học Kỳ 1 Năm Học 2021-2022");

        tbScore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã học phần", "Tên học phần", "Tín chỉ", "Điểm (Hệ 10)", "Điểm (Hệ 4)", "Điểm chữ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbScore.setRowHeight(25);
        jScrollPane1.setViewportView(tbScore);

        lbTrungbinh.setBackground(new java.awt.Color(0, 0, 0));
        lbTrungbinh.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lbTrungbinh.setText("4.0");

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel6.setText("Điểm trung bình học kỳ: ");

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        jLabel7.setText("Số tín chỉ của học kỳ:");

        lbTongTinChi.setBackground(new java.awt.Color(0, 0, 0));
        lbTongTinChi.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        lbTongTinChi.setText("18");

        btnExportAll.setBackground(new java.awt.Color(129, 97, 197));
        btnExportAll.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnExportAll.setForeground(new java.awt.Color(255, 255, 255));
        btnExportAll.setText("In bảng điểm tất cả");
        btnExportAll.setBorder(null);
        btnExportAll.setContentAreaFilled(false);
        btnExportAll.setFocusPainted(false);
        btnExportAll.setOpaque(true);
        btnExportAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportAllActionPerformed(evt);
            }
        });

        btnExportOne.setBackground(new java.awt.Color(129, 97, 197));
        btnExportOne.setFont(new java.awt.Font("Open Sans", 1, 14)); // NOI18N
        btnExportOne.setForeground(new java.awt.Color(255, 255, 255));
        btnExportOne.setText("In bảng điểm học kỳ này");
        btnExportOne.setBorder(null);
        btnExportOne.setContentAreaFilled(false);
        btnExportOne.setFocusPainted(false);
        btnExportOne.setOpaque(true);
        btnExportOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportOneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTrungbinh, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExportOne, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbTongTinChi, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnExportAll, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(154, 154, 154))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTrungbinh, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportOne, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongTinChi, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportAll, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(264, 264, 264)
                        .addComponent(lbHKNH, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(373, 373, 373)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bgLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(55, Short.MAX_VALUE))
            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bgLayout.createSequentialGroup()
                    .addContainerGap(291, Short.MAX_VALUE)
                    .addComponent(lbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(241, 241, 241)))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbHKNH, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(bgLayout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(lbHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(578, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLietKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLietKeActionPerformed
        showScore();
    }//GEN-LAST:event_btnLietKeActionPerformed

    private void cbNamHocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNamHocActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbNamHocActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void btnExportAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportAllActionPerformed
        xuatBangDiemFull();
    }//GEN-LAST:event_btnExportAllActionPerformed

    private void btnExportOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportOneActionPerformed
        xuatBangDiemHocKy();
    }//GEN-LAST:event_btnExportOneActionPerformed

    /**
     * @param args the command 
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
            java.util.logging.Logger.getLogger(ScoreDetailFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScoreDetailFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScoreDetailFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScoreDetailFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScoreDetailFrm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton btnExportAll;
    private javax.swing.JButton btnExportOne;
    private javax.swing.JButton btnLietKe;
    private javax.swing.JComboBox<String> cbHocKy;
    private javax.swing.JComboBox<String> cbNamHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbHKNH;
    private javax.swing.JLabel lbHoTen;
    private javax.swing.JLabel lbTongTinChi;
    private javax.swing.JLabel lbTrungbinh;
    private javax.swing.JTable tbScore;
    // End of variables declaration//GEN-END:variables
}
