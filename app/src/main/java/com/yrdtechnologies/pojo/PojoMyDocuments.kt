package com.yrdtechnologies.pojo

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by kartikshah on 17/02/18.
 */
class PojoMyDocuments : Parcelable {
    var filePath: String?=null
    var name: String? =null
    var startDate: String?=null
    var endDate: String?=null
    var details: String?=null
    var type: String?=null
    var drName: String?=null
    var shortDesc: String?=null
    var longDesc: String?=null
    var hospital: String?=null

    constructor(parcel: Parcel) {
        filePath = parcel.readString()
        name = parcel.readString()
        startDate = parcel.readString()
        endDate = parcel.readString()
        details = parcel.readString()
        type = parcel.readString()
        drName = parcel.readString()
        hospital = parcel.readString()
        shortDesc = parcel.readString()
        longDesc = parcel.readString()
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(filePath)
        dest.writeString(name)
        dest.writeString(startDate)
        dest.writeString(endDate)
        dest.writeString(details)
        dest.writeString(type)
        dest.writeString(drName)
        dest.writeString(hospital)
        dest.writeString(shortDesc)
        dest.writeString(longDesc)
    }

    companion object {
        val CREATOR: Parcelable.Creator<PojoMyDocuments> = object : Parcelable.Creator<PojoMyDocuments> {
            override fun createFromParcel(parcel: Parcel): PojoMyDocuments {
                return PojoMyDocuments(parcel)
            }

            override fun newArray(size: Int): Array<PojoMyDocuments?> {
                return arrayOfNulls(size)
            }
        }
    }
}