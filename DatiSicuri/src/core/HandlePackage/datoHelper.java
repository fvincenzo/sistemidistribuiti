package core.HandlePackage;


/**
 * Generated from IDL struct "dato".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public final class datoHelper
{
	private static org.omg.CORBA.TypeCode _type = null;
	public static org.omg.CORBA.TypeCode type ()
	{
		if (_type == null)
		{
			_type = org.omg.CORBA.ORB.init().create_struct_tc(core.HandlePackage.datoHelper.id(),"dato",new org.omg.CORBA.StructMember[]{new org.omg.CORBA.StructMember("id", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(3)), null),new org.omg.CORBA.StructMember("lettura", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)), null),new org.omg.CORBA.StructMember("scrittura", org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.from_int(8)), null)});
		}
		return _type;
	}

	public static void insert (final org.omg.CORBA.Any any, final core.HandlePackage.dato s)
	{
		any.type(type());
		write( any.create_output_stream(),s);
	}

	public static core.HandlePackage.dato extract (final org.omg.CORBA.Any any)
	{
		return read(any.create_input_stream());
	}

	public static String id()
	{
		return "IDL:core/Handle/dato:1.0";
	}
	public static core.HandlePackage.dato read (final org.omg.CORBA.portable.InputStream in)
	{
		core.HandlePackage.dato result = new core.HandlePackage.dato();
		result.id=in.read_long();
		result.lettura=in.read_boolean();
		result.scrittura=in.read_boolean();
		return result;
	}
	public static void write (final org.omg.CORBA.portable.OutputStream out, final core.HandlePackage.dato s)
	{
		out.write_long(s.id);
		out.write_boolean(s.lettura);
		out.write_boolean(s.scrittura);
	}
}
