package com.aplikasi.countdistance

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.aplikasi.countdistance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var latitute: Double = 0.0
    private var longitude: Double = 0.0

    private var latitudeOld : Double = -6.4092631
    private var longitudeOld : Double = 106.9075295

    private var newDistance : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvDistance.visibility = View.GONE

        binding.btnKlik.setOnClickListener {
            Log.d("MainActivity", "onCreate: btnKlik")
            showDialog()
        }
    }

    private fun showDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Peringatan")
        alertDialog.setMessage("Tolong izinkan aplikasi untuk mengakses lokasi anda")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            permission()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
            Toast.makeText(this, "Izin ditolak", Toast.LENGTH_SHORT).show()
        }
        alertDialog.show()
    }

    private fun permission() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Apa Hayo")
                alertDialog.setMessage("Siap-siap ya")
                alertDialog.setPositiveButton("OK") { dialog, which ->
                    getLocation()
                }
                alertDialog.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                    Toast.makeText(this, "Izin ditolak", Toast.LENGTH_SHORT).show()
                }
                alertDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        val location = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER)
            return
        } else {
            locationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER)
        }

        latitute = location!!.latitude
        longitude = location.longitude

        getDistance(latitute, longitude, latitudeOld, longitudeOld)

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Coba Tebak")
        alertDialog.setMessage("Seberapa jauh kita?")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            showDialogLagiPart1()
        }
        alertDialog.show()


    }

    private fun showDialogLagiPart1() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Pesan")
        alertDialog.setMessage("Kita sekarang berjarak $newDistance km")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            showDialogLagi()
        }
        alertDialog.show()
    }

    private fun showDialogLagi() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Pesan")
        alertDialog.setMessage("Have a nice day ayankk <3")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            finish()
        }
        alertDialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun getDistance(
        latitute: Double,
        longitude: Double,
        latitudeOld: Double,
        longitudeOld: Double
    ) {
        val LocationNow = android.location.Location("LocationNow")
        LocationNow.latitude = latitute
        LocationNow.longitude = longitude

        val LocationOld = android.location.Location("LocationOld")
        LocationOld.latitude = latitudeOld
        LocationOld.longitude = longitudeOld

        val distance = LocationNow.distanceTo(LocationOld)/1000
        if (distance < 1) {
            newDistance = String.format("%.2f", distance)
        } else {
            newDistance = String.format("%.2f", distance)
        }

    }
}