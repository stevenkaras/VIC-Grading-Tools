package idc.vic;

/**
 * A generic parsing exception thrown by classes in the VIC package when parsing
 * failed for a reason. The reason should be contained in the message.
 * 
 * @author Steven Karas
 */
public class VICParsingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public VICParsingException(String message) {
		super(message);
	}
}
