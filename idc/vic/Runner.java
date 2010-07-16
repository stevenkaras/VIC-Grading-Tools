package idc.vic;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Utility class for running VIC programs and analyzing their output
 * 
 * @author Steven Karas
 */
public class Runner {
	/**
	 * Special output returned if a program timed out
	 */
	public static final int[] TIMED_OUT = {1000};
	private static final long RUNNER_TIMEOUT = 10000;

	/**
	 * Run the provided program with the provided input on the provided VIC VM.
	 * 
	 * @param vic
	 * @param program
	 * @param input
	 * @return the output of the program
	 */
	public static int[] runProgram(VIC vic, int[] program, int[] input) {
		// prep the program to be loaded
		program = RunTools.loadProgram(program);
		// reset the VIC machine and load the program
		vic.reset();
		vic.flushIO();
		vic.loadMemDump(program);
		// load the provided input
		for (int data : input)
			vic.addInput(data);
		// run the program
		Future<?> programRunner = Executors.newSingleThreadExecutor().submit(vic);
		try {
			programRunner.get(RUNNER_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (ExecutionException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} catch (TimeoutException e) {
			programRunner.cancel(true);
			return TIMED_OUT.clone();
		}
		return RunTools.extractOutput(vic);
	}
}
