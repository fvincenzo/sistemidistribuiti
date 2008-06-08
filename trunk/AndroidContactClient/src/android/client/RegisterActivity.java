package android.client;

import android.app.Activity;
import android.os.Bundle;

public class RegisterActivity extends Activity {

	/**
     * This is a special intent action that means "register a new user".
     */
    public static final String REGISTER_ACTION =
        "android.client.action.REGISTER";
    
    
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.register);
	}
	
	
	

}
