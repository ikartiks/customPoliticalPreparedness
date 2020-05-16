package com.yrdtechnologies

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.yrdtechnologies.adapters.PojoMedicineAdapter
import com.yrdtechnologies.dialogs.AddMedicineDialog
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoMedicine
import com.yrdtechnologies.utility.DBUtility

class FragmentMedicinesSearch : FragmentBase(), TextWatcher, OnItemClickListener {
    var pojoDoctorAdapter: PojoMedicineAdapter? = null
    lateinit var addMedicineDialog: AddMedicineDialog
    private lateinit var onMedicinesChangedListener: OnMedicinesChangedListener

    interface OnMedicinesChangedListener {
        fun onMedicineChanged()
    }

    fun setOnMedicinesChangedListener(onMedicinesChangedListener: OnMedicinesChangedListener) {
        this.onMedicinesChangedListener = onMedicinesChangedListener
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context: Context? = activity
        val v = inflater.inflate(R.layout.activity_doctor, container, false)
        val recentParent = v.findViewById<LinearLayout>(R.id.RecentParent)
        recentParent.visibility = View.GONE
        val search = v.findViewById<EditText>(R.id.Search)
        search.hint = "Search Medicine"
        search.addTextChangedListener(this)
        val recyclerView: RecyclerView = v.findViewById(R.id.list)
        val listHeader = v.findViewById<TextView>(R.id.ListHeader)
        listHeader.text = "Medicine list"
        val dbUtility = DBUtility(context!!)
        //ArrayList<PojoDoctor> medicines= dbUtility.getAllDoctors();
        pojoDoctorAdapter = PojoMedicineAdapter(context, dbUtility.listedMedicines, this)
        recyclerView.adapter = pojoDoctorAdapter
        return v
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        pojoDoctorAdapter!!.filter(null, s.toString())
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        //allProfileRecyclerViewAdapter.filter((String) view.getTag(),null);
        //isShowingSpeciality=true;
        val pojoMedicine = view.tag as PojoMedicine
        val displayMetrics = DisplayMetrics()
        (activity as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        addMedicineDialog = AddMedicineDialog(this.context!!, pojoMedicine)
        addMedicineDialog.activityMedicinesNew = this.activity as ActivityMedicinesNew
        addMedicineDialog.width = width
        addMedicineDialog.show()
    }

    companion object {
        var fragmentDoctors: FragmentMedicinesSearch? = null
        fun newInstance(): FragmentMedicinesSearch? {
            if (fragmentDoctors != null) return fragmentDoctors
            fragmentDoctors = FragmentMedicinesSearch()
            return fragmentDoctors
        }
    }
}