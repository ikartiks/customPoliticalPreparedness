package com.yrdtechnologies.vm

import android.app.Application
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import com.yrdtechnologies.FragmentUserProfile
import com.yrdtechnologies.R
import com.yrdtechnologies.persistence.AppPreferences
import com.yrdtechnologies.pojo.PojoLanding
import com.yrdtechnologies.utility.FileUtils
import kotlinx.android.synthetic.main.app_bar_activity_landing.image
import java.util.ArrayList

class ActivityLandingViewModel(app:Application):AndroidViewModel(app) {

    val appPreferences = AppPreferences.getAppPreferences(app.applicationContext)

    fun getListItems():MutableList<PojoLanding>{
        val navigationList: MutableList<PojoLanding> = ArrayList()
        var pojoLanding = PojoLanding("009889", "My Documents", R.drawable.document)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("c64097", "My Doctors", R.drawable.doctor)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("64a8c1", "My Medicines", R.drawable.medicine)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("f26222", "My Health & Wellness", R.drawable.health)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("873393", "Knowledge center", R.drawable.knowledge_center)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("bbb332", "My special offers", R.drawable.special_discount)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("c64097", "My insurance details", R.drawable.is_details)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("64a8c1", "My claims", R.drawable.my_claim)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("f26222", "As Experts", R.drawable.expert)
        navigationList.add(pojoLanding)
        pojoLanding = PojoLanding("873393", "Contact Us", R.drawable.contact_us)
        navigationList.add(pojoLanding)
        return navigationList
    }

    fun getName() = appPreferences.getString(FragmentUserProfile.uName, "name..")
    fun getImage(image:ImageView){
        val path = appPreferences.getString(FragmentUserProfile.uImage, "")
        if (!path.isEmpty()) {
            val options = BitmapFactory.Options()
            options.inSampleSize = 2
            var bitmap = BitmapFactory.decodeFile(path, options)
            bitmap = FileUtils.getCircularBitmap(bitmap)
            image.setImageBitmap(bitmap)
            //image.setImageURI(Uri.fromFile(new File(path)));
        } else {
            image.setImageResource(R.drawable.user)
        }
    }
}