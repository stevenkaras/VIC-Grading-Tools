package idc.vic.testing;

import idc.vic.Assembler;
import idc.vic.Core;
import idc.vic.Runner;
import idc.vic.VIC;
import idc.vic.VICParsingException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Class for running a VIC program concurrently.
 * 
 * Note: the VIC used for running the program should not be used by any other
 * thread. It is the user's responsibility to ensure that each instance of
 * VICThread has its own VIC.
 * 
 * @author Steven Karas
 */
public class ProgramTest implements Callable<String> {
	private static final Class<? extends VIC> VICType = Core.class;
	private static List<VIC> runners;
	private static VIC getVIC() {
		if (runners == null)
			runners = new LinkedList<VIC>();
		// find an available runner
		synchronized (runners) {
			for (VIC vic : runners)
				if (!vic.isRunning())
					return vic;
		}
		// no available runners, so create a new one
		VIC newVIC = null;
		try {
			newVIC = VICType.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		//TODO: fix thread-safety issue (should maintain list of checked out runners, etc
		//runners.add(newVIC);	// create a new runner for this instance
		return newVIC;
	}
	
	private String program;
	private int[] code;
	private int[] input;
	private int[] expected;
	private boolean needToAssemble;
	
	/**
	 * Construct a new VICProgramTest task to run and compare a program
	 * @param vic a VIC VM
	 * @param code the program to run on the VM 
	 * @param input the input to provide to the program
	 * @param expected the output to compare it against
	 */
	public ProgramTest(int[] code, int[] input, int[] expected) {
		this.program = null;
		this.code = code;
		this.input = input;
		this.expected = expected;
		needToAssemble = false;
	}

	/**
	 * Construct a new VICProgramTest task to assemble, run, and compare a program
	 * @param vic a VIC VM
	 * @param program the program to run on the VM 
	 * @param input the input to provide to the program
	 * @param expected the output to compare it against
	 */
	public ProgramTest(String program, int[] input, int[] expected) {
		this.program = program;
		this.code = null;
		this.input = input;
		this.expected = expected;
		needToAssemble = true;
	}

	/**
	 * Construct a new VICProgramTest task to assemble, run, and compare a program
	 * @param vic a VIC VM
	 * @param code the program to run on the VM 
	 * @param test the test to build upon
	 */
	public ProgramTest(int[] code, VICTest test) {
		this(code, test.input, test.expected);
	}

	/**
	 * Construct a new VICProgramTest task to assemble, run, and compare a program
	 * @param vic a VIC VM
	 * @param program the program to run on the VM 
	 * @param test the test to build upon
	 */
	public ProgramTest(String program, VICTest test) {
		this(program, test.input, test.expected);
	}
	
	public String call() {
		if (needToAssemble) {
			try {
				code = Assembler.assemble(program);
			} catch (VICParsingException e) {
				return "Assembly Error: " + e.getMessage();
			}
		}
		VIC vic = getVIC();
		int[] actual = Runner.runProgram(vic, code, input);
		if (Arrays.equals(actual, Runner.TIMED_OUT))
			return "Program did not halt in alloted time. Likely infinite loop.";
		String compareResults = TestTools.compareOutput(actual, expected);
		if (vic.getBadStateReason() != null)
			compareResults += "\n" + vic.getBadStateReason();
		return compareResults;
	}
}
