package com.yrdtechnologies

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.PojoAllergiesRecyclerViewAdapter
import com.yrdtechnologies.dialogs.SelectItemDialog
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.Allergy
import com.yrdtechnologies.pojo.PojoAllProfile
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

class FragmentAllergies : FragmentBase(), OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener, OnDialogDismissed {
    lateinit var keyValueList: ArrayList<Allergy>
    private var keys = arrayOf(DatabaseHelper.typeDrugAllProfile,
            DatabaseHelper.typeFamilyAllProfile, DatabaseHelper.typeFoodAllProfile,
            DatabaseHelper.typeGeneticAllProfile, DatabaseHelper.typeKnownAllProfile)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_allergies, container, false)
        keyValueList = ArrayList()
        val dbUtility = DBUtility(activity!!)
        for (key in keys) {
            val values = dbUtility.getByTypeSelected(key)
            val pojoAllProfile = PojoAllProfile()
            pojoAllProfile.name = key
            pojoAllProfile.type = key
            val allergy = Allergy(values, pojoAllProfile)
            keyValueList.add(allergy)
        }

        // Set the adapter
        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        activity?.let {
            val allProfileRecyclerViewAdapter = PojoAllergiesRecyclerViewAdapter(it, keyValueList, this)
            recyclerView.adapter = allProfileRecyclerViewAdapter
        }

        return view
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        if (position == -1) return
        val allergy = keyValueList[position]
        //        pojoAllProfile.setSelected(!pojoAllProfile.isSelected());
        val displayMetrics = DisplayMetrics()
        (activity as ActivityMyProfileNew?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val type = allergy.allergyHeader?.type
        context?.let {
            val selectItemDialog = SelectItemDialog(it)
            selectItemDialog.type = type
            selectItemDialog.width = width
            selectItemDialog.onDialogDismissed = this
            selectItemDialog.show()
        }

    }

    override fun onClick(v: View) {}

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    //    public interface OnListFragmentInteractionListener {
    //        // TODO: Update argument type and name
    //        void onListFragmentInteraction(DummyItem item);
    //    }
    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
            val rootView = this@FragmentAllergies.view
            val add = rootView!!.findViewById<ImageView>(R.id.Add)
            add.performClick()
            return true
        }
        return false
    }

    override fun onDialogDismissed() {
        keyValueList = ArrayList()
        val dbUtility = DBUtility(activity!!)
        for (key in keys) {
            val values = dbUtility.getByTypeSelected(key)
            val pojoAllProfile = PojoAllProfile()
            pojoAllProfile.name = key
            pojoAllProfile.type = key
            val allergy = Allergy(values, pojoAllProfile)
            keyValueList.add(allergy)
        }

        // Set the adapter
        val recyclerView = this@FragmentAllergies.view!!.findViewById<View>(R.id.list) as RecyclerView
        val allProfileRecyclerViewAdapter = PojoAllergiesRecyclerViewAdapter(activity!!, keyValueList, this)
        recyclerView.adapter = allProfileRecyclerViewAdapter
    }

    companion object {
        private var fragmentAllergies: FragmentAllergies? = null
        fun newInstance(): FragmentAllergies? {
            if (fragmentAllergies != null) return fragmentAllergies
            fragmentAllergies = FragmentAllergies()
            return fragmentAllergies
        }
    }
}