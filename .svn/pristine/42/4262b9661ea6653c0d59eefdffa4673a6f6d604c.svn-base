package io.nuite.common.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImportMultipartExcelUtil {
	
	/**
	 * 解析上传的excel文件，组装成List<List<Object>>对象
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static List<List<Object>> importExcel(MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new Exception("文件不存在！");
		}

		InputStream in = file.getInputStream();
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = null;
		String fileName = file.getOriginalFilename();
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (".xls".equals(fileType)) {
			work = new HSSFWorkbook(in); // excel2003-版本
		} else if (".xlsx".equals(fileType)) {
			work = new XSSFWorkbook(in); // excel2007+版本
		} else {
			throw new Exception("文件格式有误！");
		}

		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if (row == null) {
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = 0; y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					if (cell == null) {
						li.add(null);
					} else {
						li.add(getCellValue(cell));
					}
				}
				list.add(li);
			}
		}
		work.close();
		in.close();
		return list;
	}


    /**
     * 根据单元格类型，对单元格中的数据格式化
     * @param cell
     * @return
     */
	private static Object getCellValue(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式化
		DecimalFormat nf = new DecimalFormat("0.00"); // 格式化数字

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:  //当单元格格式的分类设置成常规，存储的是string类型；或者单元格格式的分类设置成文本；进入
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
            if("@".equals(cell.getCellStyle().getDataFormatString())){ //当单元格格式的分类设置成文本，但是实际存在单元格中的是一个数字，并使用excel纠错将文本转换为数字后再将单元格格式转为文本时，进入
              value = df.format(cell.getNumericCellValue());
            } else if("General".equals(cell.getCellStyle().getDataFormatString())){ //当单元格格式的分类设置成常规，但是实际存在单元格中的是一个数字时，进入
				BigDecimal big = new BigDecimal(nf.format(cell.getNumericCellValue()));
				String s = big.toString();
				//如果是int类型， 去掉后面的.00
				if(!s.trim().equals("")){
				     String[] item = s.split("[.]");
				     if(item.length > 1 && "00".equals(item[1])){
				    	 s = item[0];
				     }
				}
				value = s;
            } else if (HSSFDateUtil.isCellDateFormatted(cell)) { 
              value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));    
            }
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			value = cell.toString();
			break;
		}
		return value;
	}


}