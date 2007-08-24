package core;

/**
 * Generated from IDL interface "Handle".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class HandleHolder	implements org.omg.CORBA.portable.Streamable{
	 public Handle value;
	public HandleHolder()
	{
	}
	public HandleHolder (final Handle initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return HandleHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = HandleHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		HandleHelper.write (_out,value);
	}
}
