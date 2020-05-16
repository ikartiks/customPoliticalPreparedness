package com.yrdtechnologies

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.CustomSimpleItemAnimator
import com.yrdtechnologies.adapters.PojoAllProfileRecyclerViewAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoAllProfile
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

abstract class FragmentAllergiesBase : FragmentBase(), OnItemClickListener, View.OnClickListener, TextView.OnEditorActionListener {
    lateinit var allergy: ArrayList<PojoAllProfile>
    abstract val type: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_myprofile, container, false)
        val add = view.findViewById<ImageView>(R.id.Add)
        add.setOnClickListener(this)
        val dbUtility = DBUtility(activity!!)
        allergy = dbUtility.getByType(type)
        // Set the adapter
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val addAllergy = view.findViewById<EditText>(R.id.AddProfile)
        addAllergy.hint = "Enter your missing $type"
        addAllergy.setOnEditorActionListener(this)
        val allProfileRecyclerViewAdapter = PojoAllProfileRecyclerViewAdapter(activity!!, allergy, this)
        recyclerView.adapter = allProfileRecyclerViewAdapter
        recyclerView.itemAnimator = CustomSimpleItemAnimator(allProfileRecyclerViewAdapter)
        hideSoftInput(addAllergy.windowToken)
        return view
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        if (position == -1) return
        val dbUtility = DBUtility(activity!!)
        val pojoAllProfile = allergy[position]
        pojoAllProfile.isSelected = !pojoAllProfile.isSelected
        val selected = view.findViewById<ImageView>(R.id.Selected)
        selected.visibility = View.VISIBLE
        if (pojoAllProfile.isSelected) {
            val anim = ObjectAnimator.ofFloat(selected, View.ALPHA, 0f, 1f)
            anim.duration = 700
            anim.target = selected
            anim.interpolator = LinearInterpolator()
            anim.start()
        } else {
            val anim = ObjectAnimator.ofFloat(selected, View.ALPHA, 1f, 0f)
            anim.duration = 700
            anim.target = selected
            anim.interpolator = LinearInterpolator()
            anim.start()
        }
        dbUtility.updateUserSelected(pojoAllProfile.type, pojoAllProfile.name, pojoAllProfile.isSelected)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.Add) {
            val rootView = this@FragmentAllergiesBase.view
            val addAllergy = rootView!!.findViewById<EditText>(R.id.AddProfile)
            val recyclerView: RecyclerView = rootView.findViewById(R.id.list)
            val dbUtility = DBUtility(activity!!)
            val name = addAllergy.text.toString()
            if (name.trim { it <= ' ' }.isEmpty()) {
                showCustomMessage("Kindly enter something")
                return
            }
            dbUtility.addMyProfileType(type, name, true)
            addAllergy.setText("")
            allergy.add(0, PojoAllProfile(name, type, true))
            recyclerView.adapter?.notifyItemInserted(0)
            recyclerView.scrollToPosition(0)
            hideSoftInput(addAllergy.windowToken)
            return
        }
    }

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
            val rootView = this@FragmentAllergiesBase.view
            val add = rootView!!.findViewById<ImageView>(R.id.Add)
            add.performClick()
            return true
        }
        return false
    }
}