package server;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public class DatiSicuriPOATie
	extends DatiSicuriPOA
{
	private DatiSicuriOperations _delegate;

	private POA _poa;
	public DatiSicuriPOATie(DatiSicuriOperations delegate)
	{
		_delegate = delegate;
	}
	public DatiSicuriPOATie(DatiSicuriOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public server.DatiSicuri _this()
	{
		return server.DatiSicuriHelper.narrow(_this_object());
	}
	public server.DatiSicuri _this(org.omg.CORBA.ORB orb)
	{
		return server.DatiSicuriHelper.narrow(_this_object(orb));
	}
	public DatiSicuriOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(DatiSicuriOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		return super._default_POA();
	}
	public void addListener(client.HandleListener l, java.lang.String id)
	{
_delegate.addListener(l,id);
	}

	public core.Handle riservaSpazio()
	{
		return _delegate.riservaSpazio();
	}

	public void pubblica(core.Handle h, java.lang.String destinatario)
	{
_delegate.pubblica(h,destinatario);
	}

	public boolean scriviDato(core.Handle h, int dato)
	{
		return _delegate.scriviDato(h,dato);
	}

	public int leggiDato(core.Handle h) throws server.ReadErrorException
	{
		return _delegate.leggiDato(h);
	}

}
