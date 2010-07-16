package idc.vic.unitTest;

import idc.vic.RunTools;
import idc.vic.VIC;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Tests the VIC interface, based upon contracts. Can be used to quickly build
 * a test suite for a new implementation. (So long as it runs under similar
 * constraints (3 digit memory cells, etc).
 * 
 * @author Steven Karas
 */
public class TestVIC {

	public TestVIC() {
		setUp();
	}
	
	private int[] blankMemory;
	private int[] testProgram1;

	public void setUp() {
		blankMemory = new int[VIC.MEMORY_SIZE];
		for (int i = 0; i < blankMemory.length; i++)
			blankMemory[i] = 0;
		blankMemory[98] = 0;	// write-protected constant
		blankMemory[99] = 1;	// write-protected constant
		
		// initialize test program 1
		int[] temp = { 800, 606, 704, 500, 900, 500, 0};
		testProgram1 = RunTools.loadProgram(temp);
		
		// initialize further test programs here
	}
	public void testDumpsMemoryOK(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] mem = vic.memDump();
		assertArrayEquals(blankMemory, mem);
		assertNotSame(vic.memDump(), mem);
	}
	public void testLoadsMemoryOK(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.loadMemDump(testProgram1);
		// dump the loaded program
		int[] mem = vic.memDump();
		assertArrayEquals(testProgram1, mem);
	}
	public void testFetchWorksProperlyNormalCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.loadMemDump(testProgram1);
		vic.fetch();
		assertEquals(testProgram1[0], vic.getIr());
		assertEquals(1, vic.getPc());
	}
	public void testSTOPDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = {0};
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_STOP, 0};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testADDDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 151 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_ADD, 51};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testSUBDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 252 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_SUB, 52};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testLOADDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 353 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_LOAD, 53};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testSTOREDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 454 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_STORE, 54};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testGOTODecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 555 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_GOTO, 55};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testGOTOZDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 666 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_GOTOZ, 66};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testGOTOPDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 777 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_GOTOP, 77};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testREADDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 800 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_READ, 00};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testWRITEDecodeWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = { 900 };
		testProgram = RunTools.loadProgram(testProgram);
		vic.loadMemDump(testProgram);
		vic.fetch();
		int[] decode;
		int[] expected = {VIC.VIC_WRITE, 00};
		decode = vic.decode();
		assertArrayEquals(expected, decode);
	}
	public void testADDExecuteWorksProperlyNormalCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 50;
		vic.loadMemDump(testProgram);
		vic.setDr(0);
		vic.execute(VIC.VIC_ADD, 51);
		assertEquals(50, vic.getDr());
		ensureGoodState(vic);
	}
	public void testADDExecuteWorksProperlyNormalCase2(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 50;
		vic.loadMemDump(testProgram);
		vic.setDr(50);
		vic.execute(VIC.VIC_ADD, 51);
		assertEquals(100, vic.getDr());
		ensureGoodState(vic);
	}
	public void testADDExecuteWorksProperlyOverflowCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 900;
		vic.loadMemDump(testProgram);
		vic.setDr(900);
		vic.execute(VIC.VIC_ADD, 51);
		assertNotNull(vic.getBadStateReason());
	}
	public void testADDExecuteWorksProperlyUnderflowCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = -900;
		vic.loadMemDump(testProgram);
		vic.setDr(-900);
		vic.execute(VIC.VIC_ADD, 51);
		assertNotNull(vic.getBadStateReason());
	}
	public void testSUBExecuteWorksProperlyNormalCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 50;
		vic.loadMemDump(testProgram);
		vic.setDr(0);
		vic.execute(VIC.VIC_SUB, 51);
		assertEquals(-50, vic.getDr());
		ensureGoodState(vic);
	}
	public void testSUBExecuteWorksProperlyNormalCase2(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 50;
		vic.loadMemDump(testProgram);
		vic.setDr(90);
		vic.execute(VIC.VIC_SUB, 51);
		assertEquals(40, vic.getDr());
		ensureGoodState(vic);
	}
	public void testSUBExecuteWorksProperlyOverflowCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = -900;
		vic.loadMemDump(testProgram);
		vic.setDr(900);
		vic.execute(VIC.VIC_SUB, 51);
		assertNotNull(vic.getBadStateReason());
	}
	public void testSUBExecuteWorksProperlyUnderflowCase(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 900;
		vic.loadMemDump(testProgram);
		vic.setDr(-900);
		vic.execute(VIC.VIC_SUB, 51);
		assertNotNull(vic.getBadStateReason());
	}
	public void testLOADExecuteWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 42;
		vic.loadMemDump(testProgram);
		vic.setDr(0);
		vic.execute(VIC.VIC_LOAD, 51);
		assertEquals(42, vic.getDr());
	}
	public void testSTOREExecuteWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		int[] testProgram = blankMemory.clone();
		testProgram[51] = 42;
		vic.loadMemDump(testProgram);
		vic.setDr(41);
		vic.execute(VIC.VIC_STORE, 51);
		assertEquals(41, vic.memDump()[51]);
	}
	public void testSTOREExecuteWorksProperlyONE(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(41);
		vic.execute(VIC.VIC_STORE, 99);
		assertNotNull(vic.getBadStateReason());
	}
	public void testSTOREExecuteWorksProperlyZERO(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(41);
		vic.execute(VIC.VIC_STORE, 98);
		assertNotNull(vic.getBadStateReason());
	}
	public void testGOTOExecuteWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.execute(VIC.VIC_GOTO, 36);
		assertEquals(36, vic.getPc());
	}
	public void testGOTOZExecuteWorksProperlyZero(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(0);
		vic.execute(VIC.VIC_GOTOZ, 36);
		assertEquals(36, vic.getPc());
	}
	public void testGOTOZExecuteWorksProperlyPositive(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(42);
		vic.execute(VIC.VIC_GOTOZ, 36);
		assertEquals(0, vic.getPc());
	}
	public void testGOTOZExecuteWorksProperlyNegative(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(-42);
		vic.execute(VIC.VIC_GOTOZ, 36);
		assertEquals(0, vic.getPc());
	}
	public void testGOTOPExecuteWorksProperlyPositive(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(42);
		vic.execute(VIC.VIC_GOTOP, 36);
		assertEquals(36, vic.getPc());
	}
	public void testGOTOPExecuteWorksProperlyZero(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(0);
		vic.execute(VIC.VIC_GOTOP, 36);
		assertEquals(0, vic.getPc());
	}
	public void testGOTOPExecuteWorksProperlyNegative(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(-42);
		vic.execute(VIC.VIC_GOTOP, 36);
		assertEquals(0, vic.getPc());
	}
	public void testREADExecuteWorksProperlyNormal(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(0);
		vic.addInput(42);
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(42, vic.getDr());
	}
	public void testREADExecuteWorksProperlyNoInput(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.execute(VIC.VIC_READ, 0);
		assertNotNull(vic.getBadStateReason());
	}
	public void testWRITEExecuteWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		try {
			vic.readOutput();
			fail("Output should be empty before starting test");
		} catch (NoSuchElementException e) {
			// supposed to fail
		}
		vic.setDr(42);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(42, vic.readOutput());
	}
	public void testBadStateActuallyStopsSomethingFetch(TestableVIC vic) {
		// quickest way to get into bad state is to overwrite ZERO
		vic.reset(); // ensure we have a clean machine
		vic.execute(VIC.VIC_STORE, 98);
		assertNotNull(vic.getBadStateReason());
		
		// the actual test
		int oldPC = vic.getPc();
		int oldIR = vic.getIr();
		vic.fetch();
		assertEquals(oldPC, vic.getPc());
		assertEquals(oldIR, vic.getIr());
	}
	public void testBadStateActuallyStopsSomethingDecode(TestableVIC vic) {
		// quickest way to get into bad state is to overwrite ZERO
		vic.reset(); // ensure we have a clean machine
		vic.execute(VIC.VIC_STORE, 98);
		assertNotNull(vic.getBadStateReason());
		
		// the actual test
		int[] result = vic.decode();
		assertNull(result);
	}
	public void testBadStateActuallyStopsSomethingExecute(TestableVIC vic) {
		// quickest way to get into bad state is to overwrite ZERO
		vic.reset(); // ensure we have a clean machine
		vic.execute(VIC.VIC_STORE, 98);
		assertNotNull(vic.getBadStateReason());
		
		// the actual test
		vic.setDr(42);
		vic.execute(VIC.VIC_ADD, 99); // try incrementing
		assertEquals(42, vic.getDr());
	}
	public void testAddInputWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.addInput(42);
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(42, vic.getDr());
	}
	public void testAddInputWorksProperlyQueueStyle(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.addInput(41);
		vic.addInput(42);
		vic.addInput(43);
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(41, vic.getDr());
	}
	public void testAddInputWorksProperlyMultipleUses(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.addInput(41);
		vic.addInput(42);
		vic.addInput(43);
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(41, vic.getDr());
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(42, vic.getDr());
		vic.execute(VIC.VIC_READ, 0);
		assertEquals(43, vic.getDr());
	}
	public void testAddInputWorksProperlyInvalidInput1(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.addInput(1000);
		vic.execute(VIC.VIC_READ, 0);
		assertNotNull(vic.getBadStateReason());
	}
	public void testAddInputWorksProperlyInvalidInput2(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.addInput(-1000);
		vic.execute(VIC.VIC_READ, 0);
		assertNotNull(vic.getBadStateReason());
	}
	public void testReadOutputWorksProperly(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(42);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(42, vic.readOutput());
	}
	public void testReadOutputWorksProperlyQueueStyle(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(41);
		vic.execute(VIC.VIC_WRITE, 0);
		vic.setDr(42);
		vic.execute(VIC.VIC_WRITE, 0);
		vic.setDr(43);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(41, vic.readOutput());
		assertEquals(42, vic.readOutput());
		assertEquals(43, vic.readOutput());
	}
	public void testReadOutputWorksProperlyMultipleUses(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		vic.setDr(41);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(41, vic.readOutput());
		vic.setDr(42);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(42, vic.readOutput());
		vic.setDr(43);
		vic.execute(VIC.VIC_WRITE, 0);
		assertEquals(43, vic.readOutput());
	}
	public void testReadOutputWorksProperlyNoOutput(TestableVIC vic) {
		vic.reset(); // ensure we have a clean machine
		try {
			vic.readOutput();
			fail("Expected Exception. readOutput should throw an exception when empty");
		} catch (NoSuchElementException e) {
			// passes the test
		}
	}
	public void testComplexProgram(TestableVIC vic) {
		int[] program = { 800, 606, 704, 500, 900, 500, 0 };
		program = RunTools.loadProgram(program);
		vic.loadMemDump(program);
		int[] input = {1, 2, 3, -4, 5, 6, 7, -8, 0};
		for (int data : input)
			vic.addInput(data);
		vic.run();
		int[] output = RunTools.extractOutput(vic);
		int[] expected = {1, 2, 3, 5, 6, 7};
		assertArrayEquals(expected, output);
	}
	
	private void ensureGoodState(TestableVIC vic) {
		if (vic.getBadStateReason() != null)
			fail(vic.getBadStateReason());
	}
}
