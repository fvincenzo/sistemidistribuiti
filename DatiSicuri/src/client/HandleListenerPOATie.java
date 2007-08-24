package client;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "HandleListener".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public class HandleListenerPOATie
	extends HandleListenerPOA
{
	private HandleListenerOperations _delegate;

	private POA _poa;
	public HandleListenerPOATie(HandleListenerOperations delegate)
	{
		_delegate = delegate;
	}
	public HandleListenerPOATie(HandleListenerOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public client.HandleListener _this()
	{
		return client.HandleListenerHelper.narrow(_this_object());
	}
	public client.HandleListener _this(org.omg.CORBA.ORB orb)
	{
		return client.HandleListenerHelper.narrow(_this_object(orb));
	}
	public HandleListenerOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(HandleListenerOperations delegate)
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
	public void notificaHandle(core.Handle h)
	{
_delegate.notificaHandle(h);
	}

}
