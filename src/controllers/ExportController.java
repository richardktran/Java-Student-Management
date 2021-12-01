/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import models.ScoreModel;
import models.StudentModel;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author richa
 */
public class ExportController {
    private static HSSFCellStyle createStyleForTitle(HSSFWorkbook workbook) {
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
    private static HSSFCellStyle createBorder(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }
    public static void exportAll(StudentModel student, List<Pair<String, String>>  namhocList) throws FileNotFoundException, IOException{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Bang Diem");
        
        
        int rownum = 0;
        writeTitle(workbook, sheet,rownum);
        rownum+=2;
        writeStudentInfo(workbook, sheet,rownum, student);
        rownum+=3;
        
        for (Pair<String, String> namhoc : namhocList) {
            String nh = namhoc.getKey();
            String hk = namhoc.getValue();
            writeHocKy(hk, nh, workbook, sheet, rownum);
            rownum++;
            writeHeader(workbook, sheet, rownum);
            List<ScoreModel> scoreList = ScoreController.findScoreStudent(student, nh,hk);
            rownum = writeData(scoreList, workbook, rownum, sheet);
            rownum++;
        }
        
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);
        createOutputFile(workbook, "Bangdiem_"+student.getMssv()+".xls");
    }
    
    private static void writeStudentInfo(HSSFWorkbook workbook, HSSFSheet sheet, int rowIndex, StudentModel student){
        Row row;
        Cell cell;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Họ và tên: ");
        cell.setCellStyle(style);
        
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(student.getTen());
        
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Lớp: ");
        cell.setCellStyle(style);
        
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(student.getLop());
        
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("MSSV: ");
        cell.setCellStyle(style);
        
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue(student.getMssv());
        
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Khoá: ");
        cell.setCellStyle(style);
        
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue(student.getKhoa());
    }
    
    private static void writeTitle(HSSFWorkbook workbook, HSSFSheet sheet, int rowIndex){
        Row row;
        Cell cell;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellStyle(style);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(style);
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(style);
        cell = row.createCell(3, CellType.STRING);
        cell.setCellStyle(style);
        cell = row.createCell(4, CellType.STRING);
        cell.setCellStyle(style);
        cell = row.createCell(5, CellType.STRING);
        cell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,5));
        cell.setCellValue("BẢNG KẾT QUẢ HỌC TẬP CỦA SINH VIÊN");
        cell.setCellStyle(style);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }
    
    private static void writeHeader(HSSFWorkbook workbook, HSSFSheet sheet, int rownum) {
        Row row;
        Cell cell;
        HSSFCellStyle style = createStyleForTitle(workbook);
        HSSFCellStyle border = createBorder(workbook);
        row = sheet.createRow(rownum);
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Mã học phần ");
        cell.setCellStyle(style);
        
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Tên học phần ");
        cell.setCellStyle(style);
        
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Tín chỉ ");
        cell.setCellStyle(style);
        
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Điểm hệ 10 ");
        cell.setCellStyle(style);
        
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Điểm hệ 4 ");
        cell.setCellStyle(style);
        
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Điểm chữ ");
        cell.setCellStyle(style);
    }

    private static int writeData(List<ScoreModel> scoreList,HSSFWorkbook workbook, int rownum, HSSFSheet sheet) {
        Row row;
        Cell cell;
        
        for(ScoreModel score : scoreList){
            rownum++;
            row=sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue(score.getMonhoc().getMaHP());
            
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(score.getMonhoc().getTenHP());
            
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue(score.getMonhoc().getTinChi());
            
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(score.getDiem());
            
            cell = row.createCell(4, CellType.STRING);
            cell.setCellValue(score.getDiemHe4());
            
            cell = row.createCell(5, CellType.STRING);
            cell.setCellValue(score.getDiemChu());
        }
        return ++rownum;
    }
    
    private static void writeHocKy(String hocky, String namhoc, HSSFWorkbook workbook, HSSFSheet sheet, int rownum) {
        Row row;
        Cell cell;
        HSSFCellStyle style = createStyleForTitle(workbook);
        row = sheet.createRow(rownum);
        cell = row.createCell(0, CellType.STRING);
        sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,0,5));
        cell.setCellStyle(style);
        cell.setCellValue("Học kỳ "+hocky+" - Năm học "+namhoc);
        CellStyle cellStyle = cell.getCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle);
    }

    
    
    private static void createOutputFile(HSSFWorkbook workbook, String filename) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save the file"); //name for chooser
        FileFilter filter = new FileNameExtensionFilter("XLS File", ".xls"); //filter to show only that
        fileChooser.setAcceptAllFileFilterUsed(false); //to show or not all other files
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setSelectedFile(new File(filename)); //when you want to show the name of file into the chooser
        fileChooser.setVisible(true);
        int result = fileChooser.showSaveDialog(fileChooser);
        if (result == JFileChooser.APPROVE_OPTION) {
            filename = fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return;
        }

        File file = new File(filename);
        if (file.exists() == false) {

            try (
                FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        } else {
            try (
                FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        }
    }
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }

    

}
