package android.client;

import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewLocations extends ListActivity implements OnClickListener {

	
	public static final String VIEW_LOCATIONS_ACTION =
		"android.client.action.VIEW_LOCATIONS";
	
//	private TableLayout tl ;
	private DBHelper db;
//	private Map<Button, Integer> buttons = new HashMap<Button, Integer>();
//	private Map<Integer, DBHelper.Location> locations = new HashMap<Integer, Location>();
//	private int i = 0;
//
//	private ListView myListView;
	
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		
	
		super.onCreate(icicle);
		db = new DBHelper(this);
		setTitle("Default location is: "+db.getDefault());
		setListAdapter(new MyListAdapter(this, this));
		
	
}
	
	public void update(){
//		MyListAdapter l = (MyListAdapter)getListAdapter();
		this.onContentChanged();
//		l.notifyDataSetChanged();
	}
	


	public void onClick(View arg0) {
		MyView m = (MyView)((Button)arg0).getParent();
		m.delete();
		setListAdapter(new MyListAdapter(this, this));
		// TODO Auto-generated method stub
		
	}

	
	class MyListAdapter extends BaseAdapter {

		
		private List<DBHelper.Location> list =db.fetchAllRows();
//		private DataSetObserver obs;
		private Context ctx;
		private OnClickListener listener;
//		private OnClickListener clk;
		
		public MyListAdapter(Context ctx, OnClickListener listener){
			this.ctx = ctx;
			this.listener = listener;
		}
		

		
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		

		public View getView(int position, View convertView, ViewGroup parent) {
			MyView mv = new MyView(ctx, list.get(position));
			mv.setOnClickListener(listener);
			return mv;
		}



//		public void onClick(View arg0) {
//			MyView m = (MyView)((Button)arg0).getParent();
//			m.delete();
//			this.notifyDataSetInvalidated();
//		}

		
		
	}
	
	
	 private class MyView extends LinearLayout {
		 
		 private TextView t;
		 private Button b;
		 private DBHelper.Location loc;
		 
		 public MyView(Context context, DBHelper.Location loc) {
	            super(context);
	            this.loc = loc;
	            
	            this.setOrientation(HORIZONTAL);
//	            this.setGravity(Gravity.ce)
//	            this.setBackgroundColor(Color.MAGENTA);
	            
//	            this.setPadding(2,2,2,2);
	            // Here we build the child views in code. They could also have
	            // been specified in an XML file.

	            t = new TextView(context);
	            
	            String text = "";
//	            text = "via dalle palle 13 2qefdj wi4urgh spyhv spdfuch px8y3fpe87 fdyxhclu eryg vl xuyvslfyul uygvl dfsuyvger7tv dulycvl suydfbg ";
	            if (!loc.address.equals("")){
	            	text = text+loc.address+"\n";
	            }
	            text = text+loc.preferred+"\n"+loc.latitude+","+loc.longitude;
	            t.setText(text);
//	            t.setBackgroundColor(Color.GREEN);
	            t.setGravity(Gravity.CENTER_VERTICAL);
	            addView(t, new LinearLayout.LayoutParams(
	                    250, LayoutParams.WRAP_CONTENT));

	            b = new Button(context);
	            b.setGravity(Gravity.CENTER_VERTICAL);
	            b.setText("Delete");
	            addView(b, new LinearLayout.LayoutParams(
	                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	        }


	        public void setOnClickListener(OnClickListener clk){
	        	b.setOnClickListener(clk);
	        }
	        
	        public void delete(){
	        	db.deleteRow(loc);
//	        	b.setEnabled(false);
//	        	update();
	        	
//	        	this.setVisibility(GONE);
	        }
	    }

}
