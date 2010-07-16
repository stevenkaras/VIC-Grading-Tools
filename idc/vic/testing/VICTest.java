package idc.vic.testing;

import idc.vic.VICParsingException;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class represents a test that can be run on a VIC program. A test is made
 * up of two sets of integers. One is the provided input, and the other is the
 * expected output.
 * 
 * This class has a corresponding filetype defined as follows:
 * name={testname}
 * [INPUT]
 * {values}
 * ...
 * {values}
 * [END]
 * [EXPECTED]
 * {values}
 * ...
 * {values}
 * [END]
 * 
 * @author Steven Karas
 */
public class VICTest {
	private static final int NOT_FOUND = -1;
	//TODO: make these private
	public String testName;
	public int[] input;
	public int[] expected;

	//TODO: write javadoc
	public VICTest(Scanner source) throws VICParsingException {
		if (source == null)
			throw new IllegalArgumentException();
		loadFromStream(source);
	}
	
	public VICTest(String testName, int input[], int expected[]) {
		this.testName = testName;
		this.input = input.clone();
		this.expected = expected.clone();
	}
	
	private void loadFromStream(Scanner fromHere) throws VICParsingException {
		String nextLine = FileTools.getNextLine(fromHere);
		int index = nextLine.indexOf('=');
		if (index == NOT_FOUND || !nextLine.substring(0, index).trim().equalsIgnoreCase("name"))
			throw new VICParsingException("malformed test name");
		testName = nextLine.substring(index+1).trim();
		loadInputExpected(fromHere);
		loadInputExpected(fromHere);
	}
	
	private void loadInputExpected(Scanner fromHere) throws VICParsingException {
		String label = FileTools.getNextLine(fromHere);
		int temp[] = loadIntArray(fromHere);
		if (label.equalsIgnoreCase("[INPUT]"))
			this.input = temp;
		else if (label.equalsIgnoreCase("[EXPECTED]"))
			this.expected = temp;
		else
			throw new VICParsingException("Unexpected test section: "+label);
	}
	
	private int[] loadIntArray(Scanner fromHere) throws VICParsingException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		while (true) {
			String nextLine = FileTools.getNextLine(fromHere);
			if (nextLine.equalsIgnoreCase("[END]"))
				break; // marks end of int array
			else {
				try {
					result.add(Integer.parseInt(nextLine));
				} catch (NumberFormatException e) {
					throw new VICParsingException("Malformed numeric input: "+e.getMessage());
				}
			}
		}
		return TestTools.deboxInteger(result);
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[VICTEST]\n");
		buffer.append("name=");
		buffer.append(testName);
		buffer.append("\n[INPUT]\n");
		appendIntArray(buffer, input);
		buffer.append("[EXPECTED]\n");
		appendIntArray(buffer, expected);
		return buffer.toString();
	}

	private void appendIntArray(StringBuilder buffer, int[] array) {
		for (int x : array) {
			buffer.append(x);
			buffer.append('\n');
		}
	}
}
