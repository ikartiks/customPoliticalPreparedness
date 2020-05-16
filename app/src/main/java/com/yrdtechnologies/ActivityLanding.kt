package com.yrdtechnologies

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.yrdtechnologies.adapters.LandingRecyclerViewAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.AppPreferences
import com.yrdtechnologies.pojo.PojoLanding
import com.yrdtechnologies.utility.FileUtils
import com.yrdtechnologies.vm.ActivityLandingViewModel
import kotlinx.android.synthetic.main.activity_landing.drawer
import kotlinx.android.synthetic.main.activity_landing.nav_view
import kotlinx.android.synthetic.main.app_bar_activity_landing.clickable
import kotlinx.android.synthetic.main.app_bar_activity_landing.image
import kotlinx.android.synthetic.main.app_bar_activity_landing.list
import kotlinx.android.synthetic.main.app_bar_activity_landing.name
import kotlinx.android.synthetic.main.app_bar_activity_landing.toolbar
import java.util.ArrayList

class ActivityLanding : ActivityBase(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, OnItemClickListener {

    lateinit var viewModel:ActivityLandingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(ActivityLandingViewModel::class.java)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        clickable.setOnClickListener(this)

        name.text = viewModel.getName()
        viewModel.getImage(image)

        list.isNestedScrollingEnabled = false
        list.adapter = LandingRecyclerViewAdapter(this, viewModel.getListItems() , this)
        image.setOnClickListener(this)
        name.setOnClickListener(this)
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
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        if (id == R.id.Dr) {
            val intent = Intent(this, ActivityDoctorsNew::class.java)
            intent.putExtra(ActivityDoctorsNew.openDSearchPage, true)
            startActivity(intent)
        } else if (id == R.id.Medicine) {
            val intent = Intent(this, ActivityMedicinesNew::class.java)
            intent.putExtra(ActivityDoctorsNew.openDSearchPage, true)
            startActivity(intent)
        } else if (id == R.id.Claims) {
            val intent = Intent(this, ActivityWIP::class.java)
            intent.putExtra(ActivityWIP.title, "My Claims")
            startActivity(intent)
        } else if (id == R.id.Logout) {
            putBoolean(LoginActivity.isUserLoggedIn, false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.image, R.id.name, R.id.clickable -> {
                val intent = Intent(this, ActivityMyProfileNew::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, "profile")
                startActivity(intent, options.toBundle())
            }
            else -> {
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        name.text = viewModel.getName()
        viewModel.getImage(image)
    }


    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        val tag = view.tag as String
        val intent: Intent
        if (tag.equals("My Profile", ignoreCase = true)) {
            intent = Intent(this, ActivityMyProfileNew::class.java)
        } else if (tag.equals("My Documents", ignoreCase = true)) {
            intent = Intent(this, ActivityMyDocumentsList::class.java)
        } else if (tag.equals("Emergency", ignoreCase = true)) {
            intent = Intent(this, ActivityMaps::class.java)
        } else if (tag.equals("My Medicines", ignoreCase = true)) {
            intent = Intent(this, ActivityMedicinesNew::class.java)
        } else if (tag.equals("My Doctors", ignoreCase = true)) {
            intent = Intent(this, ActivityDoctorsNew::class.java)
        } else if (tag.equals("My Health & Wellness", ignoreCase = true)) {
            intent = Intent(this, ActivityMyHealthAndWellness::class.java)
        } else {
            intent = Intent(this, ActivityWIP::class.java)
            intent.putExtra(ActivityWIP.title, tag)
        }
        startActivity(intent)
    }
}