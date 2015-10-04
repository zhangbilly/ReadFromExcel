package com.zworks.rfe.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
	public static List<String> getColumnNames(File file) {
		List<String> columnList = new ArrayList<String>();
		try {
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			Row row = sheet.getRow(0);
			for (Cell cell : row) {
				if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
					columnList.add(cell.getRichStringCellValue().getString());
				}

			}

		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnList;

	}

	public static List<String> getPreviewSql(File file,int queryIndex,String tablename,List<String> columns,HashMap<String,String> naneToCodeMap){
		
		Workbook wb;
		List<String> sql = new ArrayList<String>();
		try {
			wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheetAt(0);
			for(int i=1;i<sheet.getLastRowNum();i++){
				Row r = sheet.getRow(i);
				List<String> l = new ArrayList<String>();
				StringBuffer sb = new StringBuffer();
				for(Cell cell:r){
					switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_STRING:
                        StringBuffer s = new StringBuffer();
                        s.append("'");
                        s.append(cell.getRichStringCellValue().getString());
                        s.append("'");
	                    l.add(s.toString());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        System.out.println(cell.getDateCellValue());
	                    } else {
	                        
	                        l.add(Double.toString(cell.getNumericCellValue()));

	                    }
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    System.out.println(cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    System.out.println(cell.getCellFormula());
	                    break;
	                default:
	                    System.out.println();
	            }
				
					
				}
				sb.append("update ");
				sb.append(tablename);
				sb.append(" set ");
				for(int j=0;j<l.size();j++){
					if(j!=queryIndex){
						sb.append(naneToCodeMap.get(columns.get(j)));
						sb.append(" = ");
						sb.append(l.get(j));
						sb.append(",");
					}
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(" where ");
				sb.append(naneToCodeMap.get(columns.get(queryIndex)));
				sb.append(" = ");
				sb.append(l.get(queryIndex));
				sb.append(";");
				sql.add(sb.toString());
			}
	
			
			
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sql;
	}
}
