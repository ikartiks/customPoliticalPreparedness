package com.yrdtechnologies

import com.yrdtechnologies.persistence.DatabaseHelper

class FragmentKnownAllergies : FragmentAllergiesBase() {
    override val type: String
        get() = DatabaseHelper.typeKnownAllProfile

    companion object {
        private var fragmentDrugAllergies: FragmentKnownAllergies? = null
        fun newInstance(): FragmentKnownAllergies? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentKnownAllergies()
            return fragmentDrugAllergies
        }
    }
}