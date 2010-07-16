package idc.vic.testing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class TestTools {

	/**
	 * Deboxes a collection of integers.
	 * NOTE: this is not done automatically by Java, and is needed because
	 * Java doesn't support arrays of generics, since generics aren't available
	 * at runtime.
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

	/**
	 * Included as a public constant to allow for easy JUnit testing
	 */
	public static final String PASSED = "Passed Test.";
	/**
	 * Compares two arrays to each other, and provides a useful analysis result
	 * in English.
	 * 
	 * @param actual
	 * @param expected
	 * @return an analysis of whether Arrays.equals(actual, expected) is true
	 */
	public static String compareOutput(int[] actual, int[] expected) {
		if (expected == null && actual == null)
			return PASSED;
		if (expected == null)
			return "Unexpected output: " + actual.toString();
		if (actual == null)
			return "Expected output, but was none.";
		int i, j;
		for(i = 0, j = 0; i < actual.length && j < expected.length; i++, j++) {
			if (actual[i] != expected[j])
				return "Expected:"+expected[j]+" but was really:"+actual[i]+" at position:"+i;
		}
		if (actual.length > expected.length)
			return "Extra output after end of expected:"+Arrays.copyOfRange(actual, i, actual.length);
		if (actual.length < expected.length)
			return "Expected further output, but was none";
		return PASSED;
	}
}
