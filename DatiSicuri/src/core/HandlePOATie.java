package core;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "Handle".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */

public class HandlePOATie
	extends HandlePOA
{
	private HandleOperations _delegate;

	private POA _poa;
	public HandlePOATie(HandleOperations delegate)
	{
		_delegate = delegate;
	}
	public HandlePOATie(HandleOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public core.Handle _this()
	{
		return core.HandleHelper.narrow(_this_object());
	}
	public core.Handle _this(org.omg.CORBA.ORB orb)
	{
		return core.HandleHelper.narrow(_this_object(orb));
	}
	public HandleOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(HandleOperations delegate)
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
	public int restringiPermessi()
	{
		return _delegate.restringiPermessi();
	}

}
