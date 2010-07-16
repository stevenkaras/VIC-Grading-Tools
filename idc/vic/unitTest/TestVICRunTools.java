package idc.vic.unitTest;

import static org.junit.Assert.*;

import idc.vic.Core;
import idc.vic.RunTools;
import idc.vic.VIC;

import java.util.Arrays;


import org.junit.Before;
import org.junit.Test;

public class TestVICRunTools {

	private int[] blankMemory;
	
	@Before
	public void setUp() throws Exception {
		blankMemory = new int[VIC.MEMORY_SIZE];
		for (int i = 0; i < blankMemory.length; i++)
			blankMemory[i] = 0;
		blankMemory[98] = 0;	// write-protected constant
		blankMemory[99] = 1;	// write-protected constant
	}

	@Test
	public void testLoadProgramProperSize() {
		int[] testProgram1 = { 0 };
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertEquals(VIC.MEMORY_SIZE, result.length);
	}
	@Test
	public void testLoadProgramSetsConstants() {
		int[] testProgram1 = { 0 };
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertEquals(0, result[98]);
		assertEquals(1, result[99]);
	}
	
	@Test
	public void testLoadProgramShortProgram() {
		int[] testProgram1 = { 151, 252, 353, 454, 555, 666, 777, 800, 900, 0 };
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertArrayEquals(testProgram1, Arrays.copyOfRange(result, 0, testProgram1.length));
	}
	
	@Test
	public void testLoadProgramVeryShortProgram() {
		int[] testProgram1 = { 0 };
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertArrayEquals(testProgram1, Arrays.copyOfRange(result, 0, testProgram1.length));
	}
	
	@Test
	public void testLoadProgramNoProgram() {
		int[] testProgram1 = { };
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertArrayEquals(blankMemory, result);
	}
	
	@Test
	public void testLoadProgramNull() {
		int[] testProgram1 = null;
		int[] result;
		
		result = RunTools.loadProgram(testProgram1);
		assertArrayEquals(blankMemory, result);
	}

	@Test
	public void testExtractOutputEmptyOutput() {
		// check using an empty machine
		Core vic = new Core();
		int[] result;
		result = RunTools.extractOutput(vic);
		assertNotNull(result);
		assertEquals(0, result.length);
	}
	//TODO: write more tests for ExtractOutput
}
