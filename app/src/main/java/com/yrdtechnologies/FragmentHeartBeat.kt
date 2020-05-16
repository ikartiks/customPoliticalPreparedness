package com.yrdtechnologies

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jwetherell.heart_rate_monitor.HeartRateMonitor
import com.yrdtechnologies.persistence.AppPreferences
import kotlinx.android.synthetic.main.fragment_fragment_heart_beat.heartRate
import kotlinx.android.synthetic.main.fragment_fragment_heart_beat.lastUpdateDate
import kotlinx.android.synthetic.main.fragment_fragment_heart_beat.measure
import kotlinx.android.synthetic.main.fragment_fragment_heart_beat.range
import kotlinx.android.synthetic.main.fragment_fragment_heart_beat.updateMeasurement

class FragmentHeartBeat : FragmentBase(), View.OnClickListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_heart_beat, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        measure.setOnClickListener(this)
        val appPreferences = AppPreferences.getAppPreferences(context)
        if (appPreferences.getString(HeartRateMonitor.heart, "") != "") {
            updateMeasurement.text = "Update Measurement"
            lastUpdateDate.visibility = View.VISIBLE
            lastUpdateDate.text = appPreferences.getString(HeartRateMonitor.lastUpdateDate, "")
            heartRate.text = appPreferences.getString(HeartRateMonitor.heart, "0")
            val heart = appPreferences.getString(HeartRateMonitor.heart, "0").toInt()
            if (heart > 60 && heart < 100) {
                range.text = "Normal range"
            } else if (heart <= 60) {
                range.text = "Low heart rate"
            } else if (heart >= 100) {
                range.text = "High heart rate"
            }
        } else {
            updateMeasurement.text = "Click measure to calculate"
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.measure) (activity as ActivityMyHealthAndWellness?)!!.askPermission()
    }

    companion object {
        private var fragmentDrugAllergies: FragmentHeartBeat? = null
        fun newInstance(): FragmentHeartBeat? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentHeartBeat()
            return fragmentDrugAllergies
        }
    }
}