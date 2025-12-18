package com.tnh.baseware.core.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportFileUtils {
    private static final String TEMPLATE_FOLDER = "templates";
    private static final String TEMPLATE_FILE_NAME = "school_dashboard_template.xlsx";

    public static void main(String[] args) {
        ReportFileUtils utils = new ReportFileUtils();
        try {
            // Tạo và lưu template
            utils.createAndSaveTemplate();
            System.out.println("Template đã được tạo thành công tại: " +
                    TEMPLATE_FOLDER + "/" + TEMPLATE_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Lỗi khi tạo template: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createAndSaveTemplate() throws IOException {
        // Tạo folder templates nếu chưa tồn tại
        Path templateDir = Paths.get(TEMPLATE_FOLDER);
        if (!Files.exists(templateDir)) {
            Files.createDirectories(templateDir);
            System.out.println("Đã tạo folder: " + TEMPLATE_FOLDER);
        }

        // Tạo template
        Workbook workbook = createSchoolDashboardTemplate();

        // Lưu template vào file
        String templatePath = TEMPLATE_FOLDER + "/" + TEMPLATE_FILE_NAME;
        try (FileOutputStream outputStream = new FileOutputStream(templatePath)) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Template đã được lưu tại: " + templatePath);
        }
    }

    public Workbook loadTemplateFromFile() throws IOException {
        String templatePath = TEMPLATE_FOLDER + "/" + TEMPLATE_FILE_NAME;
        Path path = Paths.get(templatePath);

        if (!Files.exists(path)) {
            System.out.println("Template không tồn tại, tạo template mới...");
            createAndSaveTemplate();
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            return new XSSFWorkbook(inputStream);
        }
    }

    public Workbook createSchoolDashboardTemplate() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("School Dashboard Data");

        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // Add title and metadata
        createTitleAndMetadata(sheet, workbook);

        // Create header row (A4 - L4) - theo thuộc tính SchoolDashboard
        Row headerRow = sheet.createRow(3);
        String[] headers = {
                "STT", "TRƯỜNG", "KHU VỰC", "SỐ CAMERA", "SÔ HỌC SINH",
                "NGƯỜI DÙNG(HỆ THỐNG)", "NĂM ĐĂNG KÝ", "SỐ ĐĂNG KÝ", "CHƯA THANH TOÁN",
                "ĐÃ THANH TOÁN", "TỔNG SỐ TIỀN"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create template data rows with placeholders (row 4)
        Row dataRow = sheet.createRow(4);
        String[] placeholders = {
                "{STT}", "{schoolName}", "{address}", "{cameras}", "{students}",
                "{userAssignZone}", "{registrationYear}", "{registers}", "{notPaid}",
                "{paid}", "{totalAmount}", "{createdDate}"
        };

        for (int i = 0; i < placeholders.length; i++) {
            Cell cell = dataRow.createCell(i);
            cell.setCellValue(placeholders[i]);
            cell.setCellStyle(dataStyle);
        }

        // Set column widths theo từng cột
        sheet.setColumnWidth(0, 8000); // ID
        sheet.setColumnWidth(1, 6000); // School Name
        sheet.setColumnWidth(2, 8000); // Address
        sheet.setColumnWidth(3, 3000); // Cameras
        sheet.setColumnWidth(4, 3000); // Students
        sheet.setColumnWidth(5, 4500); // Users Assign Zone
        sheet.setColumnWidth(6, 4000); // Registration Year
        sheet.setColumnWidth(7, 3000); // Registers
        sheet.setColumnWidth(8, 3000); // Not Paid
        sheet.setColumnWidth(9, 3000); // Paid
        sheet.setColumnWidth(10, 4000); // Total Amount
        sheet.setColumnWidth(11, 4500); // Created Date

        return workbook;
    }

    private void createTitleAndMetadata(Sheet sheet, Workbook workbook) {
        // Create title style
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);

        // Title row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BÁO CÁO THỐNG KÊ HOẠT ĐỘNG CAMERA TRƯỜNG HỌC");
        titleCell.setCellStyle(titleStyle);

        // Metadata rows
        Row metaRow1 = sheet.createRow(1);
        metaRow1.createCell(0).setCellValue("Ngày đồng bộ dữ liệu:");
        metaRow1.createCell(1)
                .setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        Row metaRow2 = sheet.createRow(2);
        metaRow2.createCell(0).setCellValue("Phiên bản mẫu:");
        metaRow2.createCell(1).setCellValue("1.0");
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
