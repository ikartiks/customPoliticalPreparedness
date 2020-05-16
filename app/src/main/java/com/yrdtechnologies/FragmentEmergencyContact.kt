package com.yrdtechnologies

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.adapters.MyEmergencyContactRecyclerViewAdapter
import com.yrdtechnologies.dialogs.AddEmergencyContactDialog
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the []
 * interface.
 */
class FragmentEmergencyContact  //    static FragmentEmergencyContact emergencyContactFragment;
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : FragmentBase(), View.OnClickListener, OnItemClickListener, OnDialogDismissed {
    private var mColumnCount = 1

    //    private OnListFragmentInteractionListener mListener;
    private var hide = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey(hideAdd)) {
                hide = true
            }
            mColumnCount = arguments!!.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_emergencycontact_list, container, false)
        val fab = view.findViewById<TextView>(R.id.AddContact)
        fab.setOnClickListener(this)

        // Set the adapter
        val context = view.context
        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        if (mColumnCount <= 1) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, mColumnCount)
        }

        val dbUtility = DBUtility(context!!)
        val familyList = ArrayList<PojoFamily>()
        familyList.addAll(dbUtility.emergencyContacts)
        var toPass: Activity
        //        Activity activity =this.getActivity();
//        if(activity instanceof ActivityMyProfileNew)
//            toPass =(ActivityMyProfileNew)activity;
//        else
//            toPass = (ActivityMaps)activity;
        recyclerView.adapter = MyEmergencyContactRecyclerViewAdapter(familyList, this)
        if (hide) {
            val addContact = view.findViewById<TextView>(R.id.AddContact)
            addContact.visibility = View.GONE
        }
        return view
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.AddContact -> {
                val contactPickerIntent = Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                startActivityForResult(contactPickerIntent, REQUEST_PICK_CONTACT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_CONTACT) contactPicked(data!!, requestCode)
    }

    private fun contactPicked(data: Intent, requestCode: Int) {
        if (context == null || view == null || activity == null) {
            log("still null")
            return
        }
        var cursor: Cursor? = null
        try {
            var phoneNo: String? = null
            var name: String? = null
            // getData() method will have the Content Uri of the selected contact
            val uri = data.data
            //Query the content uri
            cursor = context!!.contentResolver.query(uri!!, null, null, null, null)
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
            if (requestCode == REQUEST_PICK_CONTACT) {


                //fragment.onContactFetched(name,phoneNo);
                val pojoFamily = PojoFamily()
                pojoFamily.name = name
                pojoFamily.number = phoneNo
                val addDocumentDialog = AddEmergencyContactDialog(context!!, pojoFamily)
                addDocumentDialog.activityMyProfileNew = activity as ActivityMyProfileNew
                addDocumentDialog.width = (getInt(ActivitySplash.width, 0))
                addDocumentDialog.onDialogDismissed = this
                addDocumentDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cursor?.close()
        }
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        val pojoFamily = view.tag as PojoFamily
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = Uri.parse("tel:" + pojoFamily.number)
        startActivity(intent)
    }

    companion object {
        const val contacts = "contacts"
        const val REQUEST_PICK_CONTACT = 1
        const val ARG_COLUMN_COUNT = "column-count"
        const val hideAdd = "hideAdd"
    }

    override fun onDialogDismissed() {
        if (context == null || view == null) {
            log("this is null")
            return
        }
        val dbUtility = DBUtility(context!!)
        val familyList = ArrayList<PojoFamily>()
        familyList.addAll(dbUtility.emergencyContacts)
        val recyclerView: RecyclerView = view!!.findViewById(R.id.list)
        recyclerView.adapter = MyEmergencyContactRecyclerViewAdapter(familyList, this)
    }
}