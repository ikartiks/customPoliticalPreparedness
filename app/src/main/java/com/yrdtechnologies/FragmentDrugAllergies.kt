package com.yrdtechnologies

import com.yrdtechnologies.persistence.DatabaseHelper

class FragmentDrugAllergies : FragmentAllergiesBase() {
    override val type: String
        get() = DatabaseHelper.typeDrugAllProfile

    companion object {
        private var fragmentDrugAllergies: FragmentDrugAllergies? = null
        fun newInstance(): FragmentDrugAllergies? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentDrugAllergies()
            return fragmentDrugAllergies
        }
    }
}