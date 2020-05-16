package com.yrdtechnologies.pojo

class PojoLanding {
    var color: String? = null
    var name: String? = null
    var drawableX = 0

    constructor() {}
    constructor(color: String?, name: String?, drawableX: Int) {
        this.color = color
        this.name = name
        this.drawableX = drawableX
    }

}