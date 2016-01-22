package com.example.signalmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.signalmap.MainActivity;

public class AllProductsActivity extends Activity {

   // Progress Dialog
   private ProgressDialog pDialog;

   // Creating JSON Parser object
   JSONParser jParser = new JSONParser();

   ArrayList<HashMap<String, String>> networksList;

   // url to get all products list
   private static String url_all_products = "http://192.168.1.16/get_all_products.php";

   // JSON Node names
   ArrayList<String>  valueArray;
   private static final String TAG_SUCCESS = "success";
   private static final String TAG_PRODUCTS = "products";
   private static final String TAG_NET_NAME = "net_name";
   private static final String TAG_ID = "id";
   private static final String TAG_NET_TYPE = "net_type";
   private static final String TAG_LATITUDE = "latitude";
   private static final String TAG_LONGITUDE = "longitude";
   private static final String TAG_STRENGTH = "sig_strength";
   
   private GoogleMap map;
   // products JSONArray
   JSONArray networks = null;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       if (android.os.Build.VERSION.SDK_INT > 9) {
   	    StrictMode.ThreadPolicy policy = 
   	    new StrictMode.ThreadPolicy.Builder().permitAll().build();      
   	        StrictMode.setThreadPolicy(policy);
   	 }
       Log.e("Alert", " Stage 0- IN");
       // Hashmap for ListView
       
       map=((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
       map.setMyLocationEnabled(true);
       
       networksList = new ArrayList<HashMap<String, String>>();
       
       Log.e("Alert", " Stage 1- IN");
   //     Loading products in Background Thread
  //     new LoadAllProducts().execute();
   }
       // Get listview
   //   ListView lv = getListView();

       // on seleting single product
       // launching Edit Product Screen
   //   lv.setOnItemClickListener(new OnItemClickListener() {

   //        @Override
   //        public void onItemClick(AdapterView<?> parent, View view,
   //                int position, long id) {
   //            // getting values from selected ListItem
   //            String pid = ((TextView) view.findViewById(R.id.pid)).getText()
   //                    .toString();

               // Starting new intent
   //            Intent in = new Intent(getApplicationContext(),
   //                    EditProductActivity.class);
               // sending pid to next activity
   //          in.putExtra(TAG_PID, pid);

               // starting new activity and expecting some response back
   //            startActivityForResult(in, 100);
   //        }
   //   });

   

   // Response from Edit Product Activity
   //@Override.
   //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   //    super.onActivityResult(requestCode, resultCode, data);
   //    // if result code 100
   //    if (resultCode == 100) {
   //        // if result code 100 is received
           // means user edited/deleted product
           // reload this screen again
   //       Intent intent = getIntent();
   //        finish();
   //        startActivity(intent);
   //  }

   //}

   /**
    * Background Async Task to Load all product by making HTTP Request
    * */
   class LoadAllProducts extends AsyncTask<String, String, String> {

       /**
        * Before starting background thread Show Progress Dialog
        * */
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(AllProductsActivity.this);
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
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           // getting JSON string from URL
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

                       float latitude = Float.parseFloat(c.getString(TAG_LATITUDE));
                       float longitude = Float.parseFloat(c.getString(TAG_LONGITUDE));
                       int sig_strength = Integer.parseInt(c.getString(TAG_STRENGTH));
                       
                       valueArray.add(new String(id));
                       valueArray.add(new String(net_name));
                       valueArray.add(new String(net_type));
                       
                       
                    	
                   
               	   Log.e("Alert", " in the loop for arrays with "+latitude+ "" +longitude+ " " +sig_strength);

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

           return null;
       }
       
       /**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog after getting all products
           pDialog.dismiss();
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
}
      
 
 
