package com.yrdtechnologies.vm

import android.app.Application
import android.content.ContentValues
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.yrdtechnologies.network.RetrofitClientInstance.retrofitInstance
import com.yrdtechnologies.network.ServicesInterface
import com.yrdtechnologies.persistence.AllMyProfileTable
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.persistence.TableDoctors
import com.yrdtechnologies.persistence.TableMedicines
import com.yrdtechnologies.room.AllProfile
import com.yrdtechnologies.room.Doctor
import com.yrdtechnologies.room.Medicine
import com.yrdtechnologies.room.toPojoDoctor
import com.yrdtechnologies.room.toPojoMedicine
import com.yrdtechnologies.utility.DBUtility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashViewModel(val app: Application):AndroidViewModel(app){

    var dbUtility = DBUtility(app.applicationContext)
    var itemsInserted = 0

    fun checkIfDataInserted():Boolean{
        return dbUtility.checkIfDataIsInserted()
    }

    fun downloadData(completion:()->Unit){

        val service = retrofitInstance!!.create(ServicesInterface::class.java)
        val call=  service.fetchDocs()
        call.enqueue(object : Callback<List<Doctor>> {
            override fun onFailure(call: Call<List<Doctor>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Doctor>>, response: Response<List<Doctor>>) {
                if(response.isSuccessful){
                    val list = response.body()!!.map { it.toPojoDoctor() }
                    val contentValues = list.map {
                        val contentValues = ContentValues()
                        contentValues.put(TableDoctors.name, it.name)
                        contentValues.put(TableDoctors.city, it.city)
                        contentValues.put(TableDoctors.degree, it.degree)
                        contentValues.put(TableDoctors.contact, it.contact)
                        contentValues.put(TableDoctors.pin, it.pin)
                        contentValues.put(TableDoctors.speciality, it.speciality)
                        contentValues.put(TableDoctors.state, it.state)
                        contentValues.put(TableDoctors.rating, it.rating)
                        contentValues.put(TableDoctors.userSelected, 0)
                        contentValues
                    }
                    dbUtility.insertDrs(contentValues = contentValues.toTypedArray())
                    dbUtility.insertDrugs()
                    dbUtility.insertGenetic()
                    dbUtility.insertKnown()
                    dbUtility.insertFamily()
                    dbUtility.insertFood()
                    itemsInserted ++
                }
            }
        })

        val call2=  service.fetchMedicines()
        call2.enqueue(object : Callback<List<Medicine>> {
            override fun onFailure(call: Call<List<Medicine>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Medicine>>, response: Response<List<Medicine>>) {
                if(response.isSuccessful){

                    val list = response.body()!!.map { it.toPojoMedicine() }
                    val contentValues = list.map {
                        val contentValues = ContentValues()
                        contentValues.put(TableMedicines.name, it.name)
                        contentValues.put(TableMedicines.type, TableMedicines.typeList)
                        contentValues
                    }
                    dbUtility.insertMedicinesFromAPI(contentValues.toTypedArray())
                    itemsInserted++
                }
            }
        })

        checkIfAllInserted(completion)
    }

    private fun checkIfAllInserted(completion:()->Unit){
        Handler().postDelayed({
            if(itemsInserted == 2){
                completion.invoke()
            }else{
                checkIfAllInserted(completion)
            }
        },3000)
    }
}