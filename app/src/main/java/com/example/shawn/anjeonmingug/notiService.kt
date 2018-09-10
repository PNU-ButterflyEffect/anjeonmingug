package com.example.shawn.anjeonmingug

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class notiService : FirebaseMessagingService(), LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 0)
            getLocation()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?) {
        if(this.latitude == p0!!.latitude && this.longitude == p0.longitude){
            println("same place!");
        } else {
            this.latitude = p0.latitude
            this.longitude = p0.longitude
            println(this.latitude!!)
            println(this.longitude!!)
        }
    }

    var locationManager : LocationManager? =null
    var latitude : Double? =null
    var longitude : Double? = null
    override fun onCreate() {
        super.onCreate()
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage!!.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage!!.getData().size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification()!!.getBody());
        }
    }
    fun getLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 100f, this)
            } else {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100f, this)
            }
        }
    }
}
