package com.yrdtechnologies

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yrdtechnologies.dialogs.AddDoctorDialog
import com.yrdtechnologies.dialogs.AddDoctorDialog.OnDoctorChangedListener
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.utility.DatePickerFragment
import com.yrdtechnologies.utility.FileUtils
import kotlinx.android.synthetic.main.activity_my_doctors_new.container
import kotlinx.android.synthetic.main.activity_my_doctors_new.tabLayout

class ActivityDoctorsNew : ActivityBase(), View.OnClickListener, OnDoctorChangedListener {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    val requestCodeForImage = 2
    var titles = arrayOf("My Doctors", "Search")
    lateinit var addDoctorDialog: AddDoctorDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_doctors_new)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
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
            imageX.visibility = View.GONE
            tabLayout.getTabAt(i)!!.customView = v
        }
        if (intent.hasExtra(openDSearchPage)) container.setCurrentItem(1, true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        when (view.id) {
            R.id.Image -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivityForResult(intent, requestCodeForImage)
            }
        }
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
            return when (position) {
                0 -> {
                    FragmentDoctors.newInstance()!!
                }
                1 -> {
                    FragmentDoctorSearch.newInstance()!!
                }
                else -> throw RuntimeException("add something")
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

    override fun onDoctorChanged() {
        val fragment = mSectionsPagerAdapter!!.getItem(0) as FragmentDoctors
        fragment.onDoctorChanged()
    }

    fun selectFile() {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.size > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        startActivityForResult(intent, requestCodeForImagePdf)
    }

    fun uploadFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermission)
        } else {
            val chooserIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(chooserIntent, requestCodeForCamera)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermission) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) uploadFromCamera() else showToast("Please grant camera permission")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == requestCodeForImagePdf && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            val cR = this.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val extension = mime.getExtensionFromMimeType(cR.getType(uri!!))
            val path = FileUtils.generateFileFromStream(this, uri, extension)
            val extensionDoc: String?
            extensionDoc = if (extension.equals("pdf", ignoreCase = true)) DatabaseHelper.typePdfMyDocs else DatabaseHelper.typeImageMyDocs
            addDoctorDialog.setDocumentPathType(path, extensionDoc)
        } else if (requestCode == requestCodeForCamera && resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras!!["data"] as Bitmap?
            val path = FileUtils.generateFileFromBitmap(this, bitmap, "jpg")
            addDoctorDialog.setDocumentPathType(path, DatabaseHelper.typeImageMyDocs)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showPopup(pojoDoctor: PojoDoctor) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        addDoctorDialog = AddDoctorDialog(this, pojoDoctor)
        addDoctorDialog.onDoctorChangedListener=(this)
        addDoctorDialog.activityDoctorsNew = this
        addDoctorDialog.width = width
        addDoctorDialog.show()
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(fragmentManager, "datePicker")
    }

    companion object {
        const val openDSearchPage = "openSearchPage"
        const val requestCodeForCamera = 3
        const val cameraPermission = 4
        const val requestCodeForImagePdf = 1
    }
}