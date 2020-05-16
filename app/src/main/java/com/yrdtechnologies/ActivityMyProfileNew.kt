package com.yrdtechnologies

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.ContactsContract
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.yrdtechnologies.FragmentUserProfile.ProfileDetailsUpdated
import com.yrdtechnologies.dialogs.AddEmergencyContactDialog
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.persistence.AppPreferences
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.utility.DatePickerFragment
import com.yrdtechnologies.utility.FileUtils

@Suppress("DEPRECATION")
class ActivityMyProfileNew : ActivityBase(), View.OnClickListener, ProfileDetailsUpdated, OnDialogDismissed {
    //    private SectionsPagerAdapter mSectionsPagerAdapter;
    private val requestCodeForImage = 2

    //    private ViewPager mViewPager;
    var titles = arrayOf("Profile", "Allergies", "Emergency Contacts")
//    var drawables = intArrayOf(R.drawable.user_slector, R.drawable.na_selector, R.drawable.emergency_selector)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_new)
        val appPreferences = AppPreferences.getAppPreferences(this)
        val collapsingToolBarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //toolbar.setPadding(0, statusBarHeight, 0, 0);

        //collapsingToolBarLayout.setTitle(appPreferences.getString(FragmentUserProfile.uName,""));
        val name = appPreferences.getString(FragmentUserProfile.uName, "name..")
        collapsingToolBarLayout.title = "           $name"
        val image = findViewById<ImageView>(R.id.Image)
        val path = appPreferences.getString(FragmentUserProfile.uImage, "")
        if (!path.isEmpty()) {

//            Bitmap rounded = FileUtils.getCircularBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.a));
//            image.setImageBitmap(rounded);
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            var bitmap = BitmapFactory.decodeFile(path, options)
            bitmap = FileUtils.getCircularBitmap(bitmap)
            image.setImageBitmap(bitmap)
            //image.setImageURI(Uri.fromFile(new File(path)));
        } else {
            image.setImageResource(R.drawable.user)
        }
        image.setOnClickListener(this)
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        val mViewPager = findViewById<ViewPager>(R.id.container)
        mViewPager.adapter = mSectionsPagerAdapter
        val tabLayout = findViewById<TabLayout>(R.id.TabLayout)
        tabLayout.setupWithViewPager(mViewPager)
        for (i in titles.indices) {
            val v = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)
            val tabOne = v.findViewById<TextView>(R.id.tab)
            tabOne.text = titles[i]
            val imageX = v.findViewById<ImageView>(R.id.Image)
            //imageX.setImageDrawable(getResources().getDrawable(drawables[i]));
            imageX.visibility = View.GONE
            tabLayout.getTabAt(i)!!.customView = v
        }
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

    //    @Override
    //    public void onListFragmentInteraction(PojoFamily item) {
    //        Intent intent = new Intent();
    //        intent.setAction(Intent.ACTION_DIAL);
    //        intent.setData(Uri.parse("tel:"+item.getNumber()));
    //        startActivity(intent);
    //    }
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

    override fun onProfileDetailsUpdated(name: String) {
        val collapsingToolBarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
        collapsingToolBarLayout.title = "           $name"
    }

    override fun onDialogDismissed() {
        val viewPager = findViewById<ViewPager>(R.id.container)
        val sectionsPagerAdapter = viewPager.adapter as SectionsPagerAdapter?
        val fragment = sectionsPagerAdapter!!.getItem(2) as FragmentEmergencyContact
        fragment.onDialogDismissed()
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
                val fragmentUserProfile: FragmentUserProfile = FragmentUserProfile.newInstance()!!
                fragmentUserProfile.setProfileDetailsUpdatedListener(this@ActivityMyProfileNew)
                fragmentUserProfile
            } else if (position == 1){ FragmentAllergies.newInstance()!!
            }
            else if (position == 2) {
                val fragmentEmergencyContact = FragmentEmergencyContact()
                val args = Bundle()
                args.putInt(FragmentEmergencyContact.ARG_COLUMN_COUNT, 1)
                fragmentEmergencyContact.arguments = args
                fragmentEmergencyContact as Fragment
            } else if (position == 3) {
                FragmentFoodAllergies.newInstance()!!
            } else if (position == 4) {
                FragmentKnownAllergies.newInstance()!!
            } else if (position == 5) {
                FragmentGeneticAllergies.newInstance()!!
            } else {
                throw RuntimeException("lol add new data")
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            when (requestCode) {
                FragmentUserProfile.requestPickContact -> contactPicked(data!!, requestCode)
                requestCodeForImage -> if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data
                    val selecImage = findViewById<ImageView>(R.id.Image)
                    val path = FileUtils.generateFileFromStream(this, uri, "jpg")
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 2
                    var bitmap = BitmapFactory.decodeFile(path, options)
                    bitmap = FileUtils.getCircularBitmap(bitmap)
                    selecImage.setImageBitmap(bitmap)
                    val appPreferences = AppPreferences.getAppPreferences(this)
                    appPreferences.putString(FragmentUserProfile.uImage, path)
                }
            }
        }
    }

    private fun contactPicked(data: Intent, requestCode: Int) {
        var cursor: Cursor? = null
        try {
            val phoneNo: String?
            val name: String?
            // getData() method will have the Content Uri of the selected contact
            val uri = data.data
            //Query the content uri
            cursor = contentResolver.query(uri!!, null, null, null, null)
            cursor!!.moveToFirst()
            // column index of the phone number
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            // column index of the contact name
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            phoneNo = cursor.getString(phoneIndex)
            name = cursor.getString(nameIndex)
            cursor.close()
            //mSectionsPagerAdapter.getItem(mSectionsPagerAdapter.)
            //int index = mViewPager.getCurrentItem();
            if (requestCode == FragmentUserProfile.requestPickContact) {
                val viewPager = findViewById<ViewPager>(R.id.container)
                val sectionsPagerAdapter = viewPager.adapter as SectionsPagerAdapter?
                val fragment = sectionsPagerAdapter!!.getItem(0) as FragmentUserProfile
                fragment.onContactFetched(name, phoneNo)
            } else if (requestCode == FragmentEmergencyContact.REQUEST_PICK_CONTACT) {


                //fragment.onContactFetched(name,phoneNo);
                val pojoFamily = PojoFamily()
                pojoFamily.name = name
                pojoFamily.number = phoneNo
                val addDocumentDialog = AddEmergencyContactDialog(this, pojoFamily)
                addDocumentDialog.activityMyProfileNew = this
                addDocumentDialog.width = (getInt(ActivitySplash.width, 0))
                //                addDocumentDialog.setOnDialogDismissed(fragment);
                addDocumentDialog.onDialogDismissed =(this)
                addDocumentDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cursor?.close()
        }
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(fragmentManager, "datePicker")
    }
}