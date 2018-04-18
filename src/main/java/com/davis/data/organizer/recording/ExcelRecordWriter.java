package com.davis.data.organizer.recording;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

public class ExcelRecordWriter {
  private static final Logger log = LoggerFactory.getLogger(ExcelRecordWriter.class.getName());
  private static boolean intialized = false;
  private static String excelFilePath;

  public static void intialize(Path pathOfFiletoCreate, List<String> headerFields)
      throws IOException {

    if (!pathOfFiletoCreate.toFile().exists()) {
      boolean success = pathOfFiletoCreate.toFile().createNewFile();
      if (success) {
        writeHeadersToXLSX(headerFields, pathOfFiletoCreate.toFile());
        excelFilePath = pathOfFiletoCreate.toAbsolutePath().toString();
      } else {
        log.error(
            "Failed to create XLSX file of {}", pathOfFiletoCreate.toAbsolutePath().toString());
      }
    } else {
      log.info("Excel file of {} already exists ", pathOfFiletoCreate.toAbsolutePath().toString());
    }
  }

  private static void writeHeadersToXLSX(List<String> headerFields, File xlsxFile) {
    String sheetName = "Restructuring"; //name of sheet
	  try(XSSFWorkbook wb = new XSSFWorkbook()){
		  XSSFSheet sheet = wb.createSheet(sheetName);
		  XSSFRow row = sheet.createRow(0);
		  int count = -1;
		  for (String headerField : headerFields) {
			  XSSFCell cell = row.createCell((count + 1));
			  cell.setCellValue(headerField);
		  }

		  try (FileOutputStream fileOut = new FileOutputStream(xlsxFile)) {
			  //write this workbook to an Outputstream.
			  wb.write(fileOut);
			  fileOut.flush();
			  fileOut.close();
		  } catch (IOException fnF) {
			  log.error("Unable to write header fields into the XLSX sheet. {}", fnF);
		  }
	  }catch (IOException e) {
		  log.error("Unable to close workbook after header field write. {}", e);
	  }
  }

  private static void appendXLSXFile(File xlsxFile) {

  	try(InputStream inp = new FileInputStream(xlsxFile)){
		Workbook wb = WorkbookFactory.create(inp);
		Sheet sheet = wb.getSheetAt(0);
		Row row = sheet.getRow(0);
		Cell cell = row.getCell(0);
		String cellContents = cell.getStringCellValue();
		//Modify the cellContents here
		// Write the output to a file
		cell.setCellValue(cellContents);
		FileOutputStream fileOut = new FileOutputStream("wb.xls");
		wb.write(fileOut);
		fileOut.close();
	} catch (IOException e) {
		log.error("Unable to open XLSX file during append operations {}", e);
	} catch (InvalidFormatException e) {
		e.printStackTrace();
	}

  }

  public static void readXLSFile() throws IOException {
    InputStream ExcelFileToRead = new FileInputStream(new File("Test.xls"));
    HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

    HSSFSheet sheet = wb.getSheetAt(0);
    HSSFRow row;
    HSSFCell cell;

    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (HSSFRow) rows.next();
      Iterator cells = row.cellIterator();

      while (cells.hasNext()) {
        cell = (HSSFCell) cells.next();

        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
          System.out.print(cell.getStringCellValue() + " ");
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
          System.out.print(cell.getNumericCellValue() + " ");
        } else {
          //U Can Handel Boolean, Formula, Errors
        }
      }
      System.out.println();
    }
  }

  public static void writeXLSFile() throws IOException {

    String excelFileName = "Test.xls"; //name of excel file

    String sheetName = "Sheet1"; //name of sheet

    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet(sheetName);

    //iterating r number of rows
    for (int r = 0; r < 5; r++) {
      HSSFRow row = sheet.createRow(r);

      //iterating c number of columns
      for (int c = 0; c < 5; c++) {
        HSSFCell cell = row.createCell(c);

        cell.setCellValue("Cell " + r + " " + c);
      }
    }

    FileOutputStream fileOut = new FileOutputStream(excelFileName);

    //write this workbook to an Outputstream.
    wb.write(fileOut);
    fileOut.flush();
    fileOut.close();
  }

  public static void readXLSXFile() throws IOException {
    InputStream ExcelFileToRead = new FileInputStream("Test.xlsx");
    XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);

    XSSFWorkbook test = new XSSFWorkbook();

    XSSFSheet sheet = wb.getSheetAt(0);
    XSSFRow row;
    XSSFCell cell;

    Iterator rows = sheet.rowIterator();

    while (rows.hasNext()) {
      row = (XSSFRow) rows.next();
      Iterator cells = row.cellIterator();
      while (cells.hasNext()) {
        cell = (XSSFCell) cells.next();

        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
          System.out.print(cell.getStringCellValue() + " ");
        } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
          System.out.print(cell.getNumericCellValue() + " ");
        } else {
          //U Can Handel Boolean, Formula, Errors
        }
      }
      System.out.println();
    }
  }

  public static void main(String[] args) throws IOException {

    writeXLSFile();
    readXLSFile();

    readXLSXFile();
  }
}
