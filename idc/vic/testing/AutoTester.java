package idc.vic.testing;

import idc.vic.VICParsingException;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The class that brings it all together. Requires the following directory layout:<br>
 * root<br>
 * root/{programname}.testSuite<br>
 * root/{ID#}/{programname}.asm<br>
 * 
 * @author Steven Karas
 */
public class AutoTester {
	private static class EndfixFilter implements FilenameFilter {
		private String endfix;
		private boolean prefix;
		EndfixFilter(String endfix, boolean prefix) {
			this.endfix = endfix;
			this.prefix = prefix;
		}
		public boolean accept(File dir, String name) {
			return (prefix) ? name.startsWith(endfix) : name.endsWith(endfix);
		}
	}

	//TODO: write javadoc
	public static List<VICTestFolder> loadSubdirectories(File root,
			Map<String, VICTestSuite> tests) {
		File[] listing = root.listFiles(new FileFilter() { public boolean accept(File file) {
			return file.isDirectory();
	         }});
		List<VICTestFolder> folders = new LinkedList<VICTestFolder>();
		for (File subdirectory : listing) {
			VICTestFolder subfolder = new VICTestFolder(subdirectory, tests);
			folders.add(subfolder);
			File[] asms = subdirectory.listFiles(new EndfixFilter(FileTools.ASM_EXT, false));
			for (File asm : asms) {
				String programName = FileTools.extractProgramName(asm);
				if (!tests.containsKey(programName)) {
					subfolder.addResult("Extra file "+asm.getName()+ " found. Possibly misnamed?");
				}
			}
		}
		return folders;
	}

	//TODO: write javadoc
	public static Map<String, VICTestSuite> loadTestSuites(File directory) {
		Map<String, VICTestSuite> tests = new LinkedHashMap<String, VICTestSuite>();
		File[] listing = directory.listFiles(new EndfixFilter(VICTestSuite.TEST_SUITE_EXT, false));
		for (File suite : listing) {
			Scanner scan = null;
			try {
				scan = new Scanner(suite);
			} catch (FileNotFoundException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
			VICTestSuite testSuite;
			try {
				testSuite = new VICTestSuite(scan);
			} catch (VICParsingException e) {
				System.out.println("Could not load test suite '"+suite.getName()+"'. Reason: " + e.getMessage());
				continue;
			} finally {
				scan.close();
			}
			if (!testSuite.tests.isEmpty()) {
				tests.put(FileTools.extractProgramName(suite), testSuite);
			}
		}
		return tests;
	}
}