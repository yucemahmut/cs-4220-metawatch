                                                                     
                                                                     
                                                                     
                                             
 /*****************************************************************************
  *  Copyright (c) 2011 Meta Watch Ltd.                                       *
  *  www.MetaWatch.org                                                        *
  *                                                                           *
  =============================================================================
  *                                                                           *
  *  Licensed under the Apache License, Version 2.0 (the "License");          *
  *  you may not use this file except in compliance with the License.         *
  *  You may obtain a copy of the License at                                  *
  *                                                                           *
  *    http://www.apache.org/licenses/LICENSE-2.0                             *
  *                                                                           *
  *  Unless required by applicable law or agreed to in writing, software      *
  *  distributed under the License is distributed on an "AS IS" BASIS,        *
  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
  *  See the License for the specific language governing permissions and      *
  *  limitations under the License.                                           *
  *                                                                           *
  *****************************************************************************/

 /*****************************************************************************
  * MetaWatch.java                                                            *
  * MetaWatch                                                                 *
  * Main activity with menu                                                            *
  *                                                                           *
  *                                                                           *
  *****************************************************************************/

package org.metawatch.manager;

import org.metawatch.manager.MetaWatchService.Preferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MetaWatch extends Activity {

	public static final String TAG = "MetaWatch";
	private ListView mainLV;
	Context context;
	private static final String MAIN_ITEM1 = "Last Text";
	private static final String MAIN_ITEM2 = "Fake Call";
	private static final String MAIN_ITEM3 = "RSS Feed";
	private static final String MAIN_ITEM4 = "E-mail";
	private String mainLVItems[]={MAIN_ITEM1,MAIN_ITEM2,MAIN_ITEM3,MAIN_ITEM4};
	MetaWatch CurrAct = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        mainLV=(ListView)findViewById(R.id.mainListView);
        mainLV.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , mainLVItems));
        
        mainLV.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
            	
            	String item = (String) ((TextView) view).getText();
            	
            	if(item.equals(MAIN_ITEM1))
            	{
            		startActivity(new Intent(context, LastText.class));
            	} 
            	else if(item.equals(MAIN_ITEM2))
            	{
            		startActivity(new Intent(context, FakeCall.class));
            	} 
            	else if(item.equals(MAIN_ITEM3))
            	{
            		startActivity(new Intent(context, RSS.class));
            	} 
            	else if(item.equals(MAIN_ITEM4))
            	{
            		startActivity(new Intent(context, Email.class));
            	}
            	
            	// When clicked, show a toast with the TextView text
            	//Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		MetaWatchService.loadPreferences(this);
		
		if (Preferences.idleMusicControls)
			Protocol.enableMediaButtons();
		//else 
			//Protocol.disableMediaButtons();
		
		if (Preferences.idleReplay)
			Protocol.enableReplayButton();
		//else
			//Protocol.disableReplayButton();
		
		Protocol.configureMode();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.start:
	    	startService();
	        return true;
	    case R.id.stop:	   
	    	stopService();
	        return true;
	    case R.id.test:
	    	startActivity(new Intent(this, Test.class));
	        return true;
	    case R.id.settings:	 
	    	startActivity(new Intent(this, Settings.class));
	        return true;
	    case R.id.about:
	    	showAbout();
	        return true;
	    case R.id.exit:	        
	    	exit();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
	void startService() {
		startService(new Intent(this, MetaWatchService.class));
	}
	
    void stopService() {
    	stopService(new Intent(this, MetaWatchService.class));
    }
    
    void exit() {
    	System.exit(0);
    }
    
    void showAbout() {
    	
    	WebView webView = new WebView(this);
		String html = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><title>About</title></head><body>" + 
						"<h1>MetaWatch</h1>" +
						"<p>Version " + Utils.getVersion(this) + ".</p>" +
						"<p>� Copyright 2011 Meta Watch Ltd.</p>" +
						"</body></html>";
        webView.loadData(html, "text/html", "utf-8");
        
        new AlertDialog.Builder(this).setView(webView).setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {			
			//@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();        
    }
    
}