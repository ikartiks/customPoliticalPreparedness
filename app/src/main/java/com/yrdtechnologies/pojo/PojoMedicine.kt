package com.yrdtechnologies.pojo

/**
 * Created by kartikshah on 04/03/18.
 */
class PojoMedicine {
    var name: String=""
    var startDate: String=""
    var endDate: String=""
    var morning: String=""
    var evening: String=""
    var night: String=""
    var drName: String=""
    var isUserSelected = false
    var type = 0

    constructor()
    constructor(name: String="",
                startDate: String="",
                endDate: String="",
                morning: String="",
                evening: String="",
                night: String="",
                drName: String="",
                isUserSelected:Boolean = false,
                type:Int = 0)

    companion object {
        const val typeSpeciality = 1
        const val typeOthers = 2
    }
}