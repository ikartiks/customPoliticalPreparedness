package com.yrdtechnologies.pojo

/**
 * Created by kartikshah on 04/03/18.
 */
class PojoDoctor {
    var id: String?=null
    var speciality: String?=null
    var name: String?=null
    var degree: String?=null
    var address: String?=""
    var contact: String?=null
    var pin: String?=null
    var city: String?=null
    var state: String?=null
    var rating: String?=null
    var visitDate: String?=null
    var filePath: String?=null
    var fileType: String?=null
    var shortDesc: String?=null
    var longDesc: String?=null
    var isSelected = false
    var type = 0

    companion object {
        const val typeSpeciality = 1
        const val typeOthers = 2
    }
}