package net.lephones.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoaderActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = new Intent();
        intent.setClassName(getApplicationContext(), "com.lephones.net.BActivity");
        startActivity(intent);
        
    }
}