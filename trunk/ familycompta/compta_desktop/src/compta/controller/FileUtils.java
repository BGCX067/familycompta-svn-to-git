package compta.controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FileUtils {

	private static final String CSV_EXT = "csv";

	private static final String XML_EXT = "xml";

	private FileUtils() {

	}

	/*
	 * Get the extension of a file.
	 */
	private static String getExtension(String _fileName) {
		String ext = null;
		int i = _fileName.lastIndexOf('.');

		if (i > 0 && i < _fileName.length() - 1) {
			ext = _fileName.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * 
	 */
	@Deprecated
	public static final FileFilter CSV_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File _file) {
			if (_file.isDirectory())
				return true;

			String ext = getExtension(_file.getName());
			return (ext != null && ext.equals(CSV_EXT));
		}

		public String getDescription() {
			return "CSV files (*." + CSV_EXT + ")";
		}
	};

	/**
	 * 
	 */
	public static final FileFilter XML_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File _file) {
			if (_file.isDirectory())
				return true;

			String ext = getExtension(_file.getName());
			return (ext != null && ext.equals(XML_EXT));
		}

		public String getDescription() {
			return "XML files (*." + XML_EXT + ")";
		}
	};

}
