package idc.vic.testing.cli;

import idc.vic.testing.AutoTester;
import idc.vic.testing.VICTestFolder;
import idc.vic.testing.VICTestSuite;

import java.io.File;
import java.util.List;
import java.util.Map;

public class VICTester {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: VICTester <asm file or directory>");
			return;
		}
		File target = new File(args[0]);
		if (target.isDirectory())
			processDirectory(target);
		else if (target.isFile())
			System.out.println("Unsupported Operation: File ops not yet implemented");
		else
			System.out.println("Target '"+args[0]+"' does not exist");
	}

	private static void processDirectory(File root) {
		Map<String, VICTestSuite> tests = AutoTester.loadTestSuites(root);
		List<VICTestFolder> subfolders = AutoTester.loadSubdirectories(root, tests);
		for (VICTestFolder folder : subfolders) {
			System.out.println(folder.getFullReport(true));
		}
	}
}
