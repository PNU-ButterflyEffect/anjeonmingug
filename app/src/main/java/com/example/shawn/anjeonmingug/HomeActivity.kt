package com.example.shawn.anjeonmingug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View

import net.daum.mf.map.api.MapView
import android.widget.RelativeLayout
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_home.*
import net.daum.mf.map.api.MapView.MapViewEventListener


class HomeActivity() : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mapView = MapView(this)
        mapView.setDaumMapApiKey("6f504f9b73ad280372b2aff0036b6f32")

        val container = findViewById<View>(R.id.map_view) as RelativeLayout
        container.addView(mapView)

        gps_icon.bringToFront()
        gps_icon.setOnClickListener(){

        }

    }

}


