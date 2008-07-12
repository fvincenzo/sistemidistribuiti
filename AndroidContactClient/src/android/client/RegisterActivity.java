package android.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import java.util.List;
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

	private int MODIFY = 1;
	private int REGISTER = 0;
	private int status;

	private ServiceInterface contactList = null;

	private Button button;
	private EditText username;
	private EditText password;
	private EditText mobile;
	private EditText home;
	private EditText work;
	private EditText email;
	private EditText im;

//	private LocationManager lManager;

	//TODO: inserire i parametri che si vuole modificare quando si sceglie di modificarli...



	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		String action = getIntent().getAction();
		if (action.equals(MODIFY_ACTION))
			status = MODIFY;
		else 
			status = REGISTER;

		bindService(new Intent("android.client.MY_SERVICE"),this,0);


	}


	@Override
	public void onClick(View arg0) {
		if (arg0 == button){
			button.setSelected(true);
			if (status == REGISTER){
				final String u = username.getText().toString();
				final String p = password.getText().toString();
				final String m = mobile.getText().toString();
				final String h = home.getText().toString();
				final String w = work.getText().toString();
				final String e = email.getText().toString();
				final String i = im.getText().toString();
//				Location l = lManager.getCurrentLocation("gps");
				try {
					contactList.connect("10.0.2.2");
//					if (contactList.register(u, p, m, h, w, e, i, l.getLatitude(), l.getLongitude())){
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
			if (status == MODIFY) {
				if (password.getText().toString().equals("") ){
					try {
						contactList.changepersonal(username.getText().toString(), null, null, mobile.getText().toString(), home.getText().toString(), work.getText().toString(), email.getText().toString(), im.getText().toString());
						//TODO: Notificare l'avvenuta modifica
					} catch (DeadObjectException e) {
			
					}
				}
				else {
					
					new PasswordDialog(this, this).show();
				}
				//TODO: Inserire le operazioni di modifica dei dati personali
			}
		}
	}


	@Override
	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		try {
			contactList = ServiceInterface.Stub.asInterface(arg1);
			if (status == REGISTER)
				setContentView(R.layout.register);
			if (status == MODIFY)
				setContentView(R.layout.modify);
			
			username  = (EditText) findViewById(R.id.reg_username);

			password  = (EditText) findViewById(R.id.reg_password);
			mobile  = (EditText) findViewById(R.id.mobile);
			home  = (EditText) findViewById(R.id.home);
			work  = (EditText) findViewById(R.id.work);
			email  = (EditText) findViewById(R.id.email);
			im  = (EditText) findViewById(R.id.im);
			button  = (Button) findViewById(R.id.Register_final);
			button.setOnClickListener(this);
			
//				lManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			
			if (status == MODIFY){
				username.setEnabled(false);
				List<String> personal = contactList.getPersonal();
				username.setText(personal.get(0));
				mobile.setText(personal.get(1));
				home.setText(personal.get(2));
				work.setText(personal.get(3));
				email.setText(personal.get(4));
				im.setText(personal.get(5));
			}


		}catch (DeadObjectException e){

		}
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub

	}
	
	
	public boolean changePersonalData(String oldPassword){
		try {
			return contactList.changepersonal(username.getText().toString(), oldPassword, password.getText().toString(), mobile.getText().toString(), home.getText().toString(), work.getText().toString(), email.getText().toString(), im.getText().toString());
		} catch (DeadObjectException e) {

			return false;
		}
		
	}

	class PasswordDialog extends Dialog implements OnClickListener{

		private Button ok;
		private Button cancel;
		private EditText password; 
		private RegisterActivity callback;
		
		public PasswordDialog(Context context, RegisterActivity r) {
			super(context);
			this.callback = r;
			setContentView(R.layout.password_dialog);
			ok = (Button)findViewById(R.id.pwd_ok);
			cancel = (Button)findViewById(R.id.pwd_cancel);
			password = (EditText)findViewById(R.id.pwd_password);
			ok.setOnClickListener(this);
			cancel.setOnClickListener(this);
			
		}
		
		public void onClick(View arg0) {
			if (arg0 == ok){
				if (callback.changePersonalData(password.getText().toString())){
					startActivity(new Intent(android.client.MainLoopActivity.MAIN_LOOP_ACTION, getIntent().getData()));
					finish();
				}
				else{
					AlertDialog.show(callback, "Error", 0, "Wrong password inserted", "BACK",false);
					
				}
			}
			if (arg0 == cancel){
				this.dismiss();
			}
			
		}
		
		
		
		
	}

}
