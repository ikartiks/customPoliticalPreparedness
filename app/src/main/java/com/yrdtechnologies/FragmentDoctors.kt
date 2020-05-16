package com.yrdtechnologies

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yrdtechnologies.adapters.PojoDoctorAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList
import java.util.Collections

class FragmentDoctors : FragmentBase(), TextWatcher, OnItemClickListener, View.OnClickListener {

    var pojoDoctorAdapter: PojoDoctorAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.activity_doctor, container, false)
        val search = v.findViewById<EditText>(R.id.Search)
        search.addTextChangedListener(this)
        val recyclerView: RecyclerView = v.findViewById(R.id.list)
        val listHeader = v.findViewById<TextView>(R.id.ListHeader)
        listHeader.text = "My doctor visits"
        val dbUtility = DBUtility(context!!)
        //ArrayList<PojoDoctor> medicines= dbUtility.getAllDoctors();
        val selectedDocs = dbUtility.selectedDoctors
        selectedDocs.reverse()
        pojoDoctorAdapter = PojoDoctorAdapter(context!!, selectedDocs, this)
        pojoDoctorAdapter!!.hideAddIcon()
        recyclerView.adapter = pojoDoctorAdapter
        return v
    }

    override fun onStart() {
        super.onStart()
        val dbUtility = DBUtility(context!!)
        val selectedDocs = dbUtility.selectedDoctors
        Collections.reverse(selectedDocs)
        initializeListAndShowRecents(selectedDocs)
    }

    private fun initializeListAndShowRecents(pojoDoctorArrayList: ArrayList<PojoDoctor>) {
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
            dateOne.text = pojoDoctorArrayList[0]!!.visitDate
            dateTwo.text = pojoDoctorArrayList[1]!!.visitDate
            dateThree.text = pojoDoctorArrayList[2]!!.visitDate
            imageOne.setImageResource(if (pojoDoctorArrayList[0]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageTwo.setImageResource(if (pojoDoctorArrayList[1]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageThree.setImageResource(if (pojoDoctorArrayList[2]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            one.tag = pojoDoctorArrayList[0]
            two.tag = pojoDoctorArrayList[1]
            three.tag = pojoDoctorArrayList[2]
            one.visibility = View.VISIBLE
            two.visibility = View.VISIBLE
            three.visibility = View.VISIBLE
        } else if (pojoDoctorArrayList.size == 2) {
            textViewOne.text = pojoDoctorArrayList[0]!!.name
            textViewTwo.text = pojoDoctorArrayList[1]!!.name
            dateOne.text = pojoDoctorArrayList[0]!!.visitDate
            dateTwo.text = pojoDoctorArrayList[1]!!.visitDate
            imageOne.setImageResource(if (pojoDoctorArrayList[0]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageTwo.setImageResource(if (pojoDoctorArrayList[1]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            one.tag = pojoDoctorArrayList[0]
            two.tag = pojoDoctorArrayList[1]
            one.visibility = View.VISIBLE
            two.visibility = View.VISIBLE
            three.visibility = View.INVISIBLE
        } else if (pojoDoctorArrayList.size == 1) {
            textViewOne.text = pojoDoctorArrayList[0]!!.name
            dateOne.text = pojoDoctorArrayList[0]!!.visitDate
            imageOne.setImageResource(if (pojoDoctorArrayList[0]!!.fileType == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
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

    fun onDoctorChanged() {
        val recyclerView: RecyclerView = view!!.findViewById(R.id.list)
        val context: Context? = activity
        val dbUtility = DBUtility(context!!)
        val selectedDocs = dbUtility.selectedDoctors
        selectedDocs.reverse()
        pojoDoctorAdapter = PojoDoctorAdapter(context!!, selectedDocs, this)
        pojoDoctorAdapter!!.hideAddIcon()
        recyclerView.adapter = pojoDoctorAdapter
        initializeListAndShowRecents(selectedDocs)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.One || v.id == R.id.Two || v.id == R.id.Three) (activity as ActivityDoctorsNew?)!!.showPopup(v.tag as PojoDoctor)
    }

    companion object {
        var fragmentDoctors: FragmentDoctors? = null
        fun newInstance(): FragmentDoctors? {
            if (fragmentDoctors != null) return fragmentDoctors
            fragmentDoctors = FragmentDoctors()
            return fragmentDoctors
        }
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        //allProfileRecyclerViewAdapter.filter((String) view.getTag(),null);
        //isShowingSpeciality=true;
        (activity as ActivityDoctorsNew?)!!.showPopup(view.tag as PojoDoctor)
    }
}