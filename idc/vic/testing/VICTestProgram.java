package idc.vic.testing;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//TODO: write javadoc
public class VICTestProgram {
	public String programName;
	private String sourceCode;
	public Map<String, Future<String>> futures = new LinkedHashMap<String, Future<String>>();

	//TODO: write javadoc
	public VICTestProgram(File source) {
		this.programName = source.getName();
		this.setSourceCode(FileTools.readTextFile(source));
	}

	private void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	//TODO: write javadoc
	public String getReport(boolean suppressPassed) {
		StringBuilder buffer = new StringBuilder(futures.size() * 20); // avg 20 chars/test
		buffer.append("Report for sourceCode ");
		buffer.append(programName);
		buffer.append('\n');
		for (Entry<String, Future<String>> future : futures.entrySet()) {
			String result = null;
			try {
				result = future.getValue().get();
			} catch (InterruptedException e) {
				throw new IllegalStateException(e.getMessage(), e);
			} catch (ExecutionException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
			if (result.startsWith("Assembly Error: ")) {
				buffer.append(result);
				break;	// print it once.
			}
			if (suppressPassed && result.equals(TestTools.PASSED))
				continue;
			buffer.append("Test ");
			buffer.append(future.getKey());
			buffer.append(" returned :");
			buffer.append(result);
			buffer.append('\n');
		}
		return buffer.toString();
	}

	public String getSourceCode() {
		return sourceCode;
	}
}
