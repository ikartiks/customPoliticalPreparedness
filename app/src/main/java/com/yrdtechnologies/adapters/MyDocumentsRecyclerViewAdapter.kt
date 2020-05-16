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
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.PojoMyDocuments
import java.util.ArrayList
import java.util.Locale

class MyDocumentsRecyclerViewAdapter(context: Context, private val allValues: ArrayList<PojoMyDocuments>, onItemClickListener: OnItemClickListener? /*, OnListFragmentInteractionListener listener*/) : RecyclerView.Adapter<MyDocumentsRecyclerViewAdapter.ViewHolder>() {
    private val toBeShown: ArrayList<PojoMyDocuments> = ArrayList()
    var onItemClickListener: OnItemClickListener?
    var context: Context
    fun filter(type: String?, text: String) {
        toBeShown.clear()
        if (type == null) {
            if (text.isEmpty()) {
                toBeShown.addAll(allValues)
            } else {
                for (single in allValues) {
                    if (single.name?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.hospital?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.shortDesc?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.drName?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.details?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.endDate?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.startDate?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.longDesc?.toLowerCase(Locale.getDefault())?.contains(text)==true) toBeShown.add(single)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_mydocuments, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = toBeShown[position]
        holder.mView.tag = holder.mItem
        holder.imageView.setImageResource(if (holder.mItem?.type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_with_circle else R.drawable.jpeg_with_circle)
        holder.name.text = holder.mItem?.name
        holder.dates.text = holder.mItem?.startDate + " - " + holder.mItem?.endDate
    }

    override fun getItemCount(): Int {
        return toBeShown.size
    }

    inner class ViewHolder(var mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        var name: TextView
        var dates: TextView
        var imageView: ImageView
        var mItem: PojoMyDocuments? = null
        override fun onClick(v: View) {
             onItemClickListener?.onItemClick(mView, adapterPosition,
                    this@MyDocumentsRecyclerViewAdapter)
        }

        init {
            name = mView.findViewById(R.id.Title)
            dates = mView.findViewById(R.id.startDateEndDate)
            imageView = mView.findViewById(R.id.Image)
            mView.setOnClickListener(this)
        }
    }

    //private final OnListFragmentInteractionListener mListener;
    init {
        toBeShown.addAll(allValues)
        this.onItemClickListener = onItemClickListener
        this.context = context
    }
}