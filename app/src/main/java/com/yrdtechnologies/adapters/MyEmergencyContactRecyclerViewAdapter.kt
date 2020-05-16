package com.yrdtechnologies.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoFamily
import java.util.ArrayList

//import com.yrdtechnologies.FragmentEmergencyContact.OnListFragmentInteractionListener;
class MyEmergencyContactRecyclerViewAdapter(private val mValues: ArrayList<PojoFamily>, var onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<MyEmergencyContactRecyclerViewAdapter.ViewHolder>() {
    fun addItem(item: PojoFamily) {
        mValues.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_emergencycontact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mView.tag = holder.mItem
        holder.mIdView.text = mValues[position].name
        holder.mContentView.text = """
            ${mValues[position].number}
            ${mValues[position].relation}
            """.trimIndent()
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView
        val mContentView: TextView
        var mItem: PojoFamily? = null
        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(v, adapterPosition, this@MyEmergencyContactRecyclerViewAdapter)
        }

        init {
            mView.setOnClickListener(this)
            mIdView = mView.findViewById<View>(R.id.id) as TextView
            mContentView = mView.findViewById<View>(R.id.content) as TextView
        }
    }

}