package idc.vic.unitTest;

import static org.junit.Assert.*;

import idc.vic.Assembler;
import idc.vic.VICParsingException;

import java.util.Map;

import org.junit.Test;

public class TestAssembler {

	private static final String testProgram1 = "\n" +
			"// Reads a series of numbers that ends with a 0.\n" +
			"// Write the positive numbers only.\n" +
			"LOOP:		read\n" +
			"			gotoz END\n" +
			"			gotop WRITEIT\n" +
			"			goto LOOP\n" +
			"WRITEIT:	write\n" +
			"			goto LOOP\n" +
			"END:		stop";
	private static final int[] testCode1 = {
		800, 606, 704, 500, 900, 500, 0 };

	private static final String testProgram2 = "" +
			"add 51\n" +
			"sub 52\n" +
			"load 53\n" +
			"store 54\n" +
			"goto 55\n" +
			"gotoz 66\n" +
			"gotop 77\n" +
			"read\n" +
			"write\n" +
			"stop\n";
	private static final int[] testCode2 = {
		151, 252, 353, 454, 555, 666, 777, 800, 900, 0 };

	private static final String testProgram3 = "" +
			"load ZERO\n" +
			"add ONE\n" +
			"add ONE\n" +
			"store x\n" +
			"read\n" +
			"store y\n" +
			"sub x\n" +
			"write\n" +
			"stop\n" +
			"\n";
	private static final int[] testCode3 = {
		398, 199, 199, 450, 800, 451, 250, 900, 0 };
	
	private static final String testProgram4 = "" +
			"LOOP:\n" +
			"add 51\n" +
			"END: sub 52\n" +
			"WriteIT:\tgoto LOOP\n" +
			"\n";
	private static final int[] testCode4 = {
		151, 252, 500 };
	
	//TODO: ask Neta-Lee for permission to use this
	private static final String testProgram5 = "" +
			"/*\n" +
			" * Exercise number   : 4.3\n" + 
			" * File Name         : find.txt\n" + 
			" * Name (First Last) : Neta-lee Simon\n" +
			" * Group number      : 4\n" +
			" *\n" +
			" */\n" +
			"\n" +
			"// This program is boolean program which reads a number and a series of numbers\n" +
			"// The program checks If the number exists in the series, if so -\n" + 
			"// the program writes 1 Otherwise it writes 0\n" +
			"\n" +
			"READ\n" +
			"STORE wally\n" +
			"READ\n" +
			"GOTOZ 9\n" +
			"\n" +
			"// Checks if the number is the same as Wally,\n" + 
			"// if so - stop searching and print if 1\n" +
			"SUB wally\n" +
			"GOTOZ 7\n" +
			"GOTO 2\n" +
			"LOAD 99\n" +
			"GOTO 10\n" +
			"\n" +
			"// If didn't find Wally and got 0 as an input - print o\n" +
			"LOAD 98\n" +
			"WRITE\n" +
			"\n" +
			"STOP\n";
	
	private static final int[] testCode5 = {
		800, 450, 800, 609, 250, 607, 502, 399, 510, 398, 900, 0};
	
	@Test
	public void testAssembleSimpleProgram() {
		try {
			int[] program = Assembler.assemble(testProgram2);
			assertArrayEquals(testCode2, program);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testAssembleTestProgram1() {
		try {
			int[] program = Assembler.assemble(testProgram1);
			assertArrayEquals(testCode1, program);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testAssembleBlankProgram() {
		try {
			int[] program = Assembler.assemble("");
			assertEquals(0, program.length);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testAssembleTestProgram3() {
		try {
			int[] program = Assembler.assemble(testProgram3);
			assertArrayEquals(testCode3, program);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testAssembleTestProgram4() {
		try {
			int[] program = Assembler.assemble(testProgram4);
			assertArrayEquals(testCode4, program);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProperVicProgram() {
		try {
			int[] program = Assembler.assemble(testProgram5);
			assertArrayEquals(testCode5, program);
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testPreprocess_DoesntCrash() {
		try {
			Assembler.preProcess(testProgram1);
		} catch (VICParsingException e) {
			fail();
		}
	}
	@Test
	public void testPreprocess_RemovesLineComments() {
		try {
			assertTrue(!Assembler.preProcess(testProgram1).contains("//"));
		} catch (VICParsingException e) {
			fail();
		}
	}
	@Test
	public void testPreprocess_RemovesBlockComments() {
		try {
			Assembler.preProcess(testProgram1);
		} catch (VICParsingException e) {
			fail();
		}
	}
	
	//TODO: write bad syntax checks for assemble(String)
	@Test
	public void testBuildSymbolsEmptyProgram() {
		try {
			Map<String, Integer> table = Assembler.buildSymbols("");
			assertTrue(table.containsKey("ZERO"));
			assertTrue(table.containsKey("ONE"));
			assertEquals(98, table.get("ZERO").intValue());
			assertEquals(99, table.get("ONE").intValue());
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testBuildSymbolsLabelsOnlyProgram() {
		try {
			Map<String, Integer> table = Assembler.buildSymbols(testProgram1);
			assertTrue(table.containsKey("LOOP"));
			assertEquals(0, table.get("LOOP").intValue());
			assertTrue(table.containsKey("END"));
			assertEquals(6, table.get("END").intValue());
			assertTrue(table.containsKey("WRITEIT"));
			assertEquals(4, table.get("WRITEIT").intValue());
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testBuildSymbolsNoSymbolsProgram() {
		try {
			Map<String, Integer> table = Assembler.buildSymbols(testProgram2);
			assertEquals(2, table.size());	// default constants ONE, ZERO
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testBuildSymbolsVarsOnlyProgram() {
		try {
			Map<String, Integer> table = Assembler.buildSymbols(testProgram3);
			assertTrue(table.containsKey("X"));
			assertTrue(table.containsKey("Y"));
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testBuildSymbolsCaseSensitiveProgram() {
		try {
			Map<String, Integer> table = Assembler.buildSymbols("load ZeRo");
			assertEquals(2, table.size());	// default constants ONE, ZERO
		} catch (VICParsingException e) {
			fail(e.getMessage());
		}
	}
	
	//TODO: write bad syntax tests for buildSymbols(String)
	@Test
	public void testIsNumberValidNumbers() {
		assertTrue(Assembler.isNumber("1"));
		assertTrue(Assembler.isNumber("0"));
		assertTrue(Assembler.isNumber("42"));
	}
	@Test
	public void testIsNumberNegativeNumbers() {
		assertTrue(Assembler.isNumber("-1"));
		assertTrue(Assembler.isNumber("-42"));
		assertTrue(Assembler.isNumber("-0"));
	}
	@Test
	public void testIsNumberFunkyNumbers1() {
		assertFalse(Assembler.isNumber("0x3f"));
	}
	@Test
	public void testIsNumberFunkyNumbers2() {
		assertFalse(Assembler.isNumber("3.2"));
	}
	@Test
	public void testIsNumberReallyLongNumber() {
		assertFalse(Assembler.isNumber("99999999999999999999999999999999999999999999999999999999999999999999999999999"));
	}
	@Test
	public void testIsInstructionProper() {
		assertTrue(Assembler.isInstruction("read"));
	}
	@Test
	public void testIsInstructionProperCase() {
		assertTrue(Assembler.isInstruction("ReAd"));
	}
	@Test
	public void testIsInstructionPrefix() {
		assertFalse(Assembler.isInstruction("rea"));
	}@Test
	public void testIsInstructionSuffix() {
		assertFalse(Assembler.isInstruction("otoz"));
	}@Test
	public void testIsInstructionAmbiguousPrefix() {
		assertFalse(Assembler.isInstruction("s"));
	}
	@Test
	public void testIsLineLabelProper() {
		assertTrue(Assembler.isLineLabel("Loop:"));
	}
	@Test
	public void testIsLineLabelShort() {
		assertTrue(Assembler.isLineLabel("L:"));
	}
	@Test
	public void testIsLineLabelEmpty() {
		assertFalse(Assembler.isLineLabel(":"));
	}
	@Test
	public void testIsLineLabelREAALLYYLONG() {
		assertTrue(Assembler.isLineLabel("Lalskjdflajsdfhkjhvlkhasdlflvoienzishefvinhzsdkjfvkznhsevz,sifhvnz,sonfmvzsdnfbznsdkfjzshndjfhvbzshfkvzsnkfdhzvskfhzs,hfdbvzs,dhf,nzoop:"));
	}
	@Test
	public void testIsLineLabelNoColon() {
		assertFalse(Assembler.isLineLabel("LOOP"));
	}
	@Test
	public void testIsLineLabelInstruction() {
		assertFalse(Assembler.isLineLabel("READ"));
	}
	@Test
	public void testIsLineLabelNotEnglishColon() {
		assertTrue(Assembler.isLineLabel("שזהו:"));
	}
	@Test
	public void testIsSymbol_ProperSingleLower() {
		assertTrue(Assembler.isSymbol("a"));
	}
	@Test
	public void testIsSymbol_ProperMultiLower() {
		assertTrue(Assembler.isSymbol("abase"));
	}
	@Test
	public void testIsSymbol_ProperSingleUpper() {
		assertTrue(Assembler.isSymbol("A"));
	}
	@Test
	public void testIsSymbol_ProperMultiMixed() {
		assertTrue(Assembler.isSymbol("aBAsE"));
	}
	@Test
	public void testIsSymbol_ProperMultiMixed2() {
		assertTrue(Assembler.isSymbol("ABAsE"));
	}
	@Test
	public void testIsSymbol_ProperMultiAlphaNum() {
		assertTrue(Assembler.isSymbol("aba123as"));
	}
	@Test
	public void testIsSymbol_ProperMultiAlphaNumScore() {
		assertTrue(Assembler.isSymbol("a_horse_of_course"));
	}
	@Test
	public void testIsSymbol_ProperScore() {
		assertTrue(Assembler.isSymbol("_"));
	}
	@Test
	public void testIsSymbol_ProperMultiScore() {
		assertTrue(Assembler.isSymbol("_x"));
	}
	@Test
	public void testIsSymbol_ImproperDigit() {
		assertFalse(Assembler.isSymbol("1"));
	}
	@Test
	public void testIsSymbol_ImproperPunc() {
		assertFalse(Assembler.isSymbol("a!"));
	}
	@Test
	public void testIsSymbol_ImproperSpaces() {
		assertFalse(Assembler.isSymbol("a horse of course"));
	}
}
