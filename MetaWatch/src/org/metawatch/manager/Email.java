package org.metawatch.manager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Email extends Activity
{	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);
        Toast.makeText(getApplicationContext(), "Email Somthing2", Toast.LENGTH_SHORT).show();
	}
}