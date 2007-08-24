package core.HandlePackage;

/**
 * Generated from IDL struct "dato".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class datoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public core.HandlePackage.dato value;

	public datoHolder ()
	{
	}
	public datoHolder(final core.HandlePackage.dato initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return core.HandlePackage.datoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = core.HandlePackage.datoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		core.HandlePackage.datoHelper.write(_out, value);
	}
}
