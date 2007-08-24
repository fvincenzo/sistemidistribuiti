package server;


/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public abstract class DatiSicuriPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, server.DatiSicuriOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "addListener", new java.lang.Integer(0));
		m_opsHash.put ( "riservaSpazio", new java.lang.Integer(1));
		m_opsHash.put ( "pubblica", new java.lang.Integer(2));
		m_opsHash.put ( "scriviDato", new java.lang.Integer(3));
		m_opsHash.put ( "leggiDato", new java.lang.Integer(4));
	}
	private String[] ids = {"IDL:server/DatiSicuri:1.0"};
	public server.DatiSicuri _this()
	{
		return server.DatiSicuriHelper.narrow(_this_object());
	}
	public server.DatiSicuri _this(org.omg.CORBA.ORB orb)
	{
		return server.DatiSicuriHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // addListener
			{
				client.HandleListener _arg0=client.HandleListenerHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				addListener(_arg0,_arg1);
				break;
			}
			case 1: // riservaSpazio
			{
				_out = handler.createReply();
				core.HandleHelper.write(_out,riservaSpazio());
				break;
			}
			case 2: // pubblica
			{
				core.Handle _arg0=core.HandleHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				pubblica(_arg0,_arg1);
				break;
			}
			case 3: // scriviDato
			{
				core.Handle _arg0=core.HandleHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				_out.write_boolean(scriviDato(_arg0,_arg1));
				break;
			}
			case 4: // leggiDato
			{
			try
			{
				core.Handle _arg0=core.HandleHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(leggiDato(_arg0));
			}
			catch(server.ReadErrorException _ex0)
			{
				_out = handler.createExceptionReply();
				server.ReadErrorExceptionHelper.write(_out, _ex0);
			}
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
