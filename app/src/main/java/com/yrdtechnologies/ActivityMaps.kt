package com.yrdtechnologies

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yrdtechnologies.pojo.maps.Hospitals
import com.yrdtechnologies.pojo.maps.Result
import com.yrdtechnologies.utility.DBUtility
import com.yrdtechnologies.utility.FileUtils
import com.yrdtechnologies.utility.Utils
import com.yrdtechnologies.vm.HospitalsViewModel
import kotlinx.android.synthetic.main.activity_maps.container
import kotlinx.android.synthetic.main.activity_maps.tabLayout

class ActivityMaps : ActivityBase(), OnMapReadyCallback, OnMarkerClickListener {
    private var mMap: GoogleMap? = null
    private val changeLocationSettings = 2
    private val permissionSendSms = 3
    private var allPermissionsGranted = false
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    lateinit var hospitalsViewModel: HospitalsViewModel

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    var currentLocation: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        hospitalsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(HospitalsViewModel::class.java)

//        FileUtils.deleteLogFile(this)
//        FileUtils.generateLogFileInDownloads(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        tabLayout.setupWithViewPager(container)
        val titles = arrayOf("Hospitals", "Emergency Contacts")
        for (i in titles.indices) {
            val v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val tabOne = v.findViewById<TextView>(R.id.tab)
            tabOne.text = titles[i]
            val imageX = v.findViewById<ImageView>(R.id.Image)
            //imageX.setImageDrawable(getResources().getDrawable(drawables[i]));
            imageX.visibility = View.GONE
            tabLayout.getTabAt(i)!!.customView = v
        }
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    @SuppressLint("WrongConstant")
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        val hospitalsFragment = FragmentHospitals.newInstance()!!


        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            if (position == 0) {
                return hospitalsFragment
            } else if (position == 1) {
                val fragmentEmergencyContact = FragmentEmergencyContact()
                val args = Bundle()
                args.putInt(FragmentEmergencyContact.ARG_COLUMN_COUNT, 1)
                args.putInt(FragmentEmergencyContact.hideAdd, 1)
                fragmentEmergencyContact.arguments = args
                return fragmentEmergencyContact
            }
            return FragmentHospitals.newInstance()!!
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val titles = arrayOf("Hospitals", "Emergency Contacts")
            return titles[position]
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationSettings()
    }

    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
    private val locationPermission: Unit
        get() {
            /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
            if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = true
                if (mMap != null) {
                    FileUtils.writeToLogFile("b4 usr location")
                    usersLocationAndCompute
                } else FileUtils.writeToLogFile("mMap null")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION),
                        permissionSendSms)
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            permissionSendSms -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = true
                    if (mMap != null) usersLocationAndCompute
                } else {
                    locationPermission
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == changeLocationSettings && resultCode == Activity.RESULT_OK) {
            locationPermission
        }
    }

    private fun updateLocationUI() {
        if (mMap == null) {
            FileUtils.writeToLogFile("mMap null updateLocationAPI")
            return
        }
        try {
            if (allPermissionsGranted) {
                mMap!!.isMyLocationEnabled = true
                mMap!!.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap!!.isMyLocationEnabled = false
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
                locationPermission
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.toString())
        }
    }

    fun checkLocationSettings() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(this) { // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            locationPermission
        }
        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@ActivityMaps, changeLocationSettings)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val result = marker.tag as Result?
        val location = result!!.geometry.location
        val uri = ("http://maps.google.com/maps?f=d&hl=en&saddr="
                + currentLocation!!.latitude + "," + currentLocation!!.longitude + "&daddr=" + location.lat + "," + location.lng)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        if (resolveActivity(intent)) startActivity(intent)
        return false
    }
    
    private val usersLocationAndCompute: Unit
        get() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return
            }
            FileUtils.writeToLogFile("Fetching location from cache")


//        This method returns null many times so not using it
            val locationResult: Task<*> = mFusedLocationClient!!.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    currentLocation = task.result as? Location?
                    if (currentLocation == null) {
                        FileUtils.writeToLogFile("Failed to fetch location from cache")
                        val request = LocationRequest()
                        request.interval = 60 * 1000.toLong()
                        request.fastestInterval = 30 * 1000.toLong()
                        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        mFusedLocationClient!!.requestLocationUpdates(request, MyLocationCallBack(), null)
                    } else {
                        FileUtils.writeToLogFile("Got location from cache")
                        doThingsOnceLocationIsAvailable()
                    }
                } else {
                    FileUtils.writeToLogFile("Failed to fetch location from cache")
                    val request = LocationRequest()
                    request.interval = 1 * 60 * 1000.toLong()
                    request.fastestInterval = 30 * 1000.toLong()
                    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    mFusedLocationClient!!.requestLocationUpdates(request, MyLocationCallBack(), null)
                }
            }
        }

    private fun sendSms() {
        if (allPermissionsGranted) {
            val dbUtility = DBUtility(this)
            val pojoFamilyArrayList = dbUtility.emergencyContacts
            val numbers = StringBuilder()
            val sms = StringBuilder("I need help. http://maps.google.com/maps?f=d&hl=en&saddr=&daddr=" + currentLocation!!.latitude + "," + currentLocation!!.longitude)
            sms.append(" \n Emergency Contacts \n ")
            for (pojoFamily in pojoFamilyArrayList) {
                sms.append(pojoFamily.name).append(" - ").append(pojoFamily.number).append(" \n ")
                numbers.append(pojoFamily.number + ";")
            }

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("smsto:$numbers") // This ensures only SMS apps respond
            intent.putExtra("sms_body", sms.toString())
            //intent.putExtra(Intent.EXTRA_STREAM, attachment);
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    inner class MyLocationCallBack : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            if (locationResult == null) {
                FileUtils.writeToLogFile("Unable to fetch users location, will retry.")
                return
            }
            currentLocation = locationResult.lastLocation
            if (currentLocation == null) {
                FileUtils.writeToLogFile("Unable to fetch users location, will retry.")
                return
            }
            FileUtils.writeToLogFile("got location")
            mFusedLocationClient!!.removeLocationUpdates(this)
            doThingsOnceLocationIsAvailable()
        }
    }

    fun doThingsOnceLocationIsAvailable() {
        val latitude = currentLocation!!.latitude
        val longitude = currentLocation!!.longitude
        updateLocationUI()
        //sendSms()
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(
                LatLng(currentLocation!!.latitude,
                        currentLocation!!.longitude), 12f))

        val results = hospitalsViewModel.getResults(latitude, longitude)
        for (result in results) {
            val geometry = result.geometry
            val placeLocation = geometry.location
            result.distance = Utils.distanceInKm(latitude, longitude,
                    placeLocation.lat, placeLocation.lng)
            val places = mMap!!.addMarker(MarkerOptions()
                    .position(LatLng(placeLocation.lat, placeLocation.lng))
                    .title(result.name))
            places.tag = result
        }
        val fragmentHospitals = mSectionsPagerAdapter!!.getItem(0) as FragmentHospitals
        fragmentHospitals.onLocationFound(latitude, longitude,results)


    }
}