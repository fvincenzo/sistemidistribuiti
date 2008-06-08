package android.client;

import SQLite.BusyHandler;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//TODO: Controllare come usare la posizione gps e le operzioni sulla rubrica

public class MyContatsClient extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	private ContactClientInterface contactList;
	
	
	
	
	Button login;
	Button register;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.main);
        login = (Button) findViewById(R.id.Login);
        register  = (Button) findViewById(R.id.Register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

	@Override
	public void onClick(View arg0) {
		if (arg0 == (Button) findViewById(R.id.Login)){
			EditText username = (EditText)findViewById(R.id.Username);
			EditText password = (EditText)findViewById(R.id.Password);
			
//			contactList.login(username.getText().toString(), password.getText().toString());
			startActivity(new Intent(Intent.VIEW_ACTION, getIntent().getData()));
		}
		if (arg0 == (Button) findViewById(R.id.Register)){
			Log.i("MyContactsClient", "Hai premuto register");
		}
	}
}