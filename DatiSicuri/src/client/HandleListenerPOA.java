package client;


/**
 * Generated from IDL interface "HandleListener".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public abstract class HandleListenerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, client.HandleListenerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "notificaHandle", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:client/HandleListener:1.0"};
	public client.HandleListener _this()
	{
		return client.HandleListenerHelper.narrow(_this_object());
	}
	public client.HandleListener _this(org.omg.CORBA.ORB orb)
	{
		return client.HandleListenerHelper.narrow(_this_object(orb));
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
			case 0: // notificaHandle
			{
				core.Handle _arg0=core.HandleHelper.read(_input);
				_out = handler.createReply();
				notificaHandle(_arg0);
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
