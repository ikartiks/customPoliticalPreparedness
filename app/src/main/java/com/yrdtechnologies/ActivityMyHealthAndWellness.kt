package com.yrdtechnologies

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jwetherell.heart_rate_monitor.HeartRateMonitor
import kotlinx.android.synthetic.main.activity_my_health.container
import kotlinx.android.synthetic.main.activity_my_health.tabLayout

class ActivityMyHealthAndWellness : AppCompatActivity() {
    lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private val REQUEST_CAMERA = 1
    var titles = arrayOf("Pedometer", "Heartbeat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_health)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(container)
        for (i in titles.indices) {
            val v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val tabOne = v.findViewById<TextView>(R.id.tab)
            tabOne.text = titles[i]
            val imageX = v.findViewById<ImageView>(R.id.Image)
            imageX.visibility = View.GONE
            tabLayout.getTabAt(i)!!.customView = v
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_landing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this, ActivityMaps::class.java)
            startActivity(intent)
            return true
        }
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return if (position == 0) FragmentPedometer.newInstance()!! else if (position == 1) FragmentHeartBeat.newInstance()!! else {
                throw RuntimeException("lol add new data")
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    fun askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        } else {
            val intent = Intent(this, HeartRateMonitor::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(this, HeartRateMonitor::class.java)
                    startActivity(intent)
                }
                return
            }
        }
    }
}