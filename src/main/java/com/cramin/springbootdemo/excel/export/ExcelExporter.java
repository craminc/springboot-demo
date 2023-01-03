package com.cramin.springbootdemo.excel.export;

import com.cramin.springbootdemo.excel.export.annotation.ExcelField;
import com.cramin.springbootdemo.excel.export.annotation.ExcelModel;
import com.cramin.springbootdemo.excel.export.enums.SensitiveStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExcelExporter<T> implements Closeable {

    private final static int HEAD_ROW_NUM = 0;
    private final static int DATA_ROW_NUM_START = 1;

    private final static String PREFIX_GET = "get";
    private final static String DEFAULT_COLUMN_NAME = "列";
    private final List<Pair<ExcelField, Field>> excelFields = new ArrayList<>();

    private final Class<T> clz;

    private String excelTitle = "default";

    private String[] sheetNames = {"sheet0"};

    private HSSFWorkbook wb;


    public ExcelExporter(Class<T> clz) {
        ExcelModel excelModel = clz.getAnnotation(ExcelModel.class);
        if (excelModel == null)
            log.info("required annotation {} missed on class {}", ExcelModel.class.getName(), clz.getName());
        else {
            if (ObjectUtils.isNotEmpty(excelModel.title()))
                this.excelTitle = excelModel.title();
            if (ObjectUtils.isNotEmpty(excelModel.titleSuffix()))
                this.excelTitle += "_" + excelModel.titleSuffix().generator().get();
            if (ObjectUtils.isNotEmpty(excelModel.sheetNames()))
                this.sheetNames = excelModel.sheetNames();
        }
        this.clz = clz;
        this.initExcel();
    }

    public ExcelExporter(Class<T> clz, String excelTitle, String[] sheetNames) {
        if (ObjectUtils.isNotEmpty(excelTitle))
            this.excelTitle = excelTitle;
        if (ObjectUtils.isNotEmpty(sheetNames))
            this.sheetNames = sheetNames;
        this.clz = clz;
        this.initExcel();
    }

    private void initExcel() {
        Field[] fields = this.clz.getDeclaredFields();
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField != null)
                this.excelFields.add(Pair.of(excelField, field));
        }
        this.excelFields.sort((Comparator.comparing(t -> t.getKey().order())));

        HSSFWorkbook workbook = new HSSFWorkbook();
        for (String sheetName : this.sheetNames) {
            HSSFSheet sheet = workbook.createSheet(sheetName);

            // head row
            HSSFRow headRow = sheet.createRow(HEAD_ROW_NUM);
            // row height px
            headRow.setHeightInPoints(21);
            HSSFCellStyle headRowStyle = this.headRowStyle(workbook);

            int column = 0;
            for (Pair<ExcelField, Field> excelFieldPair : this.excelFields) {
                ExcelField excelField = excelFieldPair.getKey();
                String title = ObjectUtils.isEmpty(excelField.title()) ?
                        DEFAULT_COLUMN_NAME + column : excelField.title();
                HSSFCell cell = headRow.createCell(column);
                sheet.setColumnWidth(column, excelField.cellWidth() * 256);
                cell.setCellStyle(headRowStyle);
                cell.setCellValue(title);
                column++;
            }
        }
        this.wb = workbook;
    }

    public void setExcelData(List<T> excelData) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (String sheetName : this.sheetNames) {
            this.setExcelData(excelData, sheetName);
        }
    }

    public void setExcelData(Map<String, List<T>> groupExcelData) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Map.Entry<String, List<T>> entry : groupExcelData.entrySet()) {
            this.setExcelData(entry.getValue(), entry.getKey());
        }
    }

    public void setExcelData(List<T> excelData, String sheetName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HSSFSheet sheet = this.wb.getSheet(sheetName);
        if (sheet == null) return;
        int rowNum = DATA_ROW_NUM_START;
        for (T excelDatum : excelData) {
            HSSFRow row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Pair<ExcelField, Field> excelFieldPair : this.excelFields) {
                ExcelField excelField = excelFieldPair.getKey();
                Field field = excelFieldPair.getValue();
                // cell
                CellType cellType = excelField.fieldType();
                HSSFCell cell = row.createCell(colNum++, cellType);
                String methodName = PREFIX_GET + StringUtils.capitalize(field.getName());
                Method method = this.clz.getMethod(methodName);
                Object value = method.invoke(excelDatum);
                // field value mapping
                if (ObjectUtils.isNotEmpty(excelField.valMap())) {
                    Map<String, String> map = Arrays.stream(excelField.valMap().split(","))
                            .map(t -> t.split(":"))
                            .collect(Collectors.toMap(t -> t[0], t -> t[1]));
                    value = map.get(value.toString());
                }
                // set excel data
                if (value instanceof Date) {
                    cell.setCellValue(DateFormatUtils.format((Date) value, "yyyy-MM-dd HH:mm:ss"));
                } else if (value instanceof Number) {
                    cell.setCellValue(Double.parseDouble(value.toString()));
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else {
                    SensitiveStrategy strategy = excelField.strategy();
                    cell.setCellValue(strategy.desensitizer().apply(value.toString()));
                }
            }
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        try (OutputStream os = response.getOutputStream()) {
            String fileName = URLEncoder.encode(excelTitle, "UTF-8") + ".xls";
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            this.wb.write(os);
        }
    }

    @Override
    public void close() throws IOException {
        this.wb.close();
    }

    public HSSFCellStyle headRowStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headRowStyle = workbook.createCellStyle();
        // align center
        headRowStyle.setAlignment(HorizontalAlignment.CENTER);
        // vertical center
        headRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // background color
        headRowStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        headRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // border
        headRowStyle.setBorderTop(BorderStyle.THIN);
        headRowStyle.setBorderRight(BorderStyle.THIN);
        headRowStyle.setBorderBottom(BorderStyle.THIN);
        headRowStyle.setBorderLeft(BorderStyle.THIN);
        // font
        HSSFFont fontStyle = workbook.createFont();
        fontStyle.setBold(true);
        fontStyle.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headRowStyle.setFont(fontStyle);

        return headRowStyle;
    }
}
