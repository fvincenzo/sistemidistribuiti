package server;


/**
 * Generated from IDL exception "ReadErrorException".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class ReadErrorExceptionHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_exception_tc(server.ReadErrorExceptionHelper.id(),"ReadErrorException",new org.omg.CORBA.StructMember[0]);
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final server.ReadErrorException s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static server.ReadErrorException extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:server/ReadErrorException:1.0";
	}
	public static server.ReadErrorException read (final org.omg.CORBA.portable.InputStream in)
	{
		server.ReadErrorException result = new server.ReadErrorException();
		if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final server.ReadErrorException s)
	{
		out.write_string(id());
	}
}
