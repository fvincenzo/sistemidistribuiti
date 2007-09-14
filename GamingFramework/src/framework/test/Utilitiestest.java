package framework.test;
import framework.core.logic.Utilities;
import junit.framework.*;
/*
 * Created on 20-mag-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/**
 * @author Vincenzo Frascino
 *
 * Utilitiestest testa il funzionamento della classe Utilities
 */
public class Utilitiestest extends TestCase {
	/**
	 * Costruttore della classe UtilitiesTest
	 * @param testName contiene il nome del test
	 */
	public Utilitiestest(java.lang.String testName) {
        super(testName);
    }
	/**
	 * Il metodo genera un test sulla classe Utilities
	 * @return un oggetto di tipo Test
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite(Utilitiestest.class);
		return suite;
    }
    /*
     * Metodo principale
     */
	public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
	/**
	 * Test del metodo BooleanToInt()
	 *
	 */
	public void testBooleanToInt() {
        System.out.println("testBooleanToInt");
        assertEquals(true, Utilities.intToBoolean(1));
    }
	/**
	 * Test del metodo IntToBoolean()
	 *
	 */
	public void testIntToBoolean(){
		System.out.println("testIntToBoolean");
        assertEquals(1, Utilities.booleanToInt(true));
	}
	/**
	 * Test del metodo dueAllaIMultiple(int x)
	 *
	 */
	public void testdueAllaIMultiple(){
		System.out.println("testdueAllaIMultiple");
        assertEquals(true, Utilities.dueAllaIMultiple(16));
	}

}
