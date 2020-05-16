package com.yrdtechnologies

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.PojoMedicineAdapter
import com.yrdtechnologies.dialogs.AddMedicineDialog
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoMedicine
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

class FragmentMedicines : FragmentBase(), TextWatcher, OnItemClickListener, View.OnClickListener {
    private var pojoDoctorAdapter: PojoMedicineAdapter? = null
    lateinit var addMedicineDialog: AddMedicineDialog

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context: Context? = activity
        val v = inflater.inflate(R.layout.activity_doctor, container, false)
        val search = v.findViewById<EditText>(R.id.Search)
        search.hint = "Search medicine"
        search.addTextChangedListener(this)
        val recyclerView: RecyclerView = v.findViewById(R.id.list)
        val empty = v.findViewById<TextView>(R.id.EmptyMessage)
        val recent = v.findViewById<TextView>(R.id.Recent)
        empty.text = "Seems your drive is empty, add a medicine from search medicines section to get started"
        recent.text = "Recent medicines"
        val listHeader = v.findViewById<TextView>(R.id.ListHeader)
        listHeader.text = "My medicines"
        val dbUtility = DBUtility(context!!)
        val medicines = dbUtility.usersMedicines
        medicines.reverse()
        pojoDoctorAdapter = PojoMedicineAdapter(context, medicines, this)
        recyclerView.adapter = pojoDoctorAdapter
        return v
    }

    override fun onStart() {
        super.onStart()
        val dbUtility = DBUtility(context!!)
        val selectedDocs = dbUtility.usersMedicines
        selectedDocs.reverse()
        initializeListAndShowRecents(selectedDocs)
    }

    @SuppressLint("SetTextI18n")
    private fun initializeListAndShowRecents(pojoDoctorArrayList: ArrayList<PojoMedicine>) {
        val view = view
        val emptyParent = view!!.findViewById<LinearLayout>(R.id.EmptyParent)
        if (pojoDoctorArrayList.size == 0) {
            emptyParent.visibility = View.VISIBLE
        } else {
            emptyParent.visibility = View.GONE
        }
        val one = view.findViewById<LinearLayout>(R.id.One)
        val two = view.findViewById<LinearLayout>(R.id.Two)
        val three = view.findViewById<LinearLayout>(R.id.Three)
        val textViewOne = view.findViewById<TextView>(R.id.NameOne)
        val textViewTwo = view.findViewById<TextView>(R.id.NameTwo)
        val textViewThree = view.findViewById<TextView>(R.id.NameThree)
        val dateOne = view.findViewById<TextView>(R.id.DateOne)
        val dateTwo = view.findViewById<TextView>(R.id.DateTwo)
        val dateThree = view.findViewById<TextView>(R.id.DateThree)
        val imageOne = view.findViewById<ImageView>(R.id.ImageOne)
        val imageTwo = view.findViewById<ImageView>(R.id.ImageTwo)
        val imageThree = view.findViewById<ImageView>(R.id.ImageThree)
        imageOne.visibility = View.GONE
        imageTwo.visibility = View.GONE
        imageThree.visibility = View.GONE
        one.setOnClickListener(this)
        two.setOnClickListener(this)
        three.setOnClickListener(this)
        if (pojoDoctorArrayList.size == 0) {
            one.visibility = View.INVISIBLE
            three.visibility = View.INVISIBLE
            two.visibility = View.INVISIBLE
        } else if (pojoDoctorArrayList.size >= 3) {
            textViewOne.text = pojoDoctorArrayList[0]!!.name
            textViewTwo.text = pojoDoctorArrayList[1]!!.name
            textViewThree.text = pojoDoctorArrayList[2]!!.name
            dateOne.text = "${pojoDoctorArrayList[0]!!.startDate} - ${pojoDoctorArrayList[0]!!.endDate}"
            dateTwo.text = "${pojoDoctorArrayList[1]!!.startDate} - ${pojoDoctorArrayList[1]!!.endDate}"
            dateThree.text = "${pojoDoctorArrayList[2]!!.startDate} - ${pojoDoctorArrayList[2]!!.endDate}"
            one.tag = pojoDoctorArrayList[0]
            two.tag = pojoDoctorArrayList[1]
            three.tag = pojoDoctorArrayList[2]
            one.visibility = View.VISIBLE
            two.visibility = View.VISIBLE
            three.visibility = View.VISIBLE
        } else if (pojoDoctorArrayList.size == 2) {
            textViewOne.text = pojoDoctorArrayList[0]!!.name
            textViewTwo.text = pojoDoctorArrayList[1]!!.name
            dateOne.text = "${pojoDoctorArrayList[0]!!.startDate} - ${pojoDoctorArrayList[0]!!.endDate}"
            dateTwo.text = "${pojoDoctorArrayList[1]!!.startDate} - ${pojoDoctorArrayList[1]!!.endDate}"
            one.tag = pojoDoctorArrayList[0]
            two.tag = pojoDoctorArrayList[1]
            one.visibility = View.VISIBLE
            two.visibility = View.VISIBLE
            three.visibility = View.INVISIBLE
        } else if (pojoDoctorArrayList.size == 1) {
            textViewOne.text = pojoDoctorArrayList[0]!!.name
            dateOne.text = "${pojoDoctorArrayList[0]!!.startDate} - ${pojoDoctorArrayList[0]!!.endDate}"
            one.tag = pojoDoctorArrayList[0]
            one.visibility = View.VISIBLE
            three.visibility = View.INVISIBLE
            two.visibility = View.INVISIBLE
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        pojoDoctorAdapter!!.filter(null, s.toString())
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        //allProfileRecyclerViewAdapter.filter((String) view.getTag(),null);
        //isShowingSpeciality=true;
        val pojoDoctor = view.tag as PojoMedicine
        showPopup(pojoDoctor)
    }

    private fun showPopup(pojoMyDocuments: PojoMedicine) {
        val displayMetrics = DisplayMetrics()
        (activity as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        addMedicineDialog = AddMedicineDialog(this.context!!, pojoMyDocuments)
        addMedicineDialog.activityMedicinesNew = (this.activity as ActivityMedicinesNew?)
        addMedicineDialog.width = width
        addMedicineDialog.show()
    }

    fun onMedicinesAdded() {
        val recyclerView: RecyclerView = view!!.findViewById(R.id.list)
        val context: Context? = activity
        val dbUtility = DBUtility(context!!)
        val medicines = dbUtility.usersMedicines
        medicines.reverse()
        initializeListAndShowRecents(medicines)
        pojoDoctorAdapter = PojoMedicineAdapter(context!!, medicines, this)
        recyclerView.adapter = pojoDoctorAdapter
    }

    override fun onClick(v: View) {
        if (v.id == R.id.One || v.id == R.id.Two || v.id == R.id.Three) showPopup(v.tag as PojoMedicine)
    }

    companion object {
        var fragmentDoctors: FragmentMedicines? = null
        fun newInstance(): FragmentMedicines? {
            if (fragmentDoctors != null) return fragmentDoctors
            fragmentDoctors = FragmentMedicines()
            return fragmentDoctors
        }
    }
}