package core;


/**
 * Generated from IDL interface "Handle".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class HandleHelper
{
	public static void insert (final org.omg.CORBA.Any any, final core.Handle s)
	{
			any.insert_Object(s);
	}
	public static core.Handle extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:core/Handle:1.0", "Handle");
	}
	public static String id()
	{
		return "IDL:core/Handle:1.0";
	}
	public static Handle read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(core._HandleStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final core.Handle s)
	{
		_out.write_Object(s);
	}
	public static core.Handle narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof core.Handle)
		{
			return (core.Handle)obj;
		}
		else if (obj._is_a("IDL:core/Handle:1.0"))
		{
			core._HandleStub stub;
			stub = new core._HandleStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static core.Handle unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof core.Handle)
		{
			return (core.Handle)obj;
		}
		else
		{
			core._HandleStub stub;
			stub = new core._HandleStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
