package android.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainLoopActivity extends Activity implements OnClickListener, ServiceConnection{

	public static final String MAIN_LOOP_ACTION =
		"android.client.action.MAIN_LOOP";
	
	private Button start;
	private Button stop;
	private TextView runningLabel;
	private TextView notRunningLabel;
	private Button findFriends;
	
	
	@Override
	protected void onCreate(Bundle icicle) {
	
		super.onCreate(icicle);
		
		bindService(new Intent("android.client.MY_SERVICE"),this,0);
		
//		}
//		else {
//			startActivity(new Intent(Intent.MAIN_ACTION, getIntent().getData()));
//			finish();
//		}
	}


	@Override
	public void onClick(View arg0) {
		if (arg0 == findFriends){
			startActivity(new Intent(android.client.FriendsList.ALL_USERS_ACTION, getIntent().getData()));
		}
		if (arg0 == start){
			startService(new Intent("android.client.MY_SERVICE"), null);
			start.setVisibility(View.INVISIBLE);
			notRunningLabel.setVisibility(View.INVISIBLE);
			stop.setVisibility(View.VISIBLE);
			runningLabel.setVisibility(View.VISIBLE);
			
		}
		if (arg0 == stop){
			stopService(new Intent("android.client.MY_SERVICE"));
			stop.setVisibility(View.INVISIBLE);
			runningLabel.setVisibility(View.INVISIBLE);
			start.setVisibility(View.VISIBLE);
			notRunningLabel.setVisibility(View.VISIBLE);
			
		}
	}


	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

		setContentView(R.layout.main_loop);
		
		findFriends = (Button)findViewById(R.id.findFriends);
		start = (Button)findViewById(R.id.start_button);
		stop = (Button)findViewById(R.id.stop_button);
		runningLabel =  (TextView)findViewById(R.id.running_label);
		notRunningLabel = (TextView)findViewById(R.id.not_running_label);
		findFriends.setOnClickListener(this);
		start.setOnClickListener(this);
		stop.setOnClickListener(this);
		
		start.setVisibility(View.INVISIBLE);
		notRunningLabel.setVisibility(View.INVISIBLE);
		stop.setVisibility(View.VISIBLE);
		runningLabel.setVisibility(View.VISIBLE);		
	}


	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		stop.setVisibility(View.INVISIBLE);
		runningLabel.setVisibility(View.INVISIBLE);
		start.setVisibility(View.VISIBLE);
		notRunningLabel.setVisibility(View.VISIBLE);
	}

	
}
