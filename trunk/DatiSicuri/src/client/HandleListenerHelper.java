package client;


/**
 * Generated from IDL interface "HandleListener".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class HandleListenerHelper
{
	public static void insert (final org.omg.CORBA.Any any, final client.HandleListener s)
	{
			any.insert_Object(s);
	}
	public static client.HandleListener extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:client/HandleListener:1.0", "HandleListener");
	}
	public static String id()
	{
		return "IDL:client/HandleListener:1.0";
	}
	public static HandleListener read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(client._HandleListenerStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final client.HandleListener s)
	{
		_out.write_Object(s);
	}
	public static client.HandleListener narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof client.HandleListener)
		{
			return (client.HandleListener)obj;
		}
		else if (obj._is_a("IDL:client/HandleListener:1.0"))
		{
			client._HandleListenerStub stub;
			stub = new client._HandleListenerStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static client.HandleListener unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof client.HandleListener)
		{
			return (client.HandleListener)obj;
		}
		else
		{
			client._HandleListenerStub stub;
			stub = new client._HandleListenerStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
