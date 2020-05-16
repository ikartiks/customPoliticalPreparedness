package com.yrdtechnologies

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.PojoDoctorAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

class FragmentDoctorSearch : FragmentBase(), TextWatcher, OnItemClickListener {

    private var pojoDoctorAdapter: PojoDoctorAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context: Context? = activity
        val v = inflater.inflate(R.layout.activity_doctor, container, false)
        val recentParent = v.findViewById<LinearLayout>(R.id.RecentParent)
        recentParent.visibility = View.GONE
        val search = v.findViewById<EditText>(R.id.Search)
        search.addTextChangedListener(this)
        val recyclerView: RecyclerView = v.findViewById(R.id.list)
        val listHeader = v.findViewById<TextView>(R.id.ListHeader)
        listHeader.text = "Find doctor"
        val dbUtility = DBUtility(context!!)
        val compltelist = ArrayList<PojoDoctor>()
        val speciality = dbUtility.uniqueSpeciality
        for (pojoDoctor in speciality) {
            compltelist.add(pojoDoctor)
            compltelist.addAll(dbUtility.getDoctorsByType(pojoDoctor.speciality!!))
        }
        pojoDoctorAdapter = PojoDoctorAdapter(context, compltelist, this)
        recyclerView.adapter = pojoDoctorAdapter
        return v
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        pojoDoctorAdapter!!.filter(null, s.toString())
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        (activity as ActivityDoctorsNew?)!!.showPopup(view.tag as PojoDoctor)
    }

    companion object {
        var fragmentDoctors: FragmentDoctorSearch? = null
        fun newInstance(): FragmentDoctorSearch? {
            if (fragmentDoctors != null) return fragmentDoctors
            fragmentDoctors = FragmentDoctorSearch()
            return fragmentDoctors
        }
    }
}

//TODO medicine, other screens, view model in fragments, profile