package module6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
 



import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class IataCodeReader {

	public static String getCityNameGivenIataCode(String iataCode) throws IOException{
		
		String cityName = null;
		String excelFilePath = "C:\\Users\\cvora\\git\\UCSD-OOJava\\OOPStarterCode"
				+ "\\UCSDUnfoldingMaps\\data\\airport-codes.xls";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		HSSFWorkbook  workbook = new HSSFWorkbook(inputStream);
		HSSFSheet firstSheet=workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		
		while(iterator.hasNext()){
			
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Cell prevCell = null;

			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();

				switch(cell.getCellType()){
				
				case Cell.CELL_TYPE_STRING:
					if((cell.getStringCellValue().equals(iataCode)) && (prevCell != null))
						cityName = prevCell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    //System.out.print(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    //System.out.print(cell.getNumericCellValue());
                    break;
                default:
                   	//System.out.print("WTF IS IS DATA");
                   	break;
				}
					//System.out.print(" - ");
					prevCell = cell;
			}
			//System.out.println();
		}
	   
		workbook.close();
		inputStream.close();
		
		return cityName;
	}

	public static void tester() throws IOException{
		
		System.out.println(getCityNameGivenIataCode("BOM"));
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		tester();
	}

}
