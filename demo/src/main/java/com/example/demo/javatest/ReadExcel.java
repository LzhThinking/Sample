package com.example.demo.javatest;

import android.text.TextUtils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 需要引入poi-3.9.jar和poi-ooxml-3.17.jar两个jar包
 */
public class ReadExcel {
    public static void main(String [] args) {
        readExcel("C:\\project\\AqaraHome文案整理.xlsx");
    }

    public static void readExcel(String file) {
        File xlsFile = new File(file);
        /**
         * 这里根据不同的excel类型
         * 可以选取不同的处理类：
         *          1.XSSFWorkbook
         *          2.HSSFWorkbook
         */
        // 获得工作簿
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(xlsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int sheetCount = workbook.getNumberOfSheets();
//        for (int i = 0; i < sheetCount; i++) {
            // 获得工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getPhysicalNumberOfRows();

            for (int j = 0; j < rows; j++) {
                // 获取第i行数据
                XSSFRow sheetRow = sheet.getRow(j);
                if (sheetRow == null) {
                    continue;
                }
                // 获取第0格数据
                XSSFCell key = sheetRow.getCell(0);
                XSSFCell cn = sheetRow.getCell(13);
//                XSSFCell cn = sheetRow.getCell(5);
                // 调用toString方法获取内容
//                <string name="Test_Hello">Test</string>
                String cnValue = cn.toString().trim();
                String keyStr = key.toString().toLowerCase().trim();
                keyStr = keyStr.replace(".", "_");
                keyStr = keyStr.replace(" ", "_");
//                keyStr.replace('.', '_');
                String cnStr = cn.toString();
                if (cnValue != null && cnValue.length() != 0) {
                    System.out.println("<string name=\"" + keyStr + "\">" + cnStr + "</string>");
                }
            }
//        }

    }

    public static void writeExcel() {
        // 创建一个新的工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个sheet页
        HSSFSheet sheet1 = workbook.createSheet("sheet1");
        // 创建一行
        HSSFRow row = sheet1.createRow(0);
        // 设置行高
        row.setHeightInPoints(30);
        // 创建并设置样式
        HSSFCreationHelper creationHelper = workbook.getCreationHelper();
        // 创建字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short)24);
        font.setFontName("courier New");// 字体名称
        font.setItalic(true);// 倾斜
        font.setStrikeout(true);
        // 创建样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 设置字体
        cellStyle.setFont(font);
        // 设置日期格式化样式
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd hh:mm:ss"));
        // 设置水平居中
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直居中
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置配景色及颜色的填充方式(如果不设置填充方式，背景色填充不上)
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.index);
//        cellStyle.setFillPattern(FillPatternType.ALT_BARS);
        // 创建一个单元格
        HSSFCell cell = row.createCell(0);
        // 设置单元格内容类型
        //cell.setCellType(CellType.STRING);
        // 给单元格设置值
        cell.setCellValue(new Date());
        // 给单元格设置样式
        cell.setCellStyle(cellStyle);

        HSSFRow row2 = sheet1.createRow(1);
        HSSFCell cell2 = row2.createCell(0);
        //cell2.setCellType(CellType.STRING);
        // 通过换行符，设置强制换行
        // 设置自动换行
        // HSSFCellStyle cellStyle=workbook.createCellStyle();
        // cellStyle.setWrapText(true);
        cell2.setCellValue("我要\n换行");
        // 创建输出流
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("/home/miracle/Downloads/a.xls");
            // 将workbook写入流中
            workbook.write(fileOutputStream);

            // 关流
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 读取Excel文件的内容
     * @param inputStream excel文件，以InputStream的形式传入
     * @param sheetName sheet名字
     * @return 以List返回excel中内容
     */
    public static List<Map<String, String>> readExcel(InputStream inputStream, String sheetName) {

        //定义工作簿
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }

        //定义工作表
        XSSFSheet xssfSheet;
        if (sheetName.equals("")) {
            // 默认取第一个子表
            xssfSheet = xssfWorkbook.getSheetAt(0);
        } else {
            xssfSheet = xssfWorkbook.getSheet(sheetName);
        }

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //定义行
        //默认第一行为标题行，index = 0
        XSSFRow titleRow = xssfSheet.getRow(0);

        //循环取每行的数据
        for (int rowIndex = 1; rowIndex < xssfSheet.getPhysicalNumberOfRows(); rowIndex++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowIndex);
            if (xssfRow == null) {
                continue;
            }

            Map<String, String> map = new LinkedHashMap<String, String>();
            //循环取每个单元格(cell)的数据
            for (int cellIndex = 0; cellIndex < xssfRow.getPhysicalNumberOfCells(); cellIndex++) {
                XSSFCell titleCell = titleRow.getCell(cellIndex);
                XSSFCell xssfCell = xssfRow.getCell(cellIndex);
                map.put(getString(titleCell),getString(xssfCell));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 把单元格的内容转为字符串
     * @param xssfCell 单元格
     * @return 字符串
     */
    public static String getString(XSSFCell xssfCell) {
        if (xssfCell == null) {
            return "";
        }
//        if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
//            return String.valueOf(xssfCell.getNumericCellValue());
//        } else if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
//            return String.valueOf(xssfCell.getBooleanCellValue());
//        } else {
//            return xssfCell.getStringCellValue();
//        }
        return "";
    }

    /**
     * 把内容写入Excel
     * @param list 传入要写的内容，此处以一个List内容为例，先把要写的内容放到一个list中
     * @param outputStream 把输出流怼到要写入的Excel上，准备往里面写数据
     */
    public static void writeExcel(List<List> list, OutputStream outputStream) {
        //创建工作簿
        XSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = new XSSFWorkbook();

        //创建工作表
        XSSFSheet xssfSheet;
        xssfSheet = xssfWorkbook.createSheet();

        //创建行
        XSSFRow xssfRow;

        //创建列，即单元格Cell
        XSSFCell xssfCell;

        //把List里面的数据写到excel中
        for (int i=0;i<list.size();i++) {
            //从第一行开始写入
            xssfRow = xssfSheet.createRow(i);
            //创建每个单元格Cell，即列的数据
            List sub_list =list.get(i);
            for (int j=0;j<sub_list.size();j++) {
                xssfCell = xssfRow.createCell(j); //创建单元格
                xssfCell.setCellValue((String)sub_list.get(j)); //设置单元格内容
            }
        }

        //用输出流写到excel
        try {
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
