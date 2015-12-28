package compta.controller;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class TestCSVFile extends TestCase {

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	public void testbuildCSVFile() {

		String[][] content = {
				{ "Cell00", "Cell01", "Cell02" },
				{ "Cell10", "Cell11", "Cell12" },
				{ " Cell with spaces ", "Cell \t with \t tabs",
						"Cell with \n newlines \n" } };

		CSVDocument csvFile = new CSVDocument();
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[0].length; j++) {
				csvFile.set(i, j, content[i][j]);
			}
		}

		assertEquals("Wrong row count", content.length, csvFile.getRowsCount());
		assertEquals("Wrong col count", content[0].length, csvFile
				.getColCount());

		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[0].length; j++) {
				assertEquals("Wrong data at (" + i + "," + j + ")",
						content[i][j], csvFile.get(i, j));
			}
		}

	}

	public void testCsvFileFromInputFile() {
		// TODO get file from properties file
		File file = new File(
				"D:\\workspace_perso\\compta\\data\\comptes_cheques.csv");

		CSVDocument csvFile;
		try {
			csvFile = new CSVDocument(file);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			return;
		}
		
		

		for (int i = 0; i < csvFile.getRowsCount(); i++) {
			for (int j = 0; j < csvFile.getColCount(); j++) {
				System.out.print(" | " + csvFile.get(i, j) + " | ");
			}
			System.out.print("\n");
		}
		
		assertEquals("Wrong row count", 24, csvFile.getRowsCount());
	}

	public void testPrintCsvToFile() {
		String[][] content = {
				{ "Cell00", "Cell01", "Cell02" },
				{ "Cell10", "Cell11", "Cell12" },
				{ " Cell with \"\". ", "Cell,w,i,t,h,,",
						"Cell with \n newlines \n" , "Cell with \runix \rnewline"}};

		CSVDocument csvFile = new CSVDocument();
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[0].length; j++) {
				csvFile.set(i, j, content[i][j]);
			}
		}

		// TODO get file from properties file
		File file = new File(
				"D:\\workspace_perso\\compta\\test\\testPrintToFile.csv");
		try {
			csvFile.printToFile(file);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			return;
		}

		CSVDocument csvFile2;
		try {
			csvFile2 = new CSVDocument(file);
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
			return;
		}

		assertEquals("Wrong row count", content.length, csvFile2.getRowsCount());
		assertEquals("Wrong col count", content[0].length, csvFile2
				.getColCount());

		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[0].length; j++) {
				assertEquals("Wrong data at (" + i + "," + j + ")",
						content[i][j], csvFile2.get(i, j));
			}
		}

	}
	

}
