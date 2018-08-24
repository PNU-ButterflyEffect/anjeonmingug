package com.example.shawn.anjeonmingug

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_main.*
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import java.sql.Types.TIMESTAMP
import java.util.*

class HomeActivity() : AppCompatActivity(), LocationListener, NavigationView.OnNavigationItemSelectedListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    fun onFinishReverseGeoCoding(result : String) {
        println(result);
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
        println(p1);
        onFinishReverseGeoCoding(p1.toString());
    }


    var latitude : Double? = null
    var longitude : Double? = null
    var mapView : MapView? = null
    var database = FirebaseDatabase.getInstance()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // firebase realtime database를 위한 데이터베이스 table
    private fun appendLocation(latitude: Double, longitude: Double) {
        class UserLocation {
            var key : String
            var latitude: Double
            var longitude: Double
            var createdTime : Any

            constructor(key : String, latitude: Double, longitude: Double) {
                this.key = key
                this.latitude = latitude
                this.longitude = longitude
                val now = System.currentTimeMillis()
                val date = Date(now)
                val sdf = SimpleDateFormat("yyyy-MM-dd : hh-mm-ss")
                val getTime = sdf.format(date)
                this.createdTime = getTime
            }
        }

        var currentUser = FirebaseAuth.getInstance().currentUser
        var key = currentUser!!.uid
        var myRef = database.getReference()
        val userLocation =UserLocation(key!!, latitude, longitude)
        myRef.child("UserLocation").child(key!!).push().setValue(userLocation)
    }

    // 위치가 바뀔 때마다 실행
    override fun onLocationChanged(p0: Location?) {
        if(latitude == p0!!.latitude && longitude == p0!!.longitude){
            println("same place!");
        } else {
            latitude = p0!!.latitude
            longitude = p0!!.longitude
            appendLocation(latitude!!, longitude!!)
            this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    var locationManager : LocationManager? =null


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(button_menu)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, button_menu, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 권한 확인
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)

        // 가장 최근 경로 가져오기
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    this.latitude = location!!.latitude
                    this.longitude = location!!.longitude
                    appendLocation(this.latitude!!, this.longitude!!)
                    this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
                }

        // 지도 만들기
        this.mapView = MapView(this)
        this.mapView!!.setDaumMapApiKey("6f504f9b73ad280372b2aff0036b6f32")

        val container = findViewById<View>(R.id.map_view) as RelativeLayout
        container.addView(mapView)

        // 최상단으로 위치
        gps_icon.bringToFront()
        button_menu.bringToFront()
        editText_search.bringToFront()
        button_search.bringToFront()

        // gps 클릭 시 지도 위치 조정
        gps_icon.setOnClickListener(){
            this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
        }


        button_search.setOnClickListener(){
            //this.mapView!!.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude!!, longitude!!), true);
            val reverseGeoCoder = MapReverseGeoCoder("6f504f9b73ad280372b2aff0036b6f32", mapView!!.mapCenterPoint, this, this)
            reverseGeoCoder.startFindingAddress()
        }

    }



    // GPS, NETWORK로 위치 가져오기
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
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_bookmark -> {
                // Handle the camera action
            }
            R.id.nav_notice -> {

            }
            R.id.nav_option -> {
                var SettingActivity = Intent(this, SettingActivity::class.java)
                startActivity(SettingActivity)
            }

            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}


