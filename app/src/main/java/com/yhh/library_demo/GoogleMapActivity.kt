package com.yhh.library_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {
    var googleMap:GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        var xCoord = intent.getStringExtra("x")!!.toDouble()
        var yCoord = intent.getStringExtra("y")!!.toDouble()
        var name = intent.getStringExtra("name")
        var addr = intent.getStringExtra("addr")

        val coord: LatLng = LatLng(xCoord, yCoord)
        var markerOptions = MarkerOptions()
        markerOptions.position(coord)
        markerOptions.title(name)
        markerOptions.snippet(addr)
        googleMap?.addMarker(markerOptions)

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 17F))
    }
}