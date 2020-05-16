package com.yrdtechnologies

import com.yrdtechnologies.persistence.DatabaseHelper

class FragmentGeneticAllergies : FragmentAllergiesBase() {
    override val type: String
        get() = DatabaseHelper.typeGeneticAllProfile

    companion object {
        private var fragmentDrugAllergies: FragmentGeneticAllergies? = null
        fun newInstance(): FragmentGeneticAllergies? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentGeneticAllergies()
            return fragmentDrugAllergies
        }
    }
}