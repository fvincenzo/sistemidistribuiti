package server;


/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public class _DatiSicuriStub
	extends org.omg.CORBA.portable.ObjectImpl
	implements server.DatiSicuri
{
	private String[] ids = {"IDL:server/DatiSicuri:1.0"};
	public String[] _ids()
	{
		return ids;
	}

	public final static java.lang.Class _opsClass = server.DatiSicuriOperations.class;
	public void addListener(client.HandleListener l, java.lang.String id)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "addListener", true);
				client.HandleListenerHelper.write(_os,l);
				_os.write_string(id);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "addListener", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			DatiSicuriOperations _localServant = (DatiSicuriOperations)_so.servant;
			try
			{
				_localServant.addListener(l,id);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public core.Handle riservaSpazio()
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "riservaSpazio", true);
				_is = _invoke(_os);
				core.Handle _result = core.HandleHelper.read(_is);
				return _result;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "riservaSpazio", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			DatiSicuriOperations _localServant = (DatiSicuriOperations)_so.servant;
			core.Handle _result;
			try
			{
				_result = _localServant.riservaSpazio();
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public void pubblica(core.Handle h, java.lang.String destinatario)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "pubblica", true);
				core.HandleHelper.write(_os,h);
				_os.write_string(destinatario);
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "pubblica", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			DatiSicuriOperations _localServant = (DatiSicuriOperations)_so.servant;
			try
			{
				_localServant.pubblica(h,destinatario);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return;
		}

		}

	}

	public boolean scriviDato(core.Handle h, int dato)
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "scriviDato", true);
				core.HandleHelper.write(_os,h);
				_os.write_long(dato);
				_is = _invoke(_os);
				boolean _result = _is.read_boolean();
				return _result;
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
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "scriviDato", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			DatiSicuriOperations _localServant = (DatiSicuriOperations)_so.servant;
			boolean _result;
			try
			{
				_result = _localServant.scriviDato(h,dato);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

	public int leggiDato(core.Handle h) throws server.ReadErrorException
	{
		while(true)
		{
		if(! this._is_local())
		{
			org.omg.CORBA.portable.InputStream _is = null;
			try
			{
				org.omg.CORBA.portable.OutputStream _os = _request( "leggiDato", true);
				core.HandleHelper.write(_os,h);
				_is = _invoke(_os);
				int _result = _is.read_long();
				return _result;
			}
			catch( org.omg.CORBA.portable.RemarshalException _rx ){}
			catch( org.omg.CORBA.portable.ApplicationException _ax )
			{
				String _id = _ax.getId();
				if( _id.equals("IDL:server/ReadErrorException:1.0"))
				{
					throw server.ReadErrorExceptionHelper.read(_ax.getInputStream());
				}
				throw new RuntimeException("Unexpected exception " + _id );
			}
			finally
			{
				this._releaseReply(_is);
			}
		}
		else
		{
			org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke( "leggiDato", _opsClass );
			if( _so == null )
				throw new org.omg.CORBA.UNKNOWN("local invocations not supported!");
			DatiSicuriOperations _localServant = (DatiSicuriOperations)_so.servant;
			int _result;
			try
			{
				_result = _localServant.leggiDato(h);
			}
			finally
			{
				_servant_postinvoke(_so);
			}
			return _result;
		}

		}

	}

}
