package com.example.shawn.anjeonmingug

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View

import net.daum.mf.map.api.MapView
import android.widget.RelativeLayout
import android.view.ViewGroup
import net.daum.mf.map.api.MapView.MapViewEventListener


class HomeActivity() : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mapView = MapView(this)
        mapView.setDaumMapApiKey("d75b37b88a66fea4c1b561f042400150")

        val container = findViewById<View>(R.id.map_view) as RelativeLayout
        container.addView(mapView)

    }

}


