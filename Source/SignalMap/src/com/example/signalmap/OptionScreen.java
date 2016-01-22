package com.example.signalmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class OptionScreen extends Activity {
	
	Button phone;
	Button wifi;
	 
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.optionscreen);
	        
	        phone = (Button) findViewById (R.id.button1);
	        wifi = (Button) findViewById (R.id.button2);
	        
	        phone.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent i = new Intent(OptionScreen.this, MainActivity.class);
		                startActivity(i);
					
					}
			});
	        
  wifi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					 Intent i = new Intent(OptionScreen.this, WifiActivity.class);
		                startActivity(i);
					
				
				}
			});
	        
	        
		}
}
