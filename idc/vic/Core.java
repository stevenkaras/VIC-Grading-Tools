package idc.vic;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Efficient implementation of VIC computer model
 * 
 * @author Steven Karas
 */
public class Core implements VIC {
	private static final int DIGITS = 3;
	
	private int[] mem;
	private int ir;
	private int dr;
	private int pc;
	private LinkedList<Integer> stdin;
	private LinkedList<Integer> stdout;
	private String badState;
	private boolean running;

	/**
	 * Constructs a new VIC object with default state
	 */
	public Core() {
		stdin = new LinkedList<Integer>();
		stdout = new LinkedList<Integer>();
		mem = new int[MEMORY_SIZE];
		reset();
		resetMemory();
		flushIO();
	}
	
	public void reset() {
		pc = PROGRAM_INSERTION_POINT;
		ir = DEFAULT_DATA;
		dr = DEFAULT_DATA;
		badState = null;
		running = false;
	}
	
	private void resetMemory() {
		Arrays.fill(mem, 0);
		mem[98] = 0;	// write protected constants ZERO & ONE
		mem[99] = 1;
	}

	private boolean inBadState() {
		return badState != null;
	}

	public void fetch() {
		if (inBadState())
			return;
		ir = mem[pc];
		pc += INSTRUCTION_SIZE;
	}
	
	public int[] decode() {
		if (inBadState())
			return null;
		if (!isProperInstruction(ir)) {
			badState("Malformed Instruction at " + pc + ":" + ir);
			return null;
		}
		// extract the nth digit of the IR, and dispatch it with the rest of the digits
		int decode[] = new int[2];
		decode[COMMAND] = extractDigitN(ir, DIGITS);
		decode[ADDRESS] = ir % (int) (Math.pow(10, DIGITS - 1));
		return decode;
	}
	
	private boolean isProperInstruction(int ir) {
		return MAX_INT >= ir && ir >= 0;
	}

	private int extractDigitN(int data, int n) {
		return (data / (int) (Math.pow(10, n - 1))) % 10;
	}

	public void execute(int command, int address) {
		if (inBadState())
			return;
		switch (command) {
		case VIC_STOP:
			running = false;
			break;
		case VIC_ADD:
			if (!isValidData(dr + mem[address]))
				badState("Integer Overflow at " + pc + ":" + ir);
			dr += mem[address];
			break;
		case VIC_SUB:
			if (!isValidData(dr - mem[address]))
				badState("Integer underflow at " + pc + ":" + ir);
			dr -= mem[address];
			break;
		case VIC_LOAD:
			dr = mem[address];
			break;
		case VIC_STORE:
			if (address == 98 || address == 99)
				badState("Memory address "+address+" is write protected at "+pc+":"+ir);
			mem[address] = dr;
			break;
		case VIC_GOTO:
			pc = address;
			break;
		case VIC_GOTOZ:
			if (dr == 0) {
				pc = address;
			}
			break;
		case VIC_GOTOP:
			if (dr > 0) {
				pc = address;
			}
			break;
		case VIC_READ:
			dr = readInput();
			break;
		case VIC_WRITE:
			stdout.add(dr);
			break;
		default:
			badState("Bad VIC instruction: " + command + address);
		}
	}
	
	private int readInput() {
		if (!stdin.isEmpty())
			return stdin.remove();
		else {
			badState("No more input at " + pc);
			return DEFAULT_DATA;
		}
	}

	private void badState(String reason) {
		running = false;
		badState = reason;
	}

	public String getBadStateReason() {
		return this.badState;
	}
	
	public void step() {
		if (inBadState())
			return;
		fetch();
		int decode[] = decode();
		execute(decode[COMMAND], decode[ADDRESS]);
	}
	
	public void run() {
		running = true;
		while (running && !inBadState()) {
			step();
		}
	}
	
	public void addInput(int data) {
		if (isValidData(data))
			stdin.add(data);
	}
	
	private boolean isValidData(int data) {
		return MAX_INT >= data && data >= MIN_INT;
	}

	public int readOutput() {
		return stdout.remove();
	}
	
	public int[] memDump() {
		return mem.clone();
	}
	
	public void loadMemDump(int memDump[]) {
		if (memDump.length != MEMORY_SIZE)
			return; // not a proper memdump
		for (int i = 0; i < MEMORY_SIZE; i++)
			if (!isValidData(memDump[i])) {
				return; // not a proper memdump
			}
		// NOTE: ignores the last 2 cells in the mem-dump since they are
		// supposed to be constant, write protected cells.
		System.arraycopy(memDump, 0, mem, 0, MEMORY_SIZE - 2);
	}

	public int getIr() {
		return ir;
	}

	public int getDr() {
		return dr;
	}

	public int getPc() {
		return pc;
	}

	public void flushIO() {
		stdin.clear();
		stdout.clear();
	}

	public boolean isRunning() {
		return running;
	}
	
	protected void setDr(int dr) {
		this.dr = dr;
	}
}
