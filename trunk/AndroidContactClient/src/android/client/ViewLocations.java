package android.client;

import java.util.List;

import android.app.Activity;
import android.client.DBHelper.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
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
		List<DBHelper.Location> l = db.fetchAllRows();
		
		tl = (TableLayout)findViewById(R.id.myTableLayout);
		
		for (Location row: l){
			
		}
		
		
		TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(
                       LayoutParams.FILL_PARENT,
                       LayoutParams.WRAP_CONTENT));
             /* Create a Button to be the row-content. */
        	EditText t = new EditText(this);
        	t.setLayoutParams(new TableRow.LayoutParams(
        			LayoutParams.WRAP_CONTENT,
                    LayoutParams.FILL_PARENT
        	));
        	t.setText("Testo di prova");
             Button b = new Button(this);
             b.setText("Delete");
             b.setLayoutParams(new LayoutParams(
                       LayoutParams.FILL_PARENT,
                       LayoutParams.WRAP_CONTENT));
             /* Add Button to row. */
             tr.addView(t);
             tr.addView(b); 
             tl.addView(tr,new TableLayout.LayoutParams(
                     LayoutParams.FILL_PARENT,
                     LayoutParams.WRAP_CONTENT));
	}
	
	
	
}
