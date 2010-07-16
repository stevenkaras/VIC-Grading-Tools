package idc.vic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TODO: replace this class with generated parser
//TODO: ask Shimon about label vs. variable symbol precedence
/**
 * An assembler for VIC programs. Allows use of line labels in prefix
 * positions, and data variables. Note that this assembler is case-insensitive
 * with respect both to variables/labels and instructions
 * <p><code>
 * the grammar is as follows:<br>
 * IL := [BC][line-label]( [BC]|[BC] )[instruction][BC][comment]<br>
 * line-label := symbol: <br>
 * instruction := (1-typeOP|2-typeOP)<br>
 * 1-typeOP := (READ|WRITE|STOP)<br>
 * 2-typeOP := (ADD|SUB|LOAD|STORE|GOTO|GOTOZ|GOTOP)[BC] address<br>
 * address := (0-99|symbol)<br>
 * symbol := [a-zA-Z][\w]*<br>
 * comment := (//|;|#|'|`).*$<br>
 * BC := /*.** /<br>
 * </code><p>
 * Note that while normally commands need to be on the same line, a block comment can be used to 
 * splice several lines together, since they inherently span several lines. The only way to prevent
 * this would be to change the grammar such that block comments prohibit the appearance of other
 * elements on the same line.
 * <p>
 * Known issues:<br>
 * The assembler currently removes comments using a preprocessor, which destroys information
 * about line numbers. Thus, the line number is the instruction line number (multiline comments are
 * a pain!)
 * 
 * @author Steven Karas
 */
public class Assembler {
	/**
	 * Constants that represent the VIC assembly instructions.
	 * Included to aid with type-safety and reduce typo-caused bugs
	 */
	public static final String VIC_ADD = "ADD";
	public static final String VIC_SUB = "SUB";
	public static final String VIC_LOAD = "LOAD";
	public static final String VIC_STORE = "STORE";
	public static final String VIC_GOTO = "GOTO";
	public static final String VIC_GOTOZ = "GOTOZ";
	public static final String VIC_GOTOP = "GOTOP";
	public static final String VIC_READ = "READ";
	public static final String VIC_WRITE = "WRITE";
	public static final String VIC_STOP = "STOP";
	private static Map<String, Integer> assemblyTable;
	
	private static Map<String, Integer> getAssemblyTable() {
		if (assemblyTable == null) {
			assemblyTable = new HashMap<String, Integer>();
			assemblyTable.put(VIC_ADD, VIC.VIC_ADD);
			assemblyTable.put(VIC_SUB, VIC.VIC_SUB);
			assemblyTable.put(VIC_LOAD, VIC.VIC_LOAD);
			assemblyTable.put(VIC_STORE, VIC.VIC_STORE);
			assemblyTable.put(VIC_GOTO, VIC.VIC_GOTO);
			assemblyTable.put(VIC_GOTOZ, VIC.VIC_GOTOZ);
			assemblyTable.put(VIC_GOTOP, VIC.VIC_GOTOP);
			assemblyTable.put(VIC_READ, VIC.VIC_READ);
			assemblyTable.put(VIC_WRITE, VIC.VIC_WRITE);
			assemblyTable.put(VIC_STOP, VIC.VIC_STOP);
		}
		return assemblyTable;
	}
	
	/**
	 * Assembles a VIC assembly program. A program must have proper syntax, or
	 * an {@link VICParsingException} will be thrown.
	 * 
	 * @param program the program to assemble
	 * @return an opcode array
	 * @throws VICParsingException if the program doesn't have proper syntax
	 */
	public static int[] assemble(String program) throws VICParsingException {
		if (program == null) return null;
		program = preProcess(program);
		Map<String, Integer> symTable = buildSymTable(program);
		return translateCommands(program, symTable);
	}

	//TODO: change this to tokenize also, so line number info can be saved
	public static String preProcess(String text) throws VICParsingException {
		text = text.replaceAll("(?ms)\\/\\*.*?\\*\\/", "");
		text = text.replaceAll("(?m)\\/\\/.*$", "");
		text = text.replaceAll("(?m)#.*$", "");
		text = text.replaceAll("(?m)'.*$", "");
		text = text.replaceAll("(?m)`.*$", "");
		return text;
	}
	
	/*
	 * Assumes a proper vic file
	 */
	private static int[] translateCommands(String program, Map<String, Integer> symTable) throws VICParsingException {
		ArrayList<Integer> code = new ArrayList<Integer>();
		Scanner scan = new Scanner(program);
		int lineNumber = 0;
		int opNumber = 0;
		while (scan.hasNextLine()) {
			lineNumber++;
			String line = scan.nextLine();
			Scanner tokenizer = new Scanner(line);
			if (!tokenizer.hasNext())
				continue;	// blank line
			String token = tokenizer.next();
			// IL position 2
			if (isLineLabel(token)) {
				if (!tokenizer.hasNext())
					continue;
				token = tokenizer.next();
			}
			// IL position 4
			if (isInstruction(token)) {
				token = token.toUpperCase();
				Integer opCode = getAssemblyTable().get(token);
				Integer address = 0;
				if (!token.equals(VIC_STOP) && !token.equals(VIC_READ) && !token.equals(VIC_WRITE)) {
					token = tokenizer.next();
					if (!isNumber(token)) {
						token = token.toUpperCase();
						address = symTable.get(token); 
					} else {
						address = Integer.parseInt(token);
					}
				}
				code.add(opCode * 100 + address);
				opNumber++;
			}
		}
		return RunTools.deboxInteger(code);
	}

	/**
	 * Constructs a symbol table for the given program. There are two default
	 * symbols for every VIC program:<br>
	 * ZERO - maps to memory location 98<br>
	 * ONE - maps to memory location 99
	 * <p>
	 * Note: performs a syntax check on the file, throwing a
	 * {@link VICParsingException} if the file is invalid
	 * 
	 * @param program the program to process
	 * @return a symbol table 
	 * @throws VICParsingException if the program is not a proper VIC program
	 */
	public static Map<String, Integer> buildSymbols(String program) throws VICParsingException {
		return buildSymTable(preProcess(program));
	}
	
	private static Map<String, Integer> buildSymTable(String program) throws VICParsingException { 
		/*
		 * This is the first pass of a two-pass assembly process. This pass is responsible for both
		 * building the symbol table for later use, and performing a syntax check on the code.
		 * 
		 * As a result, this method is extremely long. Extremely.
		 */
		Map<String, Integer> lineLabels = new HashMap<String, Integer>();
		Map<String, Integer> variables = new HashMap<String, Integer>();
		variables.put("ZERO", 98);
		variables.put("ONE", 99);
		Scanner scan = new Scanner(program);
		int nextVariable = 50;
		int lineNumber = 0;
		int opNumber = 0;
		while (scan.hasNextLine()) {
			lineNumber++;
			String line = scan.nextLine();
			Scanner tokenizer = new Scanner(line);
			if (!tokenizer.hasNext())
				continue;
			String token = tokenizer.next();
			// IL position 2
			if (isLineLabel(token)) {
				token = token.substring(0, token.length() - 1);
				if (lineLabels.get(token) != null)
					throw new VICParsingException("Duplicate line label "+token+" encountered on line "+lineNumber);
				lineLabels.put(token, opNumber);
				if (!tokenizer.hasNext())
					continue;
				token = tokenizer.next();
			}
			// IL position 4
			if (isInstruction(token)) {
				token = token.toUpperCase();
				if (!token.equals(VIC_STOP) && !token.equals(VIC_READ) && !token.equals(VIC_WRITE)) {
					if (!tokenizer.hasNext())
						throw new VICParsingException("Unexpected end of line on line "+lineNumber);
					token = tokenizer.next();
					if (!isNumber(token)) {
						token = token.toUpperCase();
						if (!variables.containsKey(token))
							variables.put(token, nextVariable++);
					} else {
						int address = Integer.parseInt(token);
						if (address > 99 || address < 0)
							throw new VICParsingException("Instruction addresses must be at most two digits");
					}
				}
				opNumber++;
				if (!tokenizer.hasNext())
					continue;
				token = tokenizer.next();
			}
			throw new VICParsingException("Expected end of line on line "+lineNumber);
		}
		if (opNumber > 49)
			throw new VICParsingException("Too many operations. Programs must have fewer than 50 instructions");
		if (nextVariable >= 98)
			throw new VICParsingException("Too many variables in program. Must be less than 48");
		
		variables.putAll(lineLabels);	// line labels should have precedent over variables
		return variables;
	}

	/**
	 * Tests if token is an integer
	 * 
	 * @param token
	 * @return true iff token is the string representation of an integer
	 */
	public static boolean isNumber(String token) {
		try {
			Integer.parseInt(token);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Tests if the token is a valid VIC assembly instruction
	 * 
	 * @param token
	 * @return true iff token is a valid VIC assembly instruction
	 */
	public static boolean isInstruction(String token) {
		return token != null && getAssemblyTable().containsKey(token.toUpperCase());
	}
	
	/**
	 * Tests if the token is a line label
	 * 
	 * @param token
	 * @return true iff token is a line label
	 */
	public static boolean isLineLabel(String token) {
		return token != null && token.endsWith(":") && token.length() > 1;
	}
	
	/**
	 * Tests if the token is a symbol
	 * 
	 * @param token
	 * @return true iff token is a symbol
	 */
	public static boolean isSymbol(String token) {
		return token.matches("^[a-zA-Z_][\\w]*$");
	}
}
