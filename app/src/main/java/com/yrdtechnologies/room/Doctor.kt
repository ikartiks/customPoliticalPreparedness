package com.yrdtechnologies.room

import com.yrdtechnologies.pojo.PojoDoctor

data class Doctor (val speciality:String, val city:String,val degree:String, val pin:String,val rating:String,val contact:String,val state:String,
var userSelected:Int,val name:String,var shortDesc:String?, var longDesc:String?,var fileType:String?,var filePath:String?,var visitDate:String?)

fun Doctor.toPojoDoctor():PojoDoctor{
    val pojoDoctor= PojoDoctor()
    pojoDoctor.name = this.name
    pojoDoctor.speciality = this.speciality
    pojoDoctor.city= this.city
    pojoDoctor.degree= this.degree
    pojoDoctor.pin= this.pin
    pojoDoctor.rating= this.rating
    pojoDoctor.contact= this.contact
    pojoDoctor.state= this.state
    pojoDoctor.isSelected= this.userSelected==1
    pojoDoctor.name= this.name
    pojoDoctor.shortDesc= this.shortDesc
    pojoDoctor.longDesc= this.longDesc
    pojoDoctor.filePath= this.filePath
    pojoDoctor.fileType= this.filePath
    pojoDoctor.visitDate= this.visitDate
    return pojoDoctor
}