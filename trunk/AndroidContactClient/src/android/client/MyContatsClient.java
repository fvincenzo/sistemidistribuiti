package android.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class MyContatsClient extends Activity implements OnClickListener, ServiceConnection{

	//TODO sistemare la gestione del servizio

	private ServiceInterface s;

	public static final String LOGIN_ACTION =
		"android.client.action.LOGIN";

	public static final String MAIN_LwOOP_ACTION =
		"android.client.action.MAIN_LOOP";

	public static final int SERVICE_BOUND = 1;

	private static final int LOGIN = 1;

	public int service_status = 1;
	private Button login;
	private Button register;
	private CheckBox forcelogin;
	EditText username;
	EditText password;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

//		final Intent intent = getIntent();
//		String action = intent.getAction();
//		if (action.equals(Intent.MAIN_ACTION)){

//		if (bindService(new Intent("android.client.MY_SERVICE"),this, 0)){
//		startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
////		finish();
//		} else {

		startService(new Intent("android.client.MY_SERVICE"), null);
		bindService(new Intent("android.client.MY_SERVICE"),this, 0);

//		}
//		}


	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == login){
			bindService(new Intent("android.client.MY_SERVICE"),this, 0);
			service_status = LOGIN;

			final String u = username.getText().toString();
			final String p = password.getText().toString();
			if ( u != "" && p != ""){
				try {
					this.s.connect("10.0.2.2");
					boolean ret;
					if(forcelogin.isChecked()){
						ret = this.s.forcelogin(u, p);
					} else 
						ret = this.s.login(u,p);	
					
					if (ret){
						//TODO Controllare il getIntent().getData che significa
						startActivity(new Intent(android.client.FriendsList.PENDING_ACTION, getIntent().getData()));
						finish();
					}
					else {
						AlertDialog.show(this, "Error", 0, "Error occurred while logging in. Check your data or try later", "BACK",false);
					}
				}catch (Exception e){
					AlertDialog.show(this, "Error", 0, "Error occurred while connecting to server.", "BACK",false);
				}
			}
			else 
			{
				AlertDialog.show(this, "Errore", 0, "Please fill in the username and the password field", "BACK",false);
			}
		}
		if (arg0 == register){
			startActivity(new Intent(android.client.RegisterActivity.REGISTER_ACTION, getIntent().getData()));
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		s = ServiceInterface.Stub.asInterface(service);
		try {
			if (s.isRunning()){
				startActivity(new Intent(MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
				finish();
			}else {

				setContentView(R.layout.login);


				username = (EditText)findViewById(R.id.Username);
				password = (EditText)findViewById(R.id.Password);
				login = (Button) findViewById(R.id.Login);
				register  = (Button) findViewById(R.id.Register);
				forcelogin = (CheckBox) findViewById(R.id.forcelogincheckbox);
				login.setOnClickListener(this);
				register.setOnClickListener(this);
			}
		}catch (DeadObjectException e){

		}
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub

	}
}