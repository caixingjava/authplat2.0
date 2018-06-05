package com.minivision.authplat2.mvc;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.minivision.authplat2.constants.CommonConstants;
import com.minivision.authplat2.domain.OpLog;

/**
 * 操作日志导出Excel
 * @author hughzhao
 * @2017年5月24日
 */
public class OpLogExcelView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		@SuppressWarnings("unchecked")
		List<OpLog> data = (List<OpLog>) model.get("data");  
  
        // 设置文件名
        response.setHeader("Content-Disposition", "attachment;filename=" + new String("操作日志".getBytes(), "ISO-8859-1") + ".xls");  
          
        // sheet的名称  
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("操作日志"); 
        HSSFCellStyle headerStyle = (HSSFCellStyle) workbook.createCellStyle(); //标题样式
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont headerFont = (HSSFFont) workbook.createFont();    //标题字体
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short)12);
        headerStyle.setFont(headerFont);
        short width = 20, height = 25 * 20;
        sheet.setDefaultColumnWidth(width);
          
        HSSFRow row = null;  
        HSSFCell cell = null;
        
        String[] titles = {"操作人员","IP地址","操作时间","操作名称"};
  
        // 行号  
        int rowIndex = 0;  
        // 列号  
        int cellIndex = 0;  
  
        // 通过sheet对象增加一行  
        row = sheet.createRow(rowIndex++);
        for (String title : titles) {
        	// 通过row对象增加一列  
            cell = row.createCell(cellIndex++);
            cell.setCellStyle(headerStyle);
            // 设置列的内容  
            cell.setCellValue(title);
		}
        sheet.getRow(0).setHeight(height);
         
        HSSFCellStyle contentStyle = (HSSFCellStyle) workbook.createCellStyle(); //内容样式
        contentStyle.setAlignment(HorizontalAlignment.CENTER);
        for (OpLog opLog : data) {
        	// 列号清零  
            cellIndex = 0;
        	// 增加一行  
        	row = sheet.createRow(rowIndex++); 
        	row.setRowStyle(contentStyle);
        	// 增加一列  
    		cell = row.createCell(cellIndex++);  
    		// 设置列的内容  
    		cell.setCellValue(opLog.getOperator() == null ? "" : opLog.getOperator().getUsername());
    		// 增加一列  
    		cell = row.createCell(cellIndex++);  
    		// 设置列的内容  
    		cell.setCellValue(opLog.getIp());
    		// 增加一列  
    		cell = row.createCell(cellIndex++);  
    		// 设置列的内容  
    		cell.setCellValue(DateFormatUtils.format(opLog.getOpTime(), CommonConstants.FULL_DATE_FORMAT));
    		// 增加一列  
    		cell = row.createCell(cellIndex++); 
    		sheet.autoSizeColumn(cellIndex - 1);
    		// 设置列的内容  
    		cell.setCellValue(opLog.getOperation());
        }
	}

}
