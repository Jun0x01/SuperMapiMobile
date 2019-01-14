package com.jun.tools.toolkit;

import junit.framework.Assert;

public class UnitAssert {

	
	private static long mAssertCalledCount = 0;
	private static long mAssertPassedCount = 0;

	/**
	 * Asserts that a condition is true. If it isn't it throws an
	 * AssertionFailedError with the given message.
	 */
	static public synchronized void assertTrue(String message, boolean condition) {
		mAssertCalledCount ++;
		Assert.assertTrue(message, condition);
		
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that a condition is false. If it isn't it throws an
	 * AssertionFailedError with the given message.
	 */
	static public synchronized void assertFalse(String message, boolean condition) {
		assertTrue(message, !condition);
	}

	/**
	 * Fails a test with the given message.
	 */
	static public synchronized void fail(String message) {
		mAssertCalledCount ++;
		
		Assert.fail(message);
		
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two objects are equal. If they are not an
	 * AssertionFailedError is thrown with the given message.
	 */
	static public synchronized void assertEquals(String message, Object expected, Object actual) {
		mAssertCalledCount ++;
		
		Assert.assertEquals(message, expected, actual);
		
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two Strings are equal.
	 */
	static public synchronized void assertEquals(String message, String expected, String actual) {
		mAssertCalledCount ++;
		
		Assert.assertEquals(message, expected, actual);
		
		mAssertPassedCount ++;
	}
	
	/**
	 * Asserts that two Strings are equal.
	 */
	static public synchronized void assertNotEquals(String message, String expected, String actual) {
		mAssertCalledCount ++;
		
//		Assert.assertEquals(message, expected, actual);
		if(expected.equals(actual)){
			if (message == null) {
				fail("[" + expected + "]" + " is the same as " + "[" + actual + "]");
			} else {
				fail(message);
			}
		}
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two doubles are equal concerning a delta. If they are not an
	 * AssertionFailedError is thrown with the given message. If the expected
	 * value is infinity then the delta value is ignored.
	 */
	static public synchronized void assertEquals(String message, double expected, double actual, double delta) {
		mAssertCalledCount ++;
		
		Assert.assertEquals(message, expected, actual, delta);

		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two floats are equal concerning a delta. If they are not an
	 * AssertionFailedError is thrown with the given message. If the expected
	 * value is infinity then the delta value is ignored.
	 */
	static public synchronized void assertEquals(String message, float expected, float actual, float delta) {
		mAssertCalledCount ++;
		
		Assert.assertEquals(message, expected, actual, delta);
		
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two longs are equal. If they are not an AssertionFailedError
	 * is thrown with the given message.
	 */
	static public void assertEquals(String message, long expected, long actual) {
		assertEquals(message, new Long(expected), new Long(actual));
	}

	/**
	 * Asserts that two booleans are equal. If they are not an
	 * AssertionFailedError is thrown with the given message.
	 */
	static public void assertEquals(String message, boolean expected, boolean actual) {
		assertEquals(message, new Boolean(expected), new Boolean(actual));
	}

	/**
	 * Asserts that two bytes are equal. If they are not an AssertionFailedError
	 * is thrown with the given message.
	 */
	static public void assertEquals(String message, byte expected, byte actual) {
		assertEquals(message, new Byte(expected), new Byte(actual));
	}

	/**
	 * Asserts that two chars are equal. If they are not an AssertionFailedError
	 * is thrown with the given message.
	 */
	static public void assertEquals(String message, char expected, char actual) {
		assertEquals(message, new Character(expected), new Character(actual));
	}

	/**
	 * Asserts that two shorts are equal. If they are not an
	 * AssertionFailedError is thrown with the given message.
	 */
	static public void assertEquals(String message, short expected, short actual) {
		assertEquals(message, new Short(expected), new Short(actual));
	}

	/**
	 * Asserts that two ints are equal. If they are not an AssertionFailedError
	 * is thrown with the given message.
	 */
	static public void assertEquals(String message, int expected, int actual) {
		assertEquals(message, new Integer(expected), new Integer(actual));
	}

	/**
	 * Asserts that an object isn't null. If it is an AssertionFailedError is
	 * thrown with the given message.
	 */
	static public void assertNotNull(String message, Object object) {
		assertTrue(message, object != null);
	}

	/**
	 * Asserts that an object is null. If it is not an AssertionFailedError is
	 * thrown with the given message.
	 */
	static public void assertNull(String message, Object object) {
		assertTrue(message, object == null);
	}

	/**
	 * Asserts that two objects refer to the same object. If they are not an
	 * AssertionFailedError is thrown with the given message.
	 */
	static public synchronized void assertSame(String message, Object expected, Object actual) {
		mAssertCalledCount ++;
		
		Assert.assertSame(message, expected, actual);
		
		mAssertPassedCount ++;
	}

	/**
	 * Asserts that two objects refer to the same object. If they are not an
	 * AssertionFailedError is thrown with the given message.
	 */
	static public synchronized void assertNotSame(String message, Object expected, Object actual) {
		mAssertCalledCount ++;
		
		Assert.assertNotSame(message, expected, actual);
		
		mAssertPassedCount ++;
	}

	
	/**
	 * Gets the count of assert method was called
	 * @return  the count of assert was called, long type 
	 */
	static public synchronized long getAssertCalledCount(){
		return mAssertCalledCount;
	}
	
	/**
	 * Gets the count of assert method was called and passed
	 * @return the count of assert was called and passed , long type 
	 */
	static public  long getAssertPassedCount(){
		return mAssertPassedCount;
	}
	
	
}
