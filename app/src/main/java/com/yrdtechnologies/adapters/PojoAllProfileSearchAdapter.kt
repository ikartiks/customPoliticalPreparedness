package com.yrdtechnologies.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoAllProfile
import java.util.ArrayList
import java.util.Locale

class PojoAllProfileSearchAdapter(context: Context, private val allValues: List<PojoAllProfile>,
                                  onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<PojoAllProfileSearchAdapter.ViewHolder>() {
    private val mValues: MutableList<PojoAllProfile>
    var onItemClickListener: OnItemClickListener?
    var context: Context
    fun filter(text: String) {
        mValues.clear()
        if (text.isEmpty()) {
            mValues.addAll(allValues)
        } else {
            for (single in allValues) {
                if (single.name?.toLowerCase(Locale.getDefault())?.contains(text)!!) mValues.add(single)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_search_medicine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mIdView.text = holder.mItem!!.name
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView
        var mItem: PojoAllProfile? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@PojoAllProfileSearchAdapter)
        }

        init {
            mIdView = mView.findViewById<View>(R.id.Name) as TextView
            mView.setOnClickListener(this)
        }
    }

    //private final OnListFragmentInteractionListener mListener;
    init {
        mValues = ArrayList()
        mValues.addAll(allValues)
        this.onItemClickListener = onItemClickListener
        this.context = context
    }
}