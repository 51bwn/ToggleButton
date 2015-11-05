package com.example.buttonon;
import com.example.buttonon.SwitchButton.OnChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
public class MainActivity extends Activity{
	protected static final String TAG="MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SwitchButton viewBtn=(SwitchButton) findViewById(R.id.btn_switch);
		viewBtn.setOnChangeListener(new OnChangeListener(){
			@Override
			public void onChanage(boolean value){
				Log.i(TAG,"当前开关:"+value);
			Toast.makeText(MainActivity.this,"当前开关;"+value,Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main,menu);
		return true;
	}
}
