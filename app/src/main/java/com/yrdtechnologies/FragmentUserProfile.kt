package com.yrdtechnologies

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.UserProfileAdapter
import com.yrdtechnologies.dialogs.AddFamilyMemberDialog
import com.yrdtechnologies.dialogs.AddInsurancePolicyDialog
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.AppPreferences
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.pojo.PojoInsurancePolicy
import com.yrdtechnologies.utility.DBUtility
import kotlinx.android.synthetic.main.activity_user_profile.addr1
import kotlinx.android.synthetic.main.activity_user_profile.addr2
import kotlinx.android.synthetic.main.activity_user_profile.addr3
import kotlinx.android.synthetic.main.activity_user_profile.age
import kotlinx.android.synthetic.main.activity_user_profile.cif
import kotlinx.android.synthetic.main.activity_user_profile.city
import kotlinx.android.synthetic.main.activity_user_profile.done
import kotlinx.android.synthetic.main.activity_user_profile.female
import kotlinx.android.synthetic.main.activity_user_profile.input_layout_name
import kotlinx.android.synthetic.main.activity_user_profile.input_layout_number
import kotlinx.android.synthetic.main.activity_user_profile.male
import kotlinx.android.synthetic.main.activity_user_profile.name
import kotlinx.android.synthetic.main.activity_user_profile.number
import kotlinx.android.synthetic.main.activity_user_profile.radioGroup
import kotlinx.android.synthetic.main.activity_user_profile.state
import java.util.ArrayList

class FragmentUserProfile : FragmentBase(), View.OnClickListener, OnItemClickListener, OnDialogDismissed {
    var navigationList: MutableList<Any> = ArrayList()
    private var profileDetailsUpdated: ProfileDetailsUpdated? = null
    override fun onDialogDismissed() {
        reloadList(typeInsurance)
        reloadList(typeFamily)
    }

    interface ProfileDetailsUpdated {
        fun onProfileDetailsUpdated(name: String)
    }

    fun setProfileDetailsUpdatedListener(profileDetailsUpdated: ProfileDetailsUpdated?) {
        this.profileDetailsUpdated = profileDetailsUpdated
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_user_profile, container, false)
    }

    override fun onStart() {
        super.onStart()

        done.setOnClickListener(this)
        val appPreferences = AppPreferences.getAppPreferences(activity)

        name.setText(appPreferences.getString(uName, ""))
        number.setText(appPreferences.getString(uNumber, ""))
        //        policy.setText(appPreferences.getString(uPolicy,""));
        age.setText(appPreferences.getString(uAge, ""))
        addr1.setText(appPreferences.getString(uAddr1, ""))
        addr2.setText(appPreferences.getString(uAddr2, ""))
        addr3.setText(appPreferences.getString(uAddr3, ""))
        city.setText(appPreferences.getString(uCity, ""))
        state.setText(appPreferences.getString(uState, ""))
        cif.setText(appPreferences.getString(uCIF, ""))
        if (appPreferences.getString(uGender, "M") == "M") {
            male.isChecked = true
        } else female.isChecked = true

        reloadList(0)
    }

    fun reloadList(type: Int) {
        navigationList.clear()
        val pojoFamily = PojoFamily()
        pojoFamily.name = "My family members"
        pojoFamily.isHeader = true
        navigationList.add(pojoFamily)
        val dbUtility = DBUtility(context!!)
        val pojoFamilyArrayList = dbUtility.familyMembers
        navigationList.addAll(pojoFamilyArrayList)
        val pojoInsurancePolicy = PojoInsurancePolicy()
        pojoInsurancePolicy.name = "My insurance policies"
        pojoInsurancePolicy.isHeader = true
        navigationList.add(pojoInsurancePolicy)
        val pojoInsurancePolicyArrayList = dbUtility.insurancePolicies
        navigationList.addAll(pojoInsurancePolicyArrayList)
        val recyclerView: RecyclerView = view!!.findViewById(R.id.list)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = UserProfileAdapter(context!!, navigationList, this)
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        val o = view.tag
        if (o is PojoInsurancePolicy) {
            val pojoInsurancePolicy = o
            if (pojoInsurancePolicy.isHeader) showPopupForInsurancePolicy(PojoInsurancePolicy()) else showPopupForInsurancePolicy(pojoInsurancePolicy)
        } else if (o is PojoFamily) {
            val pojoFamily = o
            if (pojoFamily.isHeader) showPopupForFamily(PojoFamily()) else showPopupForFamily(pojoFamily)
        }
    }

    fun showPopupForInsurancePolicy(pojoInsurancePolicy: PojoInsurancePolicy?) {
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val addDocumentDialog = AddInsurancePolicyDialog(this.activity!!, pojoInsurancePolicy!!)
        addDocumentDialog.activityMyProfileNew = (this.activity as ActivityMyProfileNew?)
        addDocumentDialog.width = width
        addDocumentDialog.onDialogDismissed = this
        addDocumentDialog.show()
    }

    fun showPopupForFamily(pojoFamily: PojoFamily) {
        if (pojoFamily.relation == null) {
            val contactPickerIntent = Intent(Intent.ACTION_PICK,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            activity!!.startActivityForResult(contactPickerIntent, requestPickContact)
        } else {
            val displayMetrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val addDocumentDialog = AddFamilyMemberDialog(this.activity!!, pojoFamily)
            addDocumentDialog.activityMyProfileNew = this.activity as ActivityMyProfileNew
            addDocumentDialog.width = width
            addDocumentDialog.onDialogDismissed = this
            addDocumentDialog.show()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.done -> {

                val appPreferences = AppPreferences.getAppPreferences(activity)
                var isvalid = true
                if (name.text.toString().isEmpty()) {
                    input_layout_name.error = "enter a name"
                    isvalid = false
                }
                if (number.text.toString().isEmpty()) {
                    input_layout_number.error = "enter your mobile number"
                    isvalid = false
                }
                
                if (isvalid) {
                    val rb =  view?.findViewById(radioGroup.checkedRadioButtonId) as RadioButton
                    appPreferences.putString(uGender, rb.text.toString())
                    appPreferences.putString(uName, name.text.toString())
                    appPreferences.putString(uNumber, number.text.toString())
                    appPreferences.putString(uAge, age.text.toString())
                    appPreferences.putString(uAddr3, addr3.text.toString())
                    appPreferences.putString(uAddr2, addr2.text.toString())
                    appPreferences.putString(uAddr1, addr1.text.toString())
                    appPreferences.putString(uCIF, cif.text.toString())
                    appPreferences.putString(uCity, city.text.toString())
                    appPreferences.putString(uState, state.text.toString())
                    showToast("Details saved successfully")
                    profileDetailsUpdated?.onProfileDetailsUpdated(name.text.toString())
                } else {
                    showToast("Please enter details first")
                }
            }
            else -> {
            }
        }
    }

    fun onContactFetched(name: String?, number: String?) {
        val pojoFamily = PojoFamily()
        pojoFamily.name = name
        pojoFamily.number = number
        val displayMetrics = DisplayMetrics()
        activity?.let {
            it.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels

            val dialog = AddFamilyMemberDialog(it, pojoFamily)
            dialog.activityMyProfileNew = it as ActivityMyProfileNew
            dialog.width = width
            dialog.onDialogDismissed = this
            dialog.show()
        }

    }

    companion object {
        
        var uName = "uName"
        var uPolicy = "uPolicy"
        var uNumber = "uNumber"
        var uAge = "uAge"
        var uGender = "uGender"
        var uImage = "uImage"
        var uAddr1 = "uAddr1"
        var uAddr2 = "uAddr2"
        var uAddr3 = "uAddr3"
        var uCity = "uCIty"
        var uState = "uState"
        var uCIF = "uCIF"
        const val requestPickContact = 3
        protected const val typeInsurance = 1
        protected const val typeFamily = 2
        var fragmentUserProfile: FragmentUserProfile? = null
        fun newInstance(): FragmentUserProfile? {
            if (fragmentUserProfile != null) return fragmentUserProfile
            fragmentUserProfile = FragmentUserProfile()
            return fragmentUserProfile
        }
    }
}