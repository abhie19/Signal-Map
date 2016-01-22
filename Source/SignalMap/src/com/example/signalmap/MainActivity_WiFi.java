package com.example.signalmap;


import java.util.List;

import com.example.signalmap.GPSTracker;
import com.example.signalmap.SignalDbAdapter;
import com.example.signalmap.R;
import com.example.signalmap.SignalDbAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity_WiFi extends Activity implements LocationListener{
	
	
	
	private TextView latituteField;
	private TextView longitudeField;
	private LocationManager locationManager;
	private String provider;
	public double a ;
	public double b ;
	int percentage;
	public int strengthAmplitude;
	
	private final LatLng LOCATION_MPSTME=new LatLng(21.2856,74.8477);
	private final LatLng LOCATION_SS=new LatLng(19.1154,72.8433);
	private final LatLng LOCATION_MUMBAI=new LatLng(18.9750,72.8258);

	//mod
	public static final int INSERT_ID = Menu.FIRST;
	private int mNoteNumber = 1;
    private SignalDbAdapter mDbHelper;
    double col1Value;
    double col2Value;
    String simOperatorName;
    String simCountry,phoneNumber;
    int networkType;
    double c1;
    double c2;
	
	private GoogleMap map;
	
	SignalStrengthListener signalStrengthListener;
	TextView signalStrengthTextView;
	//Added Mod
	TextView tv3,tv4,tv5,testtext;
	 static WifiManager wifi;
	    BroadcastReceiver receiver;


	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //mod
        mDbHelper= new SignalDbAdapter(this);
        mDbHelper.open();
      
        
        
        map=((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        signalStrengthTextView = (TextView)findViewById(R.id.tv1);
        tv5=(TextView)findViewById(R.id.tv5);

        //Added mod
        tv3=(TextView)findViewById(R.id.tv3);
        
       
        
        
        
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Get WiFi status
        WifiInfo info = wifi.getConnectionInfo();
        tv3.append("\n\nWiFi Status: " + info.toString());

        // List available networks
        List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            tv3.append("\n\n" + config.toString());
        }

        // Register Broadcast Receiver
        if (receiver == null)
           // receiver = new WifiScanReceiver();

        registerReceiver(receiver, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        String TAG = null;
		Log.d(TAG, "onCreate()");
    }

	
    @Override
    public void onStop() {
        unregisterReceiver(receiver);
            super.onStop();
  
     
        
       
        
        //Added mod to get sim details
      
     
        
        //Added Mod
        GPSTracker gps = new GPSTracker(this);
        a= gps.getLatitude();
        b= gps.getLongitude();
        
        

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            double lat = (double) (location.getLatitude());
            double lng = (double) (location.getLongitude());
           
            } else {
            	Toast.makeText(this, "Provider not available",
                        Toast.LENGTH_SHORT).show();
            	//signalStrengthTextView.setText("Provider not available");
        
            }

    }
	
	//onCreate ends
	
		
	
	
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
        a = lat;
        b = lng;
        }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disenabled provider " + provider,
                Toast.LENGTH_SHORT).show();

        
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

        
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onResume() {
        locationManager.requestLocationUpdates(provider, 500, 1, this);
        super.onResume();
    }
    
    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    
    
   


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //mod
    	/*getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
       */
    	 boolean result = super.onCreateOptionsMenu(menu);
         menu.add(0, INSERT_ID, 0, "Save entry");
         menu.add(0, 2, 0, "Retrieve");
         return result;
         
    }
    
    //mod method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
    	
    	 switch (item.getItemId()) {
         case INSERT_ID:
             createNote();
             return true;
             
         case 2:
             fillData();
             return true;
             
         }
        return super.onOptionsItemSelected(item);
    }
       
    private void createNote() {
            double noteName = a;
            double noteName2 =b;
            mDbHelper.createNote(noteName, noteName2);
            
            Toast.makeText(this, "Entry created in Db",
                    Toast.LENGTH_SHORT).show();
           // fillData();
          
           //fillData();
        }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllNotes();
        if(c!=null) {
            c.moveToLast();


            while(!c.isAfterLast()) {

                col1Value = c.getDouble(1);//here you get col1 value
                col2Value = c.getDouble(2);//here you get col2 value
                
                Toast.makeText(this, "Entry Created at " + col1Value+ " & "+col2Value,
                Toast.LENGTH_SHORT).show();
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(col1Value,col2Value),15);
                map.animateCamera(update);
                               map.addMarker(new MarkerOptions().position(new LatLng(col1Value,col2Value)).title(strengthAmplitude+" asu"));
                
               
                c.move(4);
            }
            c.deactivate();
            c.close();
        }
        
        
        
     
       
       // Toast.makeText(this, "Entry Create as " + col1Value+ " & "+col2Value,
         //       Toast.LENGTH_SHORT).show();
       
        
       // c1 = Double.parseDouble(col1Value);
        //c2 = Double.parseDouble(col2Value);
       
      //  testtext.setText(String.valueOf("Db = "+col1Value +" &" + col2Value));
        
        
        

               
}
        
  
    public void onClick_City(View v)
    {
    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	 CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_MUMBAI,15);
         map.animateCamera(update);
        map.addMarker(new MarkerOptions().position(LOCATION_MUMBAI).title("Mumbai"));
    
    }
    
    
    public void onClick_SS(View v)
    {
    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	 CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_SS,15);
         map.animateCamera(update);
        
        map.addMarker(new MarkerOptions().position(LOCATION_SS).title("Shoppers Stop"));
    }
    
    
   public void onClick_BtnSubmit(View v)
    {
    	
    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(a,b),15);
        map.animateCamera(update);
       
        map.addMarker(new MarkerOptions().position(new LatLng(a,b)).title(strengthAmplitude+" asu"));
        Toast.makeText(this, "Marker dropped at current location"+a+ " , "+b,
                Toast.LENGTH_SHORT).show();
        
    }
  
    
    private class SignalStrengthListener extends PhoneStateListener
    {
     @Override
     public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
       
        // get the signal strength (a value between 0 and 31)
        strengthAmplitude = signalStrength.getGsmSignalStrength();
       
       //do something with it (in this case we update a text view)
        
       signalStrengthTextView.setText(String.valueOf("Carrier: "+simOperatorName +  "     Signal Strength= "+	strengthAmplitude+" asu"));

       super.onSignalStrengthsChanged(signalStrength);
       
     }
}
}
