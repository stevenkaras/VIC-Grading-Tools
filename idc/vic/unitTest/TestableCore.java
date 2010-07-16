package idc.vic.unitTest;

import idc.vic.Core;

/**
 * A version of the core that can be tested without absurd complexity
 * 
 * @author Steven Karas
 */
class TestableCore extends Core implements TestableVIC {
	
	TestableCore() {
		super();
	}

	public void setDr(int dr) {
		super.setDr(dr);
	}
}
