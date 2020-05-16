package com.yrdtechnologies.network

import com.yrdtechnologies.room.Doctor
import com.yrdtechnologies.room.Medicine
import retrofit2.Call
import retrofit2.http.GET

interface ServicesInterface {

    @GET("doctor.json")
    fun fetchDocs():Call<List<Doctor>>

    @GET("medicines.json")
    fun fetchMedicines():Call<List<Medicine>>
}