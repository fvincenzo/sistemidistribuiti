package server;


/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class DatiSicuriHelper
{
	public static void insert (final org.omg.CORBA.Any any, final server.DatiSicuri s)
	{
			any.insert_Object(s);
	}
	public static server.DatiSicuri extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:server/DatiSicuri:1.0", "DatiSicuri");
	}
	public static String id()
	{
		return "IDL:server/DatiSicuri:1.0";
	}
	public static DatiSicuri read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object(server._DatiSicuriStub.class));
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final server.DatiSicuri s)
	{
		_out.write_Object(s);
	}
	public static server.DatiSicuri narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof server.DatiSicuri)
		{
			return (server.DatiSicuri)obj;
		}
		else if (obj._is_a("IDL:server/DatiSicuri:1.0"))
		{
			server._DatiSicuriStub stub;
			stub = new server._DatiSicuriStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
		else
		{
			throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
		}
	}
	public static server.DatiSicuri unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
		{
			return null;
		}
		else if (obj instanceof server.DatiSicuri)
		{
			return (server.DatiSicuri)obj;
		}
		else
		{
			server._DatiSicuriStub stub;
			stub = new server._DatiSicuriStub();
			stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
			return stub;
		}
	}
}
