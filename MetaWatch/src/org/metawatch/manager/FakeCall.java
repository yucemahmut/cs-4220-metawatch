package org.metawatch.manager;

import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings;

public class FakeCall extends Activity
{	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fakecall);
        //Toast.makeText(getApplicationContext(), "Something", Toast.LENGTH_SHORT).show();
        
        final Button button = (Button) findViewById(R.id.FakeCallSaveButton);
        final EditText name = (EditText) findViewById(R.id.FakeCallName); 
        final EditText phone = (EditText) findViewById(R.id.FakeCallPhone); 
        final SharedPreferences settings = getPreferences(MODE_WORLD_READABLE);
        
        name.setText(settings.getString("FakeCallName", "Enter Name Here:"));
        phone.setText(settings.getString("FakeCallPhone", "000-000-0000"));
        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	SharedPreferences.Editor editor = settings.edit();
            	editor.putString("FakeCallName", name.getText().toString());
            	editor.putString("FakeCallPhone", phone.getText().toString());
            	editor.commit();
            	Toast.makeText(getApplicationContext(), "Saved Data", Toast.LENGTH_SHORT).show();
            	Toast.makeText(getApplicationContext(), name.getText().toString(), Toast.LENGTH_SHORT).show();
            	PlayRingtone();
            }
        });
	}
	
	public void PlayRingtone()
	{
		MediaPlayer player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
		
		if(player == null)
		{
			Toast.makeText(getApplicationContext(), "Cannot Play Ringtone", Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.start();
	}
}