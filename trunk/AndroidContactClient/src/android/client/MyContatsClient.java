package android.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MyContatsClient extends Activity implements OnClickListener{
	/** Called when the activity is first created. */

	private ContactClientInterface contactList = ContactClient.getHistance();

	public static final String LOGIN_ACTION =
		"android.client.action.LOGIN";



	private Button login;
	private Button register;
	EditText username;
	EditText password;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);


		
			setContentView(R.layout.login);

			username = (EditText)findViewById(R.id.Username);
			password = (EditText)findViewById(R.id.Password);
			login = (Button) findViewById(R.id.Login);
			register  = (Button) findViewById(R.id.Register);
			login.setOnClickListener(this);
			register.setOnClickListener(this);
		



	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == login){
			final String u = username.getText().toString();
			final String p = password.getText().toString();
			if ( u != "" && p != ""){
				try {
					contactList.connect("192.168.0.8");
					if (contactList.login(u,p)){
						startActivity(new Intent(android.client.FriendsList.PENDING_ACTION, getIntent().getData()));
						finish();
					}
					else {
						AlertDialog.show(this, "Errore", 0, "Error occurred while logging in. Check your data or try later", "BACK",false);
					}
				}catch (Exception e){
					AlertDialog.show(this, "Errore", 0, "Error occurred while connecting to server.", "BACK",false);
				}
			}
			else 
			{
				AlertDialog.show(this, "Errore", 0, "Please fill in the username and the password field", "BACK",false);
			}
		}
		if (arg0 == register){
			startActivity(new Intent(android.client.RegisterActivity.REGISTER_ACTION, getIntent().getData()));
			finish();
		}
	}
}