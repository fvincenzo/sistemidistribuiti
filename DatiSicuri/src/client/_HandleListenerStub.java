package client;


/**
 * Generated from IDL interface "HandleListener".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public class _HandleListenerStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements client.HandleListener
{
	private String[] ids = {"IDL:client/HandleListener:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = client.HandleListenerOperations.class;
	public void notificaHandle(core.Handle h)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "notificaHandle", true);
				core.HandleHelper.write(_os,h);
				_is = _invoke(_os);
				return;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "notificaHandle", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			HandleListenerOperations _localServant = (HandleListenerOperations)_so.servant;
			try
			{
				_localServant.notificaHandle(h);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

}
