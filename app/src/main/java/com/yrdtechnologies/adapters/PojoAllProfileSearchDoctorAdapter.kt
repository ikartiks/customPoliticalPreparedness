package com.yrdtechnologies.adapters

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

class PojoAllProfileSearchDoctorAdapter(context: Context, private val allValues: List<PojoDoctor>,
                                        specialities: List<PojoDoctor>,
                                        onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val toBeShown: MutableList<PojoDoctor>
    private val specialities: List<PojoDoctor>
    var onItemClickListener: OnItemClickListener?
    var context: Context
    override fun getItemViewType(position: Int): Int {
        val pojoDoctor = toBeShown[position]
        return pojoDoctor.type
    }

    fun filter(type: String?, text: String) {
        toBeShown.clear()
        if (type == null) {
            if (text.isEmpty()) {
                toBeShown.addAll(specialities)
            } else {
                for (single in allValues) {
                    if (single.name?.toLowerCase()?.contains(text)!!) toBeShown.add(single)
                }
            }
        } else {
            val dbUtility = DBUtility(context)
            toBeShown.addAll(dbUtility.getDoctorsByType(type))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        if (viewType == PojoDoctor.typeSpeciality) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_dr_type, parent, false)
            return ViewHolder(view)
        } else if (viewType == PojoDoctor.typeOthers) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_search_medicine, parent, false)
            return OthersViewHolder(view)
        }
        //should never come here
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_dr_type, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        if (viewType == PojoDoctor.typeSpeciality) {
            val holedrx = holder as ViewHolder?
            holedrx!!.mItem = toBeShown[position]
            holedrx.mIdView.text = holedrx.mItem!!.speciality
            holedrx.mView.tag = holedrx.mItem!!.speciality
        } else if (viewType == PojoDoctor.typeOthers) {
            val othersViewHolder = holder as OthersViewHolder?
            othersViewHolder!!.mItem = toBeShown[position]
            othersViewHolder.name.text = othersViewHolder.mItem!!.name
            othersViewHolder.other.text = """${othersViewHolder.mItem!!.speciality}
            ${othersViewHolder.mItem!!.contact}
            ${othersViewHolder.mItem!!.degree}
            ${othersViewHolder.mItem!!.city}
            ${othersViewHolder.mItem!!.rating}
            ${othersViewHolder.mItem!!.pin}
            """
        }
    }


    override fun getItemCount(): Int {
        return toBeShown.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView = mView.findViewById(R.id.Name)
        var mItem: PojoDoctor? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@PojoAllProfileSearchDoctorAdapter)
        }
        init {
            mView.setOnClickListener(this)
        }
    }

    inner class OthersViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {
        var name: TextView = mView.findViewById(R.id.Name)
        var other: TextView = mView.findViewById(R.id.Other)
        var mItem: PojoDoctor? = null
    }

    init {
        toBeShown = ArrayList()
        this.specialities = specialities
        toBeShown.addAll(specialities)
        this.onItemClickListener = onItemClickListener
        this.context = context
    }
}