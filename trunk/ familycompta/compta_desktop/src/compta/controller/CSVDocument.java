package compta.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class CSVDocument {

	private int rowCount;

	private int colCount;

	private Vector<Vector<String>> content;

	private final static char CELL_SEPARATOR = ',';

	private final static char CELL_DELIMITER = '"';

	private final static char NEWLINE = '\n';

	public CSVDocument() {
		content = new Vector<Vector<String>>();
		rowCount = 0;
		colCount = 0;
	}

	/**
	 * Creates a CSV file by reading the input file
	 * 
	 * @param inputFile
	 *            the file to parse.
	 * @throws IOException
	 */
	public CSVDocument(File inputFile) throws IOException {

		FileReader fileReader = new FileReader(inputFile);
		BufferedReader buff = new BufferedReader(fileReader);

		content = new Vector<Vector<String>>();
		Vector<String> bufferLine = new Vector<String>();
		StringBuffer bufferCell = new StringBuffer();

		int intC;
		colCount = 0;
		boolean isInLongCell = false;
		boolean endOfFile = false;

		while (!endOfFile) {
			intC = buff.read();
			char c = (char) intC;

			if (intC == -1) {
				// end of file
				endOfFile = true;
				if (bufferLine.size() > 0 || bufferCell.length() > 0) {
					// store cell
					bufferLine.add(bufferCell.toString());
					bufferCell = new StringBuffer();

					// store line
					content.add(bufferLine);
					// compute number of colums max
					colCount = Math.max(colCount, bufferLine.size());
				}
			} else if (c == CELL_DELIMITER) {
				isInLongCell = !isInLongCell;

			} else if (c == CELL_SEPARATOR) {
				if (!isInLongCell) {
					// store cell
					bufferLine.add(bufferCell.toString());
					bufferCell = new StringBuffer();
				} else {
					bufferCell.append(c);
				}

			} else if (c == NEWLINE) {
				if (!isInLongCell) {

					// store cell
					bufferLine.add(bufferCell.toString());
					bufferCell = new StringBuffer();

					// store line
					content.add(bufferLine);
					// compute number of colums max
					colCount = Math.max(colCount, bufferLine.size());

					bufferLine = new Vector<String>();
				} else {
					bufferCell.append(c);
				}
			} else if (c == '\r') {
				// ignore
			} else if (c == '#') {
				if (!isInLongCell) {
					// this is a comment. Consume all the rest of the line
					buff.readLine();
				}
			} else {
				bufferCell.append(c);
			}
		}

		rowCount = content.size();

		buff.close();
		fileReader.close();
	}

	/**
	 * Returns the colsCount.
	 * 
	 * @return int
	 */
	public int getColCount() {
		return colCount;
	}

	/**
	 * Returns the rowsCount.
	 * 
	 * @return int
	 */
	public int getRowsCount() {
		return rowCount;
	}

	/**
	 * 
	 * @param rowIndex
	 *            the index of the row to test
	 * @return true if every cell in the row is empty (i.e. equals empty
	 *         string), false otherwise.
	 */
	public boolean isRowEmpty(int rowIndex) {
		if (rowIndex >= rowCount || rowIndex < 0) {
			throw new IndexOutOfBoundsException("" + rowIndex);
		}

		for (int j = 0; j < colCount; j++) {
			if (!this.get(rowIndex, j).equals("")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method get.
	 * 
	 * @param row
	 *            the raw where to retrieve data
	 * @param col
	 *            the column where to retrieve data
	 * @return String the data in the given row and column. Throws
	 *         IndexOutOfBoundsException if indexes are out of bounds.
	 * 
	 */
	public String get(int row, int col) {
		if (row < 0 || col < 0 || row > (getRowsCount() - 1)
				|| col > (getColCount() - 1)) {
			throw new IndexOutOfBoundsException();
		}
		try {
			Vector<String> theRow = content.get(row);
			if (theRow == null) {
				return "";
			}
			if (col >= theRow.size()) {
				return "";
			} else {
				String result = theRow.get(col);
				return (result == null ? "" : result);
			}
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	/**
	 * Sets the value at the given position. The csv size is extended so as to
	 * include (row,col) position.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @param value
	 *            the value to set.
	 */
	public void set(int row, int col, String value) {

		// check the row size
		if (row >= rowCount) {
			rowCount = row + 1;
			content.setSize(rowCount);
		}

		// get the row
		Vector<String> theRow;
		if (content.get(row) == null) {
			theRow = new Vector<String>(col + 1);
			content.set(row, theRow);
		} else {
			theRow = content.get(row);
		}

		// check the column size
		colCount = Math.max(colCount, col + 1);
		if (theRow.size() <= col) {
			theRow.setSize(col + 1);
		}

		// set the value
		theRow.set(col, value);
	}

	/**
	 * Prints the content to the output file in CSV (Comma separated) format .
	 * 
	 * @param file
	 *            the output file
	 * @throws IOException
	 */
	public void printToFile(File outputFile) throws IOException {
		FileWriter fileWriter = new FileWriter(outputFile);
		BufferedWriter buffWriter = new BufferedWriter(fileWriter);

		for (int i = 0; i < rowCount; i++) {

			for (int j = 0; j < colCount; j++) {
				String cell = this.get(i, j);

				if (cell.contains("" + CELL_DELIMITER)) {
					// the cell contains an ", which is quite surprising but can
					// occur
					cell.replace(CELL_DELIMITER, ' ');
					buffWriter.write(cell);
				} else if (cell.contains("" + CELL_SEPARATOR)
						|| cell.contains("" + NEWLINE) || cell.contains("#")) {
					// the cell contains special characters => the cell must be
					// surrounded by ""
					buffWriter.write('"');
					buffWriter.write(cell);
					buffWriter.write('"');
				} else if (cell.contains("\r")) {
					// delete this character
					cell.replace('\r', ' ');
					buffWriter.write(cell);
				} else {
					buffWriter.write(cell);
				}
				if (j < colCount - 1) {
					buffWriter.write(CELL_SEPARATOR);
				}
			}

			buffWriter.write(NEWLINE);
		}

		buffWriter.close();
		fileWriter.close();
	}

}
