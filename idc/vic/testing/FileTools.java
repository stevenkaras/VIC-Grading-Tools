package idc.vic.testing;

import idc.vic.VICParsingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Utility class containing several useful methods for dealing with VIC format
 * files.
 * 
 * @author Steven Karas
 */
class FileTools {
	// TODO: write javadoc
	public static final String ASM_EXT = ".asm";

	private static final int BUFFER_SIZE = 1024;

	/**
	 * Read a text file into a String using a constant sized block buffer
	 * 
	 * @param file
	 *            a File name descriptor
	 * @return a String or null, if the file didn't exist or had I/O errors
	 */
	static String readTextFile(File file) {
		FileReader reader;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		StringBuilder result = new StringBuilder(BUFFER_SIZE);
		char[] buffer = new char[BUFFER_SIZE];
		try {
			int buflength;
			while ((buflength = reader.read(buffer)) != -1) {
				result.append(buffer, 0, buflength);
			}
		} catch (IOException e) {
			return null;
		}
		return result.toString();
	}

	/**
	 * Reads the next line from a scanner, skipping blank lines.
	 * 
	 * @param scan
	 *            the scanner to read from
	 * @return
	 * @throws VICParsingException
	 *             if the end of file is reached
	 */
	static String getNextLine(Scanner scan) throws VICParsingException {
		if (scan.hasNextLine()) {
			String test = scan.nextLine();
			if (test.trim().isEmpty())
				return getNextLine(scan); // skip blank lines
			return test;
		} else
			throw new VICParsingException("Unexpected end of file");
	}

	private static final char TEST_DELIMITER = '.';
	private static final int NOT_FOUND = -1;

	/**
	 * Extract a program's name from the given file name. A program name is defined as follows:
	 * {program-name}[.{anything}]
	 * 
	 * @param fn
	 * @return
	 */
	static String extractProgramName(File fn) {
		String programName = fn.getName();
		int delimiterIndex = programName.lastIndexOf(TEST_DELIMITER);
		if (delimiterIndex != NOT_FOUND)
			programName = programName.substring(0, delimiterIndex);
		return programName;
	}
}
