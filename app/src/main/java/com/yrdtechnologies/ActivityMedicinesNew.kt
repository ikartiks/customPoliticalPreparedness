package com.yrdtechnologies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yrdtechnologies.FragmentMedicinesSearch.OnMedicinesChangedListener
import com.yrdtechnologies.utility.DatePickerFragment
import kotlinx.android.synthetic.main.activity_my_medicine_new.container
import kotlinx.android.synthetic.main.activity_my_medicine_new.tabLayout
import kotlinx.android.synthetic.main.activity_my_medicine_new.toolbar

class ActivityMedicinesNew : AppCompatActivity(), View.OnClickListener, OnMedicinesChangedListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    var titles = arrayOf("My Medicines", "Search")
    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(fragmentManager, "datePicker")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_medicine_new)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        tabLayout.setupWithViewPager(container)
        for (i in titles.indices) {
            val v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val tabOne = v.findViewById<TextView>(R.id.tab)
            tabOne.text = titles[i]
            val imageX = v.findViewById<ImageView>(R.id.Image)
            //imageX.setImageDrawable(getResources().getDrawable(drawables[i]));
            imageX.visibility = View.GONE
            tabLayout.getTabAt(i)?.customView = v
        }
        if (intent.hasExtra(ActivityDoctorsNew.openDSearchPage)) container.setCurrentItem(1, true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_landing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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

    override fun onClick(view: View) {

    }

    override fun onMedicineChanged() {
        val fragment = mSectionsPagerAdapter!!.getItem(0) as FragmentMedicines
        fragment.onMedicinesAdded()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            return if (position == 0) {
                FragmentMedicines.newInstance()!!
            } else if (position == 1) {
                val fragmentDoctorSearch: FragmentMedicinesSearch = FragmentMedicinesSearch.newInstance()!!
                fragmentDoctorSearch.setOnMedicinesChangedListener(this@ActivityMedicinesNew)
                fragmentDoctorSearch
            } else throw RuntimeException("add something")
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}