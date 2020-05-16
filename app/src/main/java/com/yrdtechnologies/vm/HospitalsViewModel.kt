package com.yrdtechnologies.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yrdtechnologies.R
import com.yrdtechnologies.pojo.maps.Hospitals
import com.yrdtechnologies.pojo.maps.Result
import com.yrdtechnologies.utility.FileUtils
import com.yrdtechnologies.utility.Utils

class HospitalsViewModel(val app:Application):AndroidViewModel(app){
    fun getResults(latitude:Double, longitude:Double):List<Result>{
        val inputStream = app.applicationContext.resources.openRawResource(R.raw.hospitals)
        val json = FileUtils.streamToString(inputStream)
        val gson = Gson()
        val collectionType = object : TypeToken<Hospitals?>() {}.type
        val hospitals = gson.fromJson<Hospitals>(json, collectionType)
        val results = hospitals.results
        for (result in results) {
            val geometry = result.geometry
            val placeLocation = geometry.location
            result.distance = Utils.distanceInKm(latitude, longitude,
                    placeLocation.lat, placeLocation.lng)
        }
        results.sort()
        return results
    }
}