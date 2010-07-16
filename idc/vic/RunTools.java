package idc.vic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Utility class with useful methods for running VIC programs
 * 
 * @author Steven Karas
 */
public class RunTools {
	private static int[] blankMemory;
	
	/**
	 * Load a VIC program into a memory dump.
	 * 
	 * @param program the program to load
	 * @return a memory dump with the program loaded
	 */
	public static int[] loadProgram(int[] program) {
		if (program == null)
			return getBlankMemory().clone();
		if (program.length == VIC.MEMORY_SIZE)
			return program;	// no need to load it into a memory dump
		if (program.length > VIC.MEMORY_SIZE)
			return null;
		int[] newProgram = getBlankMemory().clone();
		System.arraycopy(program, 0, newProgram, 0, program.length);
		return newProgram;
	}
	
	/**
	 * Extract the output of a VIC VM into an int array
	 * 
	 * @param vic a VIC VM
	 * @return an int array with the contents of the output queue
	 */
	public static int[] extractOutput(VIC vic) {
		LinkedList<Integer> outputList = new LinkedList<Integer>();
		while (true) {
			try {
				outputList.add(vic.readOutput());
			} catch (NoSuchElementException e) {
				// finished reading output
				break;
			}
		}
		return deboxInteger(outputList);
	}
	
	private static int[] getBlankMemory() {
		if (blankMemory == null) {
			blankMemory = new int[VIC.MEMORY_SIZE];
			Arrays.fill(blankMemory, 0);
			blankMemory[98] = 0;	// write-protected constant
			blankMemory[99] = 1;	// write-protected constant
		}
		return blankMemory;
	}
	
	/**
	 * Deboxes a collection of integers.
	 * NOTE: this is not done automatically by Java, and is needed because
	 * Java doesn't support arrays of generics, since generics aren't available
	 * at runtime, and also doesn't support collections of primitives.
	 * 
	 * @param a The collection to debox into an array
	 * @return a deboxed integer array
	 */
	static int[] deboxInteger(Collection<Integer> a) {
		if (a == null) return null;
		int[] result = new int[a.size()];
		Iterator<Integer> iter = a.iterator();
		for (int i = 0; i < result.length; i++) {
			result[i] = iter.next();
		}
		return result;
	}
}
