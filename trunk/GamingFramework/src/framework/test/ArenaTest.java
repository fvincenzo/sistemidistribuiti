/*
 * Created on 17-giu-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package framework.test;

import framework.console.*;
import framework.core.client.Client;
import junit.framework.*;

/**
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArenaTest extends TestCase{
	/**
	 * Costruttore
	 */
	public ArenaTest(java.lang.String testName) {
        super(testName);
    }
	/**
	 * Il metodo genera un test sul framework Arena
	 * @return un oggetto di tipo Test
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite(ArenaTest.class);
		return suite;
    }
    /*
     * Metodo principale
     */
	public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

	
	public void testLogin(){
		TextModeClient paperino = new TextModeClient();
		Client pippo = new Client(paperino);
		assertEquals(false,pippo.register("nerd","nerd","localhost:27100"));
	}
	
}
