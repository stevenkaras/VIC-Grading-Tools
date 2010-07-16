package idc.vic.unitTest;


import org.junit.Before;
import org.junit.Test;

/**
 * @author Steven Karas
 */
public class TestCore {

	private TestVIC tester;
	
	@Before
	public void setUp() throws Exception {
		tester = new TestVIC();
	}
	
	@Test
	public void testInitializesCorrectly() {
		@SuppressWarnings("unused")
		TestableCore vic = new TestableCore();
	}
	
	@Test
	public void testDumpsMemoryOK() {
		TestableCore vic = new TestableCore();
		tester.testDumpsMemoryOK(vic);
	}
	@Test
	public void testLoadsMemoryOK() {
		TestableCore vic = new TestableCore();
		tester.testLoadsMemoryOK(vic);
	}
	@Test
	public void testFetchWorksProperlyNormalCase() {
		TestableCore vic = new TestableCore();
		tester.testFetchWorksProperlyNormalCase(vic);
	}
	@Test
	public void testSTOPDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testSTOPDecodeWorksProperly(vic);
	}
	@Test
	public void testADDDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testADDDecodeWorksProperly(vic);
	}
	@Test
	public void testSUBDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testSUBDecodeWorksProperly(vic);
	}
	@Test
	public void testLOADDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testLOADDecodeWorksProperly(vic);
	}
	@Test
	public void testSTOREDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testSTOREDecodeWorksProperly(vic);
	}
	@Test
	public void testGOTODecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testGOTODecodeWorksProperly(vic);
	}
	@Test
	public void testGOTOZDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testGOTODecodeWorksProperly(vic);
	}
	@Test
	public void testGOTOPDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testGOTOPDecodeWorksProperly(vic);
	}
	@Test
	public void testREADDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testREADDecodeWorksProperly(vic);
	}
	@Test
	public void testWRITEDecodeWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testWRITEDecodeWorksProperly(vic);
	}
	@Test
	public void testADDExecuteWorksProperlyNormalCase() {
		TestableCore vic = new TestableCore();
		tester.testADDExecuteWorksProperlyNormalCase(vic);
	}
	@Test
	public void testADDExecuteWorksProperlyNormalCase2() {
		TestableCore vic = new TestableCore();
		tester.testADDExecuteWorksProperlyNormalCase2(vic);
	}
	@Test
	public void testADDExecuteWorksProperlyOverflowCase() {
		TestableCore vic = new TestableCore();
		tester.testADDExecuteWorksProperlyOverflowCase(vic);
	}
	@Test
	public void testADDExecuteWorksProperlyUnderflowCase() {
		TestableCore vic = new TestableCore();
		tester.testADDExecuteWorksProperlyUnderflowCase(vic);
	}
	@Test
	public void testSUBExecuteWorksProperlyNormalCase() {
		TestableCore vic = new TestableCore();
		tester.testSUBExecuteWorksProperlyNormalCase(vic);
	}
	@Test
	public void testSUBExecuteWorksProperlyNormalCase2() {
		TestableCore vic = new TestableCore();
		tester.testSUBExecuteWorksProperlyNormalCase2(vic);
	}
	@Test
	public void testSUBExecuteWorksProperlyOverflowCase() {
		TestableCore vic = new TestableCore();
		tester.testSUBExecuteWorksProperlyOverflowCase(vic);
	}
	@Test
	public void testSUBExecuteWorksProperlyUnderflowCase() {
		TestableCore vic = new TestableCore();
		tester.testSUBExecuteWorksProperlyUnderflowCase(vic);
	}
	@Test
	public void testLOADExecuteWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testLOADExecuteWorksProperly(vic);
	}
	@Test
	public void testSTOREExecuteWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testSTOREExecuteWorksProperly(vic);
	}
	@Test
	public void testSTOREExecuteWorksProperlyONE() {
		TestableCore vic = new TestableCore();
		tester.testSTOREExecuteWorksProperlyONE(vic);
	}
	@Test
	public void testSTOREExecuteWorksProperlyZERO() {
		TestableCore vic = new TestableCore();
		tester.testSTOREExecuteWorksProperlyZERO(vic);
	}
	@Test
	public void testGOTOExecuteWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testGOTOExecuteWorksProperly(vic);
	}
	@Test
	public void testGOTOZExecuteWorksProperlyZero() {
		TestableCore vic = new TestableCore();
		tester.testGOTOZExecuteWorksProperlyZero(vic);
	}
	@Test
	public void testGOTOZExecuteWorksProperlyPositive() {
		TestableCore vic = new TestableCore();
		tester.testGOTOZExecuteWorksProperlyPositive(vic);
	}
	@Test
	public void testGOTOZExecuteWorksProperlyNegative() {
		TestableCore vic = new TestableCore();
		tester.testGOTOZExecuteWorksProperlyNegative(vic);
	}
	@Test
	public void testGOTOPExecuteWorksProperlyPositive() {
		TestableCore vic = new TestableCore();
		tester.testGOTOPExecuteWorksProperlyPositive(vic);
	}
	@Test
	public void testGOTOPExecuteWorksProperlyZero() {
		TestableCore vic = new TestableCore();
		tester.testGOTOPExecuteWorksProperlyZero(vic);
	}
	@Test
	public void testGOTOPExecuteWorksProperlyNegative() {
		TestableCore vic = new TestableCore();
		tester.testGOTOPExecuteWorksProperlyNegative(vic);
	}
	@Test
	public void testREADExecuteWorksProperlyNormal() {
		TestableCore vic = new TestableCore();
		tester.testREADExecuteWorksProperlyNormal(vic);
	}
	@Test
	public void testREADExecuteWorksProperlyNoInput() {
		TestableCore vic = new TestableCore();
		tester.testREADExecuteWorksProperlyNoInput(vic);
	}
	@Test
	public void testWRITEExecuteWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testWRITEExecuteWorksProperly(vic);
	}
	@Test
	public void testBadStateActuallyStopsSomethingFetch() {
		TestableCore vic = new TestableCore();
		tester.testBadStateActuallyStopsSomethingFetch(vic);
	}
	@Test
	public void testBadStateActuallyStopsSomethingDecode() {
		TestableCore vic = new TestableCore();
		tester.testBadStateActuallyStopsSomethingDecode(vic);
	}
	@Test
	public void testBadStateActuallyStopsSomethingExecute() {
		TestableCore vic = new TestableCore();
		tester.testBadStateActuallyStopsSomethingExecute(vic);
	}
	@Test
	public void testAddInputWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testAddInputWorksProperly(vic);
	}
	@Test
	public void testAddInputWorksProperlyQueueStyle() {
		TestableCore vic = new TestableCore();
		tester.testAddInputWorksProperlyQueueStyle(vic);
	}
	@Test
	public void testAddInputWorksProperlyMultipleUses() {
		TestableCore vic = new TestableCore();
		tester.testAddInputWorksProperlyMultipleUses(vic);
	}
	@Test
	public void testAddInputWorksProperlyInvalidInput1() {
		TestableCore vic = new TestableCore();
		tester.testAddInputWorksProperlyInvalidInput1(vic);
	}
	@Test
	public void testAddInputWorksProperlyInvalidInput2() {
		TestableCore vic = new TestableCore();
		tester.testAddInputWorksProperlyInvalidInput2(vic);
	}
	@Test
	public void testReadOutputWorksProperly() {
		TestableCore vic = new TestableCore();
		tester.testReadOutputWorksProperly(vic);
	}
	@Test
	public void testReadOutputWorksProperlyQueueStyle() {
		TestableCore vic = new TestableCore();
		tester.testReadOutputWorksProperlyQueueStyle(vic);
	}
	@Test
	public void testReadOutputWorksProperlyMultipleUses() {
		TestableCore vic = new TestableCore();
		tester.testReadOutputWorksProperlyMultipleUses(vic);
	}
	@Test
	public void testReadOutputWorksProperlyNoOutput() {
		TestableCore vic = new TestableCore();
		tester.testReadOutputWorksProperlyNoOutput(vic);
	}
	@Test
	public void testComplexProgram() {
		TestableCore vic = new TestableCore();
		tester.testComplexProgram(vic);
	}
}
