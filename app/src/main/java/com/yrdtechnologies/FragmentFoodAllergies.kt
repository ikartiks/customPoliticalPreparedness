package com.yrdtechnologies

import com.yrdtechnologies.persistence.DatabaseHelper

class FragmentFoodAllergies : FragmentAllergiesBase() {
    override val type: String
        get() = DatabaseHelper.typeFoodAllProfile

    companion object {
        private var fragmentDrugAllergies: FragmentFoodAllergies? = null
        fun newInstance(): FragmentFoodAllergies? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentFoodAllergies()
            return fragmentDrugAllergies
        }
    }
}