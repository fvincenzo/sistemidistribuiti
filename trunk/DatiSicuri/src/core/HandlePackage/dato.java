package core.HandlePackage;

/**
 * Generated from IDL struct "dato".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class dato
	implements org.omg.CORBA.portable.IDLEntity
{
	public dato(){}
	public int id;
	public boolean lettura;
	public boolean scrittura;
	public dato(int id, boolean lettura, boolean scrittura)
	{
		this.id = id;
		this.lettura = lettura;
		this.scrittura = scrittura;
	}
}
