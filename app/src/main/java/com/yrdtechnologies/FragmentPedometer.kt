package com.yrdtechnologies

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.pedometer.StepDetector
import com.yrdtechnologies.pedometer.StepListener
import com.yrdtechnologies.persistence.AppPreferences
import kotlinx.android.synthetic.main.activity_pedometer.calories
import kotlinx.android.synthetic.main.activity_pedometer.currentActivityParent
import kotlinx.android.synthetic.main.activity_pedometer.distance
import kotlinx.android.synthetic.main.activity_pedometer.progressBar
import kotlinx.android.synthetic.main.activity_pedometer.reset
import kotlinx.android.synthetic.main.activity_pedometer.reset2
import kotlinx.android.synthetic.main.activity_pedometer.start
import kotlinx.android.synthetic.main.activity_pedometer.stepsNow
import kotlinx.android.synthetic.main.activity_pedometer.stepsTotal
import kotlinx.android.synthetic.main.activity_pedometer.stop
import java.text.DecimalFormat

class FragmentPedometer : FragmentBase(), SensorEventListener, StepListener, View.OnClickListener {
    private lateinit var appPreferences: AppPreferences
    lateinit var simpleStepDetector: StepDetector
    lateinit var sensorManager: SensorManager
    private var accel: Sensor? = null

    private var numSteps = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_pedometer, container, false)
        appPreferences = AppPreferences.getAppPreferences(context)
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accel == null) {
            showCustomMessage(getString(R.string.sensorUnavailable))
            val textView = TextView(activity)
            textView.text = getString(R.string.sensorUnavailable)
            return textView
        }
        simpleStepDetector = StepDetector()
        simpleStepDetector.registerListener(this)
        return view
    }

    override fun onResume() {
        super.onResume()

        if (appPreferences.getLong(currnetWeekTime, 0) == 0L) {
            appPreferences.putLong(currnetWeekTime, System.currentTimeMillis())
            appPreferences.putInt(currnetWeekSteps, 0)
        } else if (System.currentTimeMillis() - appPreferences.getLong(currnetWeekTime, 0) > 7 * 24 * 60 * 60 * 1000) {
            appPreferences.putLong(currnetWeekTime, System.currentTimeMillis())
            appPreferences.putInt(currnetWeekSteps, 0)
        }
        stepsTotal.text = "" + appPreferences.getInt(currnetWeekSteps, 0)
        stop.setOnClickListener(this)
        start.setOnClickListener(this)
        reset.setOnClickListener(this)
        reset2.setOnClickListener(this)
        start.performClick()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2])
        }
    }

    override fun step(timeNs: Long) {
        numSteps++
        stepsNow!!.text = "" + numSteps
        stepsTotal!!.text = "" + (numSteps + appPreferences!!.getInt(currnetWeekSteps, 0))
        val df = DecimalFormat()
        df.maximumFractionDigits = 4
        distance.text = "" + df.format(numSteps / 8.toLong())
        calories.text = "" + numSteps / 12
        if (numSteps >= 100) progressBar!!.progress = 100 else progressBar!!.progress = numSteps
        currentActivityParent!!.visibility = View.VISIBLE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.start -> {
                numSteps = 0
                sensorManager.registerListener(this@FragmentPedometer, accel, SensorManager.SENSOR_DELAY_FASTEST)
            }
            R.id.stop -> {
                sensorManager.unregisterListener(this@FragmentPedometer)
                appPreferences.putInt(currnetWeekSteps, numSteps + appPreferences!!.getInt(currnetWeekSteps, 0))
            }
            R.id.reset -> {
                numSteps = 0
                stepsNow.text = "" + numSteps
                distance.text = ""
                stepsNow.text = "" + numSteps
                stepsTotal.text = "0"
                distance.text = "0"
                calories.text = "0"
                progressBar.progress = 0
                currentActivityParent.visibility = View.GONE
                appPreferences.putLong(currnetWeekTime, System.currentTimeMillis())
                appPreferences.putInt(currnetWeekSteps, 0)
            }
            R.id.reset2 -> reset.performClick()
            else -> {
            }
        }
    }

    override fun onDestroy() {
        if (accel != null && numSteps != 0) {
            sensorManager.unregisterListener(this@FragmentPedometer)
            appPreferences.putInt(currnetWeekSteps, numSteps + appPreferences.getInt(currnetWeekSteps, 0))
        }
        super.onDestroy()
    }

    companion object {
        private const val currnetWeekSteps = "currentWeekSteps"
        private const val currnetWeekTime = "currentWeekTime"
        private var fragmentDrugAllergies: FragmentPedometer? = null
        fun newInstance(): FragmentPedometer? {
            if (fragmentDrugAllergies != null) return fragmentDrugAllergies
            fragmentDrugAllergies = FragmentPedometer()
            return fragmentDrugAllergies
        }
    }
}