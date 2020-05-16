package com.yrdtechnologies.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.maps.Result
import java.text.DecimalFormat

class MapRecyclerViewAdapter //private final OnListFragmentInteractionListener mListener;
(private val mValues: List<Result>,
 var onItemClickListener: OnItemClickListener? /*, OnListFragmentInteractionListener listener*/
) : RecyclerView.Adapter<MapRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_hospitals_nearby, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mView.tag = holder.mItem
        holder.mIdView.text = holder.mItem!!.name
        val df = DecimalFormat()
        df.maximumFractionDigits = 2
        holder.distance.text = df.format(holder.mItem!!.distance) + " km"
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        var mIdView: TextView
        var distance: TextView
        var mItem: Result? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@MapRecyclerViewAdapter)
        }

        init {
            mIdView = mView.findViewById(R.id.Name)
            distance = mView.findViewById(R.id.Distance)
            mView.setOnClickListener(this)
        }
    }

}