package android.client;

import java.util.List;

import android.app.Activity;
import android.client.DBHelper.Location;
import android.os.Bundle;
import android.text.Layout.Alignment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class ViewLocations extends Activity {

	
	public static final String VIEW_LOCATIONS_ACTION =
		"android.client.action.VIEW_LOCATIONS";
	
	private TableLayout tl ;
	private DBHelper db;
	
	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.view_locations_layout);
		db = new DBHelper(this);
		tl = (TableLayout)findViewById(R.id.myTableLayout);
		
		String defString = db.getDefault();
		
		TableRow fstRow = new TableRow(this);
		fstRow.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		/* Create a Button to be the row-content. */
		TextView defText = new TextView(this);
		defText.setLayoutParams(new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.FILL_PARENT
		));
//		defText.setWidth(250);
		defText.setLines(3);
		defText.setAlignment(Alignment.ALIGN_CENTER);
		defText.setHeight(15);
		defText.setText("Default way to be contacted is:\n"+defString);
		
		fstRow.addView(defText);

		tl.addView(fstRow,new TableLayout.LayoutParams(
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		
		List<DBHelper.Location> l = db.fetchAllRows();
		for (Location row: l){
			
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			/* Create a Button to be the row-content. */
			TextView t = new TextView(this);
			t.setLayoutParams(new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.FILL_PARENT
			));
			t.setWidth(250);
			t.setLines(3);
			t.setText(row.preferred+"\n"+"Bella di padella");
			
			Button b = new Button(this);
			b.setText("Delete");
			b.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			b.setWidth(80);
			b.setHeight(30);
			/* Add Button to row. */
			tr.addView(t);
			tr.addView(b); 
			tl.addView(tr,new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
		}
		
		
	}
	
	
	
}
