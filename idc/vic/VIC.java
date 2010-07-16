package idc.vic;

/**
 * A virtual machine model of the VIC computer model.
 * <p>
 * The VIC computer model was created by Prof. Shimon Schocken and Yotam Harchol
 * at the IDC Herzliyah.
 * 
 * @author Steven Karas
 */
public interface VIC extends Runnable {
	public static final int DEFAULT_DATA = 0;
	public static final int MIN_INT = -999;
	public static final int MAX_INT = 999;
	public static final int MEMORY_SIZE = 100;
	public static final int PROGRAM_INSERTION_POINT = 0;
	public static final int INSTRUCTION_SIZE = 1;
	
	public static final int COMMAND = 0;
	public static final int ADDRESS = 1;
	public static final int VIC_STOP = 0;
	public static final int VIC_ADD = 1;
	public static final int VIC_SUB = 2;
	public static final int VIC_LOAD = 3;
	public static final int VIC_STORE = 4;
	public static final int VIC_GOTO = 5;
	public static final int VIC_GOTOZ = 6;
	public static final int VIC_GOTOP = 7;
	public static final int VIC_READ = 8;
	public static final int VIC_WRITE = 9;

	/**
	 * Resets the state of VIC to the default.
	 */
	public void reset();
	/**
	 * Flushes the IO queues
	 */
	public void flushIO();
	/**
	 * If VIC is in a bad state, retrieves the reason for this bad state.
	 * <p>
	 * If VIC is in a bad state, {@link #fetch()}, {@link #decode()},
	 * {@link #execute(int, int)}, {@link #step()}, and {@link #run()} won't do
	 * anything. 
	 * 
	 * @return null if in good state, otherwise a useful message
	 */
	public String getBadStateReason();

	/**
	 * Perform the fetch cycle of the FDE model (Von Neumann)
	 */
	public void fetch();
	/**
	 * Perform the decode cycle of the FDE model.
	 * <p>
	 * Particularly, return an opcode and an address based upon what is in the
	 * instruction register.
	 */
	public int[] decode();
	/**
	 * Perform the execute cycle of the FDE model.
	 * <p>
	 * Applies the provided instruction on the VIC virtual machine.
	 * 
	 * @param command the instruction to perform
	 * @param address the address to perform it on (if applicable)
	 */
	public void execute(int command, int address);
	/**
	 * Run through one iteration of the FDE cycle
	 */
	public void step();
	/**
	 * Step through a program until either a bad state is reached, or a STOP
	 * instruction is processed
	 */
	public void run();
	/**
	 * Tests if the VIC VM is currently running
	 * @return true iff this VIC Object is currently running
	 */
	public boolean isRunning();
	/**
	 * Returns the first element in the output queue for this VM
	 * @return the first element in the output queue for this VM
	 */
	public int readOutput();
	/**
	 * Adds a new item of data to the input queue if it is valid data for a
	 * particular implementation
	 * 
	 * @param data the data to add
	 */
	public void addInput(int data);
	/**
	 * Retrieves a memory dump of the VIC VM.
	 * <p>
	 * This is a non-volatile copy of the actual VM memory, so any changes made
	 * to it are not reflected in the internal state.
	 *  
	 * @return a newly allocated integer array that represents the current state
	 * of the VIC machine.
	 */
	public int[] memDump();
	/**
	 * Attempt to load the provided memory dump.
	 * <p>
	 * Will only load if the provided dump is a properly sized memory dump which
	 * does not attempt to change the two constants ZERO(98), ONE(99).
	 * @param memDump
	 */
	public void loadMemDump(int memDump[]);

	public int getIr();
	public int getDr();
	public int getPc();
}
