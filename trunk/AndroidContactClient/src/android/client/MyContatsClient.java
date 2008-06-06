package android.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//TODO: Controllare come usare la posizione gps e le operzioni sulla rubrica
public class MyContatsClient extends Activity {
    /** Called when the activity is first created. */
	private ContactClientInterface contactList;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.login);
        
        final EditText username = (EditText)findViewById(R.id.user);
        
        final Button button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
//            	username.getText().toString();
            	
//            	contactList.login("Utente", "ciao");
            }
        });
    }
}