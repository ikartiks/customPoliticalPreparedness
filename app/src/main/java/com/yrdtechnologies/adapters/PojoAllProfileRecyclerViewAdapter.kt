package com.yrdtechnologies.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoAllProfile

class PojoAllProfileRecyclerViewAdapter //private final OnListFragmentInteractionListener mListener;
(var context: Context, private val mValues: List<PojoAllProfile>,
 var onItemClickListener: OnItemClickListener? /*, OnListFragmentInteractionListener listener*/
) : RecyclerView.Adapter<PojoAllProfileRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_myprofile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mIdView.text = holder.mItem!!.name
        val imageView = holder.mView.findViewById<ImageView>(R.id.Selected)
        if (holder.mItem!!.isSelected) {
            imageView.visibility = View.VISIBLE
        } else imageView.visibility = View.GONE

//        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            holder.itemView.setBackgroundDrawable(holder.mItem.isSelected()?context.getResources().getDrawable(R.drawable.bg_selected_all_profile):null);
//        } else {
//            holder.itemView.setBackground(holder.mItem.isSelected()?context.getResources().getDrawable(R.drawable.bg_selected_all_profile):null);
//        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView
        var mItem: PojoAllProfile? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@PojoAllProfileRecyclerViewAdapter)
        }

        init {
            mIdView = mView.findViewById<View>(R.id.id) as TextView
            mView.setOnClickListener(this)
        }
    }

}