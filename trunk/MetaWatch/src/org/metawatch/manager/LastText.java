package org.metawatch.manager;

import java.util.Locale;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LastText extends Activity
{	
	SQLiteDatabase db;
	final String CREATE_TABLE_LASTTEXT =
        	"CREATE TABLE IF NOT EXISTS LastText ("
        	+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
        	+ "location INTEGER,"
        	+ "name TEXT);";
	int lastLoc;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lasttext);
        
        try{
        	db = SQLiteDatabase.openDatabase("MetaWatchLastText.db", null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException ex)
        {
        	db = openOrCreateDatabase("MetaWatchLastText.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        	db.setVersion(1);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.execSQL(CREATE_TABLE_LASTTEXT);
        }

        //Find the last current location
        
        Cursor cur = db.query("LastText", null, null, null, null, null, null);
        lastLoc = 0;
        cur.moveToNext();
        while (cur.isAfterLast() == false) {
        	if(cur.getInt(1) > lastLoc)
        		lastLoc = cur.getInt(1) ;
       	    cur.moveToNext();
        }
        cur.close();
        
        UpdateView();
        
        //Set up the adding button
        final Button button = (Button) findViewById(R.id.LastTextAddButton);
        final TextView text = (TextView) findViewById(R.id.LastTextAddText); 
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	InsertRecords(lastLoc+1, text.getText().toString());
            	lastLoc++;
            	text.setText("");
            	UpdateView();
            }
        });
        
        //Setup list click
        ListView lv=(ListView)findViewById(R.id.LastTextList);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            	//Find the location value of the clicked item
            	String item = (String) ((TextView) view).getText();
            	item = item.substring(0, item.indexOf(":"));
            	int loc = Integer.parseInt(item);
            	
            	//Delete the old value
            	db.delete("LastText", "location=?", new String[] {Long.toString(loc)});
            	
            	//Update the location values of all values greater than loc.
            	Cursor cur = db.query("LastText", null, null, null, null, null, null);
                cur.moveToNext();
                while (cur.isAfterLast() == false) {
                	if(cur.getInt(1) > loc)
                		UpdateLoc(cur.getInt(1), cur.getInt(1)-1);
               	    cur.moveToNext();
                }
                cur.close();
                
                lastLoc--;
                UpdateView();
            }
        });
    }
	
	@Override
    protected void onStop(){
       super.onStop();
       db.close();
    }
	
	private void InsertRecords(int location, String response)
	{
		ContentValues values = new ContentValues();
        values.put("name", response);
        values.put("location", location);
        if(db != null)
        	db.insert("LastText", null, values);
	}
	
	private void UpdateLoc(int locOld, int locNew)
	{
		ContentValues values = new ContentValues();
		values.put("location", locNew);
		db.update("LastText", values, "location=?", new String[] {Long.toString(locOld)});
	}
	
	private void UpdateView()
	{
		String[] items;
		
		Cursor cur = db.query("LastText", null, null, null, null, null, null);
        cur.moveToNext();
        items = new String[cur.getCount()];
        
        while (cur.isAfterLast() == false) {
        	items[cur.getPosition()] = cur.getString(1)+ ":"+cur.getString(2);
       	    cur.moveToNext();
        }
        cur.close();
		ListView lv=(ListView)findViewById(R.id.LastTextList);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , items));
	}
	
	
}
