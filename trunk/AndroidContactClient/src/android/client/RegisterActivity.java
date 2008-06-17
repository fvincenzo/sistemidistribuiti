package android.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity implements OnClickListener , ServiceConnection{

	/**
	 * This is a special intent action that means "register a new user".
	 */
	public static final String REGISTER_ACTION =
		"android.client.action.REGISTER";

	public static final String MODIFY_ACTION = 
		"android.client.action.MODIFY";
	
	private ServiceInterface contactList = null;

	private Button register;
	private EditText username;
	private EditText password;
	private EditText mobile;
	private EditText home;
	private EditText work;
	private EditText email;
	private EditText im;

	private LocationManager lManager;




	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		bindService(new Intent("android.client.MY_SERVICE"),this,0);
		

	}


	@Override
	public void onClick(View arg0) {
		if (arg0 == register){
			final String u = username.getText().toString();
			final String p = password.getText().toString();
			final String m = mobile.getText().toString();
			final String h = home.getText().toString();
			final String w = work.getText().toString();
			final String e = email.getText().toString();
			final String i = im.getText().toString();
			Location l = lManager.getCurrentLocation("gps");
			try {
				contactList.connect("10.0.2.2");
//				if (contactList.register(u, p, m, h, w, e, i, l.getLatitude(), l.getLongitude())){
					if (contactList.register(u, p, m, h, w, e, i)){
					startActivity(new Intent(android.client.MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
					finish();
				}
				else {
					AlertDialog.show(this, "Error", 0, "Error occurred while registering user:\n"+u, "OK", false);
				}
			}catch (Exception ex){
				AlertDialog.show(this, "Exception", 0, "Exception occurred while registering user", "OK", false);
			}
		}
	}


	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {

		contactList = ServiceInterface.Stub.asInterface(arg1);

		setContentView(R.layout.register);
		username  = (EditText) findViewById(R.id.reg_username);
		password  = (EditText) findViewById(R.id.reg_password);
		mobile  = (EditText) findViewById(R.id.mobile);
		home  = (EditText) findViewById(R.id.home);
		work  = (EditText) findViewById(R.id.work);
		email  = (EditText) findViewById(R.id.email);
		im  = (EditText) findViewById(R.id.im);
		register  = (Button) findViewById(R.id.Register_final);
		register.setOnClickListener(this);
		lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


		
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
