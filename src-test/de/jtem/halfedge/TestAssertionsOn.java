package de.jtem.halfedge;
import junit.framework.TestCase;

import org.junit.Test;



public class TestAssertionsOn  extends TestCase {

	@Test
	public void testAssertionsOn() {
		boolean assertionsOn = false;
		assert assertionsOn = true;
		assertTrue(assertionsOn);
	}
}
