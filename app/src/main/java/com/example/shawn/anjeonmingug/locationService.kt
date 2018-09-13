package com.example.shawn.anjeonmingug

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast

class locationService() : Service(){
    val BROADCAST_ACTION = "Hello World"
    private val TWO_MINUTES = 1000 * 60 * 2
    var locationManager:LocationManager? = null
    var listener:MyLocationListener? = null
    var previousBestLocation:Location? = null
    internal var intent:Intent? = null
    internal var counter = 0

    var isBool :Boolean = true
    override fun onCreate() {
        super.onCreate()
        intent = Intent(BROADCAST_ACTION)
    }
    @SuppressLint("MissingPermission")
    override fun onStart(intent:Intent, startId:Int) {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        listener = MyLocationListener()
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0F, listener)
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0F, listener)
    }
    override fun onBind(intent:Intent): IBinder? {
        return null
    }
    protected fun isBetterLocation(location:Location, currentBestLocation:Location):Boolean {
        if (currentBestLocation == null)
        {
            // A new location is always better than no location
            return true
        }
        // Check whether the new location fix is newer or older
        val timeDelta = location.getTime() - currentBestLocation.getTime()
        val isSignificantlyNewer = timeDelta > TWO_MINUTES
        val isSignificantlyOlder = timeDelta < -TWO_MINUTES
        val isNewer = timeDelta > 0
        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer)
        {
            return true
            // If the new location is more than two minutes older, it must be worse
        }
        else if (isSignificantlyOlder)
        {
            return false
        }
        // Check whether the new location fix is more or less accurate
        val accuracyDelta = (location.getAccuracy() - currentBestLocation.getAccuracy()) as Int
        val isLessAccurate = accuracyDelta > 0
        val isMoreAccurate = accuracyDelta < 0
        val isSignificantlyLessAccurate = accuracyDelta > 200
        // Check if the old and new location are from the same provider
        val isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider())
        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate)
        {
            return true
        }
        else if (isNewer && !isLessAccurate)
        {
            return true
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider)
        {
            return true
        }
        return false
    }
    /** Checks whether two providers are the same */
    private fun isSameProvider(provider1:String, provider2:String):Boolean {
        if (provider1 == null)
        {
            return provider2 == null
        }
        return provider1 == provider2
    }
    override fun onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy()
        Log.v("STOP_SERVICE", "DONE")
        locationManager!!.removeUpdates(listener)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_NOT_STICKY
    }
    inner class MyLocationListener: LocationListener {
        @SuppressLint("LongLogTag")
        override fun onLocationChanged(loc:Location) {
            Log.i("**************************************", "Location changed")
            Log.i("**************************************", "Location changed$loc")

            loc.getLatitude()
            loc.getLongitude()

            // appendLocation in here
            /*var intent= Intent("android.intent.action.RUN")
            intent!!.putExtra("string", "soicem")
            intent!!.putExtra("Latitude", loc.getLatitude())
            intent!!.putExtra("Longitude", loc.getLongitude())
            intent!!.putExtra("Provider", loc.getProvider())

            sendBroadcast(intent)*/
        }
        override fun onProviderDisabled(provider:String) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show()
        }
        override fun onProviderEnabled(provider:String) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show()
        }
        override fun onStatusChanged(provider:String, status:Int, extras:Bundle) {
        }
    }

    fun performOnBackgroundThread(runnable: Runnable): Thread {
        val t = object : Thread() {
            override fun run() {
                try {
                    runnable.run()
                } finally {

                }
            }
        }
        t.start()
        return t
    }
}
