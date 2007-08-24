package client;

/**
 * Generated from IDL interface "HandleListener".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class HandleListenerHolder	implements org.omg.CORBA.portable.Streamable{
	 public HandleListener value;
	public HandleListenerHolder()
	{
	}
	public HandleListenerHolder (final HandleListener initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type()
	{
		return HandleListenerHelper.type();
	}
	public void _read (final org.omg.CORBA.portable.InputStream in)
	{
		value = HandleListenerHelper.read (in);
	}
	public void _write (final org.omg.CORBA.portable.OutputStream _out)
	{
		HandleListenerHelper.write (_out,value);
	}
}
