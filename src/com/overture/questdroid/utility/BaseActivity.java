package com.overture.questdroid.utility;

import java.util.List;

import com.overture.questdroid.app.VolleyController;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	
	protected LocationManager manager;
	    @Override
	    protected void onStop() {
	        super.onStop();
	        if (!isAppOnForeground()) {
	            //Indicate that the App is in background
	        	VolleyController.getInstance().isActive = false;
	        }
	    }

        @Override
        protected void onResume() {
            super.onResume();
            
            if (!VolleyController.getInstance().isActive) {
                
            	//The current App is awoke, set the flag to true
            	VolleyController.getInstance().isActive = true;
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> lp = locationManager.getAllProviders();
                for (String item:lp){
                    Log.i("Snaapiq", "Available provider: "+item); 
                }
                Criteria criteria = new Criteria();  
                criteria.setCostAllowed(false);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                //getBestProvider only the available providers will be returned
                String  providerName = locationManager.getBestProvider(criteria, true);
                Log.i("Snaapiq", "------location services£º"+ providerName);
                if (providerName != null)
                {        
                    Location location = locationManager.getLastKnownLocation(providerName);
                    if(location != null){  
	                    Log.i("Snapiq", "-------"+location);    
	                    //get the latitude
	                    VolleyController.getInstance().latitude = location.getLatitude();
	                    //get the longitude
	                    VolleyController.getInstance().longitude = location.getLongitude();
                    }
                }
                else
                {
                      Toast.makeText(this, "Unable to get your local position\nPlease check your network settings", Toast.LENGTH_SHORT).show();
                }
            	
            	
            }
        }

        /**
         * check if the app is running on foreground
         * 
         * @return true or false
         */
        public boolean isAppOnForeground() {
                // Returns a list of application processes that are running on the
                // device
                
                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                String packageName = getApplicationContext().getPackageName();

                List<RunningAppProcessInfo> appProcesses = activityManager
                                .getRunningAppProcesses();
                if (appProcesses == null)
                        return false;

                for (RunningAppProcessInfo appProcess : appProcesses) {
                        // The name of the process that this object is associated with.
                        if (appProcess.processName.equals(packageName)
                                        && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                return true;
                        }
                }

                return false;
        }
}