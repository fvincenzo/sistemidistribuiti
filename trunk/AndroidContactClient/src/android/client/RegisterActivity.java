package android.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity implements OnClickListener {

	/**
	 * This is a special intent action that means "register a new user".
	 */
	public static final String REGISTER_ACTION =
		"android.client.action.REGISTER";

	private ContactClientInterface contactList = ContactClient.getHistance();

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
				contactList.connect("192.168.0.8");
//				if (contactList.register(u, p, m, h, w, e, i, l.getLatitude(), l.getLongitude())){
					if (contactList.register(u, p, m, h, w, e, i, 13.4336, 64.3464)){
					startActivity(new Intent(android.client.FriendsList.PENDING_ACTION, getIntent().getData()));
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




}
