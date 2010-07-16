package idc.vic.testing;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//TODO: write javadoc
public class VICTestFolder {
	private List<VICTestProgram> programs = new LinkedList<VICTestProgram>();
	private List<String> results = new LinkedList<String>();
	private String folderName;

	//TODO: write javadoc
	public VICTestFolder(File subdirectory, Map<String, VICTestSuite> tests) {
		this.folderName = subdirectory.getName();
		for (Entry<String, VICTestSuite> entry : tests.entrySet()) {
			File asm = new File(subdirectory, entry.getKey()+FileTools.ASM_EXT);
			if (!asm.exists()) {
				this.results.add("Program "+entry.getKey()+" not found.");
			} else {
				VICTestProgram underTest = new VICTestProgram(asm);
				this.programs.add(underTest);
				// Run the test suite on this program
				entry.getValue().runTestSuite(underTest);
			}
		}
	}

	//TODO: write javadoc
	public void addResult(String line) {
		results.add(line);
	}

	//TODO: write javadoc
	public List<String> getResults() {
		return Collections.unmodifiableList(results);
	}

	//TODO: write javadoc
	public String getReport() {
		StringBuilder buffer = new StringBuilder(results.size() * 20); // best guess
		buffer.append("Report for ");
		buffer.append(folderName);
		buffer.append(":\n");
		for (String line : results) {
			buffer.append(line);
			buffer.append('\n');
		}
		return buffer.toString();
	}

	//TODO: write javadoc
	public String getFullReport(boolean suppressPassed) {
		StringBuilder buffer = new StringBuilder(results.size() * 20 + programs.size() * 200);
		buffer.append(getReport());
		for (VICTestProgram program : programs) {
			buffer.append(program.getReport(suppressPassed));
		}
		return buffer.toString();
	}
}
