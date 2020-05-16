package com.yrdtechnologies.room

import com.yrdtechnologies.pojo.PojoAllProfile


data class AllProfile (val type:String, val name:String, val userSelected:Int)

fun AllProfile.toPojoAllProfile() = PojoAllProfile(this.name,this.type,this.userSelected==1)