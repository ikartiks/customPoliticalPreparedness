package com.yrdtechnologies.pojo

import java.util.ArrayList

class Allergy {
    var allergies: ArrayList<PojoAllProfile>? = null
    var allergyHeader: PojoAllProfile? = null

    constructor() {}
    constructor(allergies: ArrayList<PojoAllProfile>?, allergyHeader: PojoAllProfile?) {
        this.allergies = allergies
        this.allergyHeader = allergyHeader
    }

}