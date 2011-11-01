package org.metawatch.manager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class FakeCall extends Activity
{	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fakecall);
        Toast.makeText(getApplicationContext(), "Something", Toast.LENGTH_SHORT).show();
	}
}