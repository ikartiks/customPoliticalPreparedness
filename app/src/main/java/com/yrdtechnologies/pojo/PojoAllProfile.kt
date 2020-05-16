package com.yrdtechnologies.pojo

/**
 * Created by kartikshah on 05/02/18.
 */
class PojoAllProfile {
    var name: String =""
    var type: String =""
    var isSelected = false

    constructor()
    constructor(name: String, type: String, selected: Boolean) {
        this.name = name
        this.type = type
        isSelected = selected
    }

}