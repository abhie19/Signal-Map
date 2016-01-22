package com.example.signalmap;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.signalmap.GPSTracker;
import com.example.signalmap.SignalDbAdapter;
import com.example.signalmap.R;
//import com.example.signalmap.SignalDbAdapter;
//import com.example.test1_signalmap.AllProductsActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.signalmap.AllProductsActivity;
//import com.example.signalmap.AllProductsActivity.LoadAllProducts;


public class MainActivity extends Activity implements LocationListener{
	
	// Integration 1 starts
	 private ProgressDialog pDialog;
	 
	    JSONParser jsonParser = new JSONParser();
	    
	    JSONParser jParser = new JSONParser();

	    // url to create new product
	    private String url_create_product = "http://www.hbnchem.com/create_product.php";
	    ArrayList<HashMap<String, String>> networksList;

	    // url to get all products list
	    public String url_all_products;

	    // JSON Node names
	    ArrayList<String>  valueArray;
	 //   private static final String TAG_SUCCESS = "success";
	    private static final String TAG_PRODUCTS = "products";
	    private static final String TAG_NET_NAME = "net_name";
	    private static final String TAG_ID = "id";
	    private static final String TAG_NET_TYPE = "net_type";
	    private static final String TAG_LATITUDE = "latitude";
	    private static final String TAG_LONGITUDE = "longitude";
	    private static final String TAG_STRENGTH = "sig_strength";
	    // JSON Node names
	    private static final String TAG_SUCCESS = "success";
	    private double MAX_DISTANCE = 12;

	    
	//integration 1 ends    
	private TextView latituteField;
	private TextView longitudeField;
	private LocationManager locationManager;
	private String provider;
	public double a ;
	public double b ;
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
    String net_name;
    String net_type;
	int sig_percent;
	int sig_strength;
	double latitude;
	double longitude;
	String marker_color;
	
    
	private GoogleMap map;
	
	JSONArray networks = null;
	
	
	
	SignalStrengthListener signalStrengthListener;
	TextView signalStrengthTextView;
	//Added Mod
	TextView tv3,tv4,tv5,testtext;

    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
    	    StrictMode.ThreadPolicy policy = 
    	    new StrictMode.ThreadPolicy.Builder().permitAll().build();      
    	        StrictMode.setThreadPolicy(policy);
    	 }
        
        //mod
        mDbHelper= new SignalDbAdapter(this);
        mDbHelper.open();
      

        
        map=((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        signalStrengthTextView = (TextView)findViewById(R.id.tv1);
        tv5=(TextView)findViewById(R.id.tv5);

        //Added mod
        tv3=(TextView)findViewById(R.id.tv3);
     
        tv5 = (TextView) findViewById(R.id.tv5);      

        
        networksList = new ArrayList<HashMap<String, String>>();

        
        
        signalStrengthListener = new SignalStrengthListener();	           
        TelephonyManager  telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
       telephonyManager.listen(signalStrengthListener,SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);
        //Added mod to get sim details
      
       //get network type
       if ((telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA)) {
           Log.d("Type", "3g");
           net_type="3G";
           tv5.setText(String.valueOf("Network Type: 3G"));
          
       } else if ((telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPAP)) {
           Log.d("Type", "4g"); 
           net_type="3G";
           tv5.setText(String.valueOf("Network Type: 3G"));
       
       } else if ((telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS)) {
           Log.d("Type", "GPRS");
           net_type="2G";
           tv5.setText(String.valueOf("Network Type: GPRS"));
       
       } else if ((telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE)) {
           Log.d("Type", "EDGE 2g");
           net_type="2G";
           tv5.setText(String.valueOf("Network Type: Edge 2G"));
       }
       else {
    	   Log.d("Type", "No network");
           net_type="Null";
           tv5.setText(String.valueOf("Network Type: Connected to Wifi"));
       }
      
       
       
        int simState = telephonyManager.getSimState();
         
        switch (simState) {
         
            case (TelephonyManager.SIM_STATE_ABSENT): break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED): break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED): break;
            case (TelephonyManager.SIM_STATE_UNKNOWN): break;
            case (TelephonyManager.SIM_STATE_READY): {
         
                // Get the SIM country ISO code
                    simCountry = telephonyManager.getSimCountryIso();
         
                
                // Get the name of the SIM operator
                    simOperatorName = telephonyManager.getSimOperatorName();
                    net_name =simOperatorName;
                    
                   
                    
                    networkType = telephonyManager.getNetworkType();
         
                  //mod
                    try {
                        File sd = Environment.getExternalStorageDirectory();
                        File data = Environment.getDataDirectory();

                        if (sd.canWrite()) {
                            String currentDBPath = "/data/" + getPackageName() + "/databases/mDb";
                            String backupDBPath = "signalmap.db";
                            File currentDB = new File(currentDBPath);
                            File backupDB = new File(sd, backupDBPath);

                            if (currentDB.exists()) {
                                FileChannel src = new FileInputStream(currentDB).getChannel();
                                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                dst.transferFrom(src, 0, src.size());
                                src.close();
                                dst.close();
                            }
                        }
                    } catch (Exception e) {

                    }
            }
            
            
         }
        
        //Added Mod

        
        //Added Mod
        GPSTracker gps = new GPSTracker(this);
        a= gps.getLatitude();
       latitude=a;
        b= gps.getLongitude();
       longitude=b;

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
           // double lat = (double) (location.getLatitude());
           // double lng = (double) (location.getLongitude());
           
            } else {
            	Toast.makeText(this, "Provider not available",
                        Toast.LENGTH_SHORT).show();
            	//signalStrengthTextView.setText("Provider not available");
        
            }
        //int mod start
        
        // button click event
       
                // creating new product in background thread
            
        //int mod end
      
    }
	
	//onCreate ends
	  //integration 2 starts
    

	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //mod
    	/*getMenuInflater().inflate(R.menu.main, menu);
        
        return true;
       */
    	 boolean result = super.onCreateOptionsMenu(menu);
         menu.add(0, 1, 0, "Vodafone ");
         menu.add(0, 2, 0, "Reliance ");
         menu.add(0, 3, 0, "15m Radius ");
         menu.add(0, 4, 0, "50m Radius ");

         return result;
         
    }
    
    //mod method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
    	
    	 switch (item.getItemId()) {
         case 1:
        	 url_all_products = "http://www.hbnchem.com/get_all_products_vodafone.php";
        	 map.clear();  
         	new LoadAllProducts().execute();
             return true;
             
         case 2:
             //for Reliance
        	 url_all_products = "http://www.hbnchem.com/get_all_products_reliance.php";
        	 map.clear();  
         	new LoadAllProducts().execute();
        	 return true;
         case 3:
        	 MAX_DISTANCE = 15;
        	 nearbyMarkers();
             return true;
             
         case 4:
        	 MAX_DISTANCE = 50;
        	 nearbyMarkers();
             return true;
             
             
         }
        return super.onOptionsItemSelected(item);
    }
	
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Creating Entry in database..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            /*String net_name2 = net_name;
            String price = inputPrice.getText().toString();
            String description = inputDesc.getText().toString();
             */     
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("net_name", net_name));
			Log.e("Alert","net name "+net_name);

            params.add(new BasicNameValuePair("net_type", net_type));
			Log.e("Alert","net type"+net_type);
			
			params.add(new BasicNameValuePair("sig_strength", String.valueOf(sig_strength)));
			Log.e("Alert","signal strength "+sig_strength);

			params.add(new BasicNameValuePair("sig_percent", String.valueOf(sig_percent)));
			Log.e("Alert","Signal percent"+sig_percent);

			params.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
			Log.e("Alert","latitude "+latitude);

			params.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
			Log.e("Alert","longitude"+longitude);

			params.add(new BasicNameValuePair("marker_color", marker_color));
			Log.e("Alert","marker color "+marker_color);

			// getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);
			Log.e("Alert","marker color2222"+marker_color);
         
            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully created product
 
                	Log.e("Alert","Entry saved in database");
                	
                	// Intent i = new Intent(getApplicationContext(), MainActivity.class);
                   // startActivity(i);
 
                    // closing this screen
                    //finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast toast = Toast.makeText(getApplicationContext(), "Entry successfully created in database.", Toast.LENGTH_SHORT);
            toast.show();
        }
 
    }
    
    //integration 2 ends 

	
    //Other class start
    
    
    
    
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            Log.e("Alert", " Stage 2- IN");
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
          
            // getting JSON string from URL
          
            return null;
        }
        
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            
            
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
            Log.e("Alert for success", "Value of success is " +TAG_SUCCESS);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                
                Log.e("Alert", " 11111 success is " +success);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
             	   Log.e("Alert", " i1 entre with success "+success );
                    networks = json.getJSONArray(TAG_PRODUCTS);
                    valueArray = new ArrayList<String>();
                  
                    // looping through All Products
                    for (int i = 0; i < networks.length(); i++) {
                        JSONObject c = networks.getJSONObject(i);
                        Log.e("Alert", " 2 enter ");
                        // Storing each json item in variable
                        
                        String id = c.getString(TAG_ID); //integration mod 1
                        Log.e("Alert "," Here id is " +id);
                        
                        String net_type = c.getString(TAG_NET_TYPE);
                        Log.e("Alert "," Here net_type is " +net_type);

                        String net_name = c.getString(TAG_NET_NAME); // integration mod 2
                        
                        String sig_strength = c.getString(TAG_STRENGTH);
                        
                        float latitude_1 = Float.parseFloat(c.getString(TAG_LATITUDE));
                        float longitude_1 = Float.parseFloat(c.getString(TAG_LONGITUDE));
                        int sig_strength_1 = Integer.parseInt(c.getString(TAG_STRENGTH));
                        
                        valueArray.add(new String(id));
                        valueArray.add(new String(net_name));
                        valueArray.add(new String(net_type));
                        valueArray.add(new String(c.getString(TAG_LATITUDE)));
                        valueArray.add(new String(c.getString(TAG_LONGITUDE)));
                        valueArray.add(new String(c.getString(TAG_STRENGTH)));
                        
                        placeMarker(latitude_1, longitude_1, sig_strength_1);
                    	
                	   Log.e("Alert", " in the loop for arrays with "+latitude_1+ "" +longitude_1+ " " +sig_strength_1);
                	   //Listing down the number of markers on the map
                	   Log.e("Marker list", "The marker list size is  " +markers.size());

                        // creating new HashMap
                  //      HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                  //      map.put(TAG_ID, id);
                  //      map.put(TAG_NET_NAME, net_name);

                        // adding HashList to ArrayList
                  //      networksList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                    /*Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                     */
             	   Log.e("Alert", " No networks found in the array");
                }
            } catch (JSONException e) {
         	   Log.e("Alert", " in the loop for arrays witaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah ");
                e.printStackTrace();
            }

            
            
            
            
            
            
            
            
            
            
            
            
            
            // updating UI from Background Thread
          /*  runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                   /* ListAdapter adapter = new SimpleAdapter(
                            AllProductsActivity.this, productsList,
                            R.layout.list_item, new String[] { TAG_ID,
                                    TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    
                    // updating listview
                    setListAdapter(adapter);
                */
        //     	   Log.e("Alert", "Doing post execute where u will have to set markers on the ");
      //          }
    //        });

            Log.e("Alert", "Doing post execute where u will have to set markers on the ");
            
            }
    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
	
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
                
                /*Toast.makeText(this, "Entry Created at " + col1Value+ " & "+col2Value,
                Toast.LENGTH_SHORT).show();
                */
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(col1Value,col2Value),15);
                map.animateCamera(update);
                               map.addMarker(new MarkerOptions().position(new LatLng(col1Value,col2Value)).title(sig_strength+" asu"));
                
              // Mod
              //c.move(4);
               c.moveToNext();                
            }
            
        }
        
        
        
     
       
       // Toast.makeText(this, "Entry Create as " + col1Value+ " & "+col2Value,
         //       Toast.LENGTH_SHORT).show();
       
        
       // c1 = Double.parseDouble(col1Value);
        //c2 = Double.parseDouble(col2Value);
       
      //  testtext.setText(String.valueOf("Db = "+col1Value +" &" + col2Value));
        
        
        

               
}
        
	List<Marker> markers = new ArrayList<Marker>();
	List<Integer> marker_strength = new ArrayList<Integer>();

	public void nearbyMarkers()
	{
		map.clear();
    	int minIndex = -1;
    	double minDist = 1E38; //initialising with a very high value that would be overwritten later
    	float[] distance = new float[2];
    	int size = markers.size();
    	for (int i=0; i<size; i++)
    	{
    		Marker marker = markers.get(i);
    		Integer marker_strengt = marker_strength.get(i);
    		Location.distanceBetween(a, b, marker.getPosition().latitude, marker.getPosition().longitude, distance);
            Log.e("Alert","checking at a nearest location "+i+" distance is  "+distance[0]);

    		if ( distance[0] < MAX_DISTANCE)
    		{
    			
    			if(marker_strengt <=8)
    			{
    				  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude),15);
    	                map.animateCamera(update);
    	               
    	                map.addMarker(new MarkerOptions().position(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude)).title(marker_strengt+" asu"));
    	                Log.e("Alert","marker dropped at a nearest location with distance = " +distance[0]+ " and strength = "+marker_strengt);

    			}
    			
    			
    			  else if( marker_strengt>=9  && marker_strengt<=20)
    				{
    					BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
    					 map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     	                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude),15);
     	                map.animateCamera(update);
     	               
     	                map.addMarker(new MarkerOptions().position(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude)).icon(bitmapDescriptor1).title(marker_strengt+" asu"));
     	                Log.e("Alert","marker dropped at a nearest location with distance = " +distance[0]+ " and strength = "+marker_strengt);

    				}
    				else
    				{

    					BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
    					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     	                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude),15);
     	                map.animateCamera(update);
     	               
     	                map.addMarker(new MarkerOptions().position(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude)).icon(bitmapDescriptor2).title(marker_strengt+" asu"));
     	                Log.e("Alert","marker dropped at a nearest location with distance = " +distance[0]+ " and strength = "+marker_strengt);


    				}
    		}
    		else{
                Log.e("Alert","No nearby markers ");

        	}
    	}
    	
    
	}
  
    public void onClick_City(View v)
    {
    	/*map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	 CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_MUMBAI,15);
         map.animateCamera(update);
        map.addMarker(new MarkerOptions().position(LOCATION_MUMBAI).title("Mumbai"));
        Log.e("Alert","marker color "+marker_color);
		*/
    	nearbyMarkers();
    	
    }
     
    
    public void onClick_SS(View v)
    {
    /*	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	 CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_SS,15);
         map.animateCamera(update);
        
        map.addMarker(new MarkerOptions().position(LOCATION_SS).title("Shoppers Stop"));
        Log.e("Alert","marker color 2 ");
*/
        
    	// Launching All products Activity
    	url_all_products = "http://www.hbnchem.com/get_all_products.php";
    	map.clear();  
    	new LoadAllProducts().execute();
    	 
    	 }
    
    
   public void onClick_BtnSubmit(View v)
    {
    	
    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(a,b),15);
        map.animateCamera(update);
       
        map.addMarker(new MarkerOptions().position(new LatLng(a,b)).title(sig_strength+" asu"));
        Toast.makeText(this, "Marker dropped at current location"+a+ " , "+b,
                Toast.LENGTH_SHORT).show();
        CreateNewProduct c = new CreateNewProduct();
        c.execute();
        Log.e("Alert","marker color 1 "+marker_color);

        
    }
   
   

   public void placeMarker(float latit, float longit, int Strengt)
   {   
	  
		if(Strengt<=8)
		{
	   	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	       CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latit,longit),15);
	       map.animateCamera(update);
	      
	       //put the markers in the marker list
	      Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(latit,longit)).title(Strengt+" asu"));
	      markers.add(marker);
	      marker_strength.add(Strengt);
		}

	   
	    else if( Strengt>=9  && Strengt<=20)
	{
		BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
   	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latit,longit),15);
       map.animateCamera(update);
       
       //put the markers in the marker list
	   Marker marker =  map.addMarker(new MarkerOptions().position(new LatLng(latit,longit)).icon(bitmapDescriptor1).title(Strengt+" asu"));
	   markers.add(marker);
	   marker_strength.add(Strengt);

	}
	else
	{

		BitmapDescriptor bitmapDescriptor2 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
   	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latit,longit),15);
       map.animateCamera(update);
       //put the markers in the marker list
	    
       Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(latit,longit)).icon(bitmapDescriptor2).title(Strengt+" asu"));
       markers.add(marker);
	   marker_strength.add(Strengt);

	}
       
   }
    
    private class SignalStrengthListener extends PhoneStateListener
    {
     @Override
     public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
       
        // get the signal strength (a value between 0 and 31)
    	 sig_strength = signalStrength.getGsmSignalStrength();
        
	    sig_percent  = (sig_strength*100)/31; 
	    
	    if(sig_strength<=8 && sig_strength>=0)
	    {
	    	marker_color="red";
			Log.e("Alert"," " +marker_color+" marker for signal value "+sig_strength);
	    }

	    else if(sig_strength<=20 && sig_percent>=9)
	    {
	    	marker_color="orange";
			Log.e("Alert"," " +marker_color+" marker for signal value "+sig_strength);
	    }

	    else //if(sig_strength>=21)
	    {
	    	marker_color="green";
			Log.e("Alert"," " +marker_color+" marker for signal value "+sig_strength);

	    }
       //do something with it (in this case we update a text view)
        
       signalStrengthTextView.setText(String.valueOf("Carrier: "+simOperatorName +  "     Signal Strength= "+	sig_strength+" asu"));
       //tv5.setText(String.valueOf("Network Type: HSPA")); 
       
       super.onSignalStrengthsChanged(signalStrength);
       
     }
}
   
}
