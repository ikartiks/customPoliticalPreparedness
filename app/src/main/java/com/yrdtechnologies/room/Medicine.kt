package com.yrdtechnologies.room

import com.yrdtechnologies.persistence.TableMedicines
import com.yrdtechnologies.pojo.PojoMedicine

data class Medicine(val name:String,var userSelected:Int,val type:Int, var drName:String?,var startDate:String?,
                    var endDate:String?, var evening:String?, var morning:String?, var night:String?)

fun Medicine.toPojoMedicine() :PojoMedicine {
    val medicine=  PojoMedicine()
    medicine.name = this.name
    medicine.type = TableMedicines.typeList
    return medicine
}