package idc.vic.testing;

import idc.vic.VICParsingException;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents a set of tests to be run on the same program in VIC.
 * This class can be loaded from/stored to a file in the following format:
 * [VICTESTSUITE]
 * [VICTEST]
 * {TestVIC format section}
 * [ENDVICTEST]
 * ...
 * [VICTEST]
 * {TestVIC format section}
 * [ENDVICTEST]
 * [ENDVICTESTSUITE]
 * 
 * @author Steven Karas
 */
public class VICTestSuite {
	//TODO: write javadoc
	public static final String TEST_SUITE_EXT = ".testsuite";

	//TODO: write javadoc
	//TODO: make this private
	public List<VICTest> tests = new LinkedList<VICTest>();

	//TODO: write javadoc
	public VICTestSuite() {
		// do nothing
	}
	
	//TODO: write javadoc;
	public void addTest(VICTest toAdd) {
		tests.add(toAdd);
	}
	
	//TODO: write javadoc
	public VICTestSuite(Scanner file) throws VICParsingException {
		if (file == null)
			throw new IllegalArgumentException("file must not be null");
		loadFromStream(file);
	}

	private static final int NUM_THREADS = 4;
	private static ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

	//TODO: write javadoc
	public void runTestSuite(VICTestProgram program) {
		// queue the tasks' execution
		for (VICTest test : this.tests) {
			program.futures.put(test.testName, executor.submit(new ProgramTest(program.getSourceCode(),
					test)));
		}
	}
	
	private void loadFromStream(Scanner fromHere) throws VICParsingException {
		String nextLine = FileTools.getNextLine(fromHere);
		if (!nextLine.equalsIgnoreCase("[VICTESTSUITE]"))
			throw new VICParsingException("File is not of proper format");
		while (true){
			nextLine = FileTools.getNextLine(fromHere);
			if (nextLine.equalsIgnoreCase("[VICTEST]")) {
				try {
					VICTest test = new VICTest(fromHere);
					tests.add(test);
				} catch (VICParsingException e) {
					// eat the input until a [ENDVICTEST] tag is reached
					while (true)
						if(FileTools.getNextLine(fromHere).equalsIgnoreCase("[ENDVICTEST]"))
							break;
					continue;
				}
				if(!FileTools.getNextLine(fromHere).equalsIgnoreCase("[ENDVICTEST]"))
					throw new VICParsingException("VICTests must be marked by a ENDVICTEST tag!");
			} else if (nextLine.equalsIgnoreCase("[ENDVICTESTSUITE]")) {
				break; // last of the tests loaded
			}
		}
		
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[VICTESTSUITE]\n");
		for (VICTest test : tests) {
			buffer.append("[VICTEST]\n");
			buffer.append(test.toString());
			buffer.append("[ENDVICTEST]\n");
		}
		buffer.append("[END]\n");
		return buffer.toString();
	}
}
