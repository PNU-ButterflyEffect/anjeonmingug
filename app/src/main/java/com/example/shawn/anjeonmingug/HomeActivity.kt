package com.example.shawn.anjeonmingug
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.view.View

import net.daum.mf.map.api.MapView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_home.*
import net.daum.mf.map.api.MapPoint

class HomeActivity() : AppCompatActivity(), LocationListener {
    var latitude : Double? = null
    var longitude : Double? = null
    var mapView : MapView? = null
    override fun onLocationChanged(p0: Location?) {
        println(p0!!.latitude)
        println(p0!!.longitude)
        latitude = p0!!.latitude
        longitude = p0!!.longitude
        this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);

    }



    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
        println("p0  :::::::::" + p0)
    }

    override fun onProviderDisabled(p0: String?) {
    }

    var locationManager : LocationManager? =null


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)

        this.mapView = MapView(this)
        this.mapView!!.setDaumMapApiKey("6f504f9b73ad280372b2aff0036b6f32")

        val container = findViewById<View>(R.id.map_view) as RelativeLayout
        container.addView(mapView)
        gps_icon.bringToFront()



        gps_icon.setOnClickListener(){
            this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0)
            getLocation()
    }

}


