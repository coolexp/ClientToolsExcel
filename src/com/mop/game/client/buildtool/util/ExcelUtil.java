package com.mop.game.client.buildtool.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.coolexp.vo.ObjKeyVO;

/**
 * 标题: Excel读取工具.<br>
 * 
 * 描述：Excel读取工具，主要用于读取excel中的数据.<br>
 * 
 * 版权：Copyright (C) 1997-2008 Oak Pacific Interactive. <br>
 * 
 * @author 黄杰 2010-06-011 10:50 新建
 * 
 * @since jdk1.6
 * 
 * @version 0.1
 */
public class ExcelUtil {
	public static Map<String,ObjKeyVO> getKeysList(String xlsPath, String sheetName) throws Exception{
		InputStream inp = new FileInputStream(xlsPath);
		HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
		Map<String,ObjKeyVO> map = new HashMap<String,ObjKeyVO>();
		HSSFSheet childSheet = workbook.getSheet(sheetName);
		if(childSheet==null){
			System.out.println("表格"+xlsPath+" Sheet: "+sheetName+"不存在");
			System.exit(0);
		}
		int rowNum = childSheet.getPhysicalNumberOfRows();
		HSSFRow firstRow = childSheet.getRow(0);
		int cellNum = 0;
		if(firstRow!=null){
			cellNum = firstRow.getPhysicalNumberOfCells();
		}
		for(int j=5;j<rowNum;j++) {
			HSSFRow row = childSheet.getRow(j);
			ObjKeyVO ovo = new ObjKeyVO();
			for(int k=1;k<cellNum;k++){
				HSSFCell cell = row.getCell(k);
				switch(k){
				case 1:
					ovo.sheetName = getCellData(cell);
					break;
				case 2:
					ovo.keyStr = getCellData(cell);
					break;
				case 4:
					ovo.searchAttr = getCellData(cell);
				case 5:
					ovo.methodStr = getCellData(cell);
					break;
				}
			}
			map.put(ovo.sheetName, ovo);
		}
		return map;
	}
	
	/**
	 * 获取指定Excel指定sheet的内容
	 * 
	 * @param xlsName
	 * @param sheetName
	 * @return
	 */
	public static String[][] getSheetData(String xlsPath, String sheetName)
			throws Exception {
//		OtherUtils.showInfo("[#GS.GameData.loadFromXls -- >object] [excel:"
//				+ xlsPath + " sheet:" + sheetName + "]");
		InputStream inp = new FileInputStream(xlsPath);
		HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(inp));
		HSSFSheet sheet = workbook.getSheet(sheetName);
		int rowNumber = sheet.getPhysicalNumberOfRows();
		HSSFRow row = sheet.getRow(0);
		if (rowNumber == 0 || row == null) {
			return new String[0][0];
		}
		
		int columnNumber = row.getLastCellNum();
		
		if (row.getCell(0) == null || row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_BLANK) {// 增加新规则，如果(0,0)为空，则整页sheet跳过
			return new String[0][0];
		}

		// 判断列值
		Object tmp;
		for (int i = 0; i < columnNumber; i++) {
			tmp = row.getCell(i);
			if (!"".equals(tmp) && tmp != null) {
				continue;
			} else {
				columnNumber = i;
				break;
			}
		}

		String[][] data = new String[rowNumber][columnNumber];
		boolean end = false;
		int rowNumber_legal = rowNumber;
		for (int rowIndex = 0; rowIndex < rowNumber; rowIndex++) {
			if (end) {// 如果到达地端
				break;
			}

			row = sheet.getRow(rowIndex);
			data[rowIndex] = new String[columnNumber];

			if (row == null) {// 如果整行为空时，直接退出
				rowNumber_legal = rowIndex;
				end = true;
				break;
			}

			tmp = row.getCell(0);
			if ("".equals(tmp) || tmp == null) {// 如果某行的第一列为空时，退出
				rowNumber_legal = rowIndex;
				break;
			}

			for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
				data[rowIndex][columnIndex] = PoiUtils.getStringValue(row
						.getCell(columnIndex));
			}

		}
		if (rowNumber_legal != rowNumber) {
			// 最终数据整理
			String[][] back = new String[rowNumber_legal][columnNumber];
			System.arraycopy(data, 0, back, 0, rowNumber_legal);

			return back;
		} else {
			return data;
		}
	}
	
	/**
	 * 通过excel路径获得HSSFWorkbook
	 * @param xlsPath
	 * @return
	 * @throws Exception
	 */
	public static HSSFWorkbook getWorkBook(String xlsPath) throws Exception{
//		OtherUtils.showInfo("[#GS.GameData.loadFromXls -- >object] [excel:"
//				+ xlsPath + "]");
		InputStream inp = new FileInputStream(xlsPath);
		
		return new HSSFWorkbook(new POIFSFileSystem(inp));
	}
	
	/**
	 * 获得sheetName在HSSFWorkbook中的index
	 * @param workbook
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static int getSheetIndex(HSSFWorkbook workbook, String sheetName) throws Exception {
		return workbook.getSheetIndex(sheetName);
	}
	

	/**
	 * 获得columnName在sheetName中的index
	 * @param workbook
	 * @param sheetName
	 * @return
	 * @throws Exception
	 */
	public static int getColumnIndex(HSSFWorkbook workbook,String sheetName,String columnName) throws Exception {
		
		HSSFSheet sheet = workbook.getSheet(sheetName);
		
		int rowNumber = sheet.getPhysicalNumberOfRows();
		if (rowNumber == 0) {
			return -1;
		}
		HSSFRow row = sheet.getRow(0);
		int columnNumber = row.getLastCellNum();
		if (row.getCell(0) == null) {// 增加新规则，如果(0,0)为空，则整页sheet跳过
			return -1;
		}

		// 判断列值
		for (int i = 0; i < columnNumber; i++) {
			if (row.getCell(i) != null&&PoiUtils.getStringValue(row.getCell(i)).equals(columnName)) {
				return i;
			}
		}
		
		return -1;		
	}
	
	/**
	 * 获取数据指定页的数据
	 * 
	 * @param path
	 *            excel的路径
	 * @param sheetIndex
	 *            页的序号
	 * @return 返回二维数据
	 * @throws IOException
	 *             向上抛出错误
	 */
	public static List<Map<String, String>> getSheetData(String path,
			int sheetIndex) throws IOException {
		// 获取数据
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		// 找到文件
		InputStream inp = new FileInputStream(path);
		// 打开excel
		HSSFWorkbook workBook = new HSSFWorkbook(new POIFSFileSystem(inp));
		// 打开页
		HSSFSheet sheet = workBook.getSheetAt(sheetIndex);
		// 获取总条数
		int rowCount = sheet.getPhysicalNumberOfRows();
		// 没有数据
		if (rowCount < 2) {
			throw new IOException("行为数据有误，请检查！");
		}

		// 获取总列数
		HSSFRow row = sheet.getRow(0);
		int columnCount = row.getLastCellNum();
		if (row.getCell(0) == null) {// 增加新规则，如果(0,0)为空，则整页sheet跳过
			return data;
		}

		// 没有数据
		if (columnCount == 0) {
			throw new IOException("列为空，请检查！");
		}

		// 获取键值的名字
		row = sheet.getRow(1);
		String[] key = new String[columnCount];
		for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
			key[columnIndex] = getCellData(row.getCell(columnIndex));
		}

		

		// 行容器
		Map<String, String> rowData = new HashMap<String, String>();
		for (int rowIndex = 2; rowIndex < rowCount; rowIndex++) {
			// 获取行
			row = sheet.getRow(rowIndex);
			// 获取数据
			rowData = new HashMap<String, String>();
			for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
				rowData.put(key[columnIndex],
						getCellData(row.getCell(columnIndex)));
			}
			data.add(rowData);
		}

		return data;
	}

	/**
	 * 获取单元格中的数据
	 * 
	 * @param cell
	 *            要获取数据的单元格
	 * @return 返回数据
	 */
	public static String getCellData(HSSFCell cell) {
		if (cell == null) {
			return "";
		}

		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING: {
			return cell.toString();
		}

		case HSSFCell.CELL_TYPE_NUMERIC: {
			String tmp = cell.toString();
			if (tmp.endsWith(".0")) {
				tmp = tmp.substring(0, tmp.length() - 2);
			}
			return tmp;
		}

		default: {
			return cell.toString();
		}
		}
	}

	/**
	 * 获取所有页面的名字
	 * 
	 * @param path
	 * 
	 * @return 所有sheet的名字
	 */
	public static String[] getSheetName(String path) throws IOException {
		// 找到文件
		InputStream inp = new FileInputStream(path);
		// 打开excel
		HSSFWorkbook workBook = new HSSFWorkbook(new POIFSFileSystem(inp));
		int count = workBook.getNumberOfSheets();
		String[] sheetName = new String[count];
		for (int i = 0; i < count; i++) {
			sheetName[i] = workBook.getSheetName(i);
		}
		return sheetName;
	}
}
