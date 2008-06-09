package android.client;

import android.app.Activity;
import android.os.Bundle;

public class MainLoopActivity extends Activity {

	public static final String MAIN_LOOP_ACTION =
		"android.client.action.MAIN_LOOP";
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.main_loop);
	}

	
}
