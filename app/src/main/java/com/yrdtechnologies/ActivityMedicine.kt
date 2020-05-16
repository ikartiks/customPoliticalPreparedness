package com.yrdtechnologies

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.yrdtechnologies.adapters.PojoAllProfileSearchAdapter
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.utility.DBUtility

class ActivityMedicine : ActivityBase(), TextWatcher {
    var allProfileRecyclerViewAdapter: PojoAllProfileSearchAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_landing, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val search = findViewById<EditText>(R.id.Search)
        search.addTextChangedListener(this)
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        val dbUtility = DBUtility(this)
        val medicines = dbUtility.getByType(DatabaseHelper.typeMedicineAllProfile)
        allProfileRecyclerViewAdapter = PojoAllProfileSearchAdapter(this, medicines, null)
        recyclerView.adapter = allProfileRecyclerViewAdapter
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        allProfileRecyclerViewAdapter!!.filter(s.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->                 // do what you want to be done on home button click event
                onBackPressed()
            R.id.action_settings -> {
                val intent = Intent(this, ActivityMaps::class.java)
                startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }
}