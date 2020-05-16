package com.yrdtechnologies.adapters

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoDoctor
import java.util.ArrayList
import java.util.Locale

class PojoDoctorAdapter(context: Context, private val allValues: List<PojoDoctor>,
                        onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private val toBeShown: MutableList<PojoDoctor>
    var onItemClickListener: OnItemClickListener?
    var context: Context
    var hideAddIcon = false
    fun hideAddIcon() {
        hideAddIcon = true
    }

    override fun getItemViewType(position: Int): Int {
        val pojoDoctor = toBeShown[position]
        return pojoDoctor.type
    }

    fun filter(type: String?, text: String) {
        toBeShown.clear()
        if (type == null) {
            if (text.isEmpty()) {
                toBeShown.addAll(allValues)
            } else {
                for (single in allValues) {
                    if ( single.name?.toLowerCase(Locale.getDefault())?.contains(text) == true||
                             single.rating?.toLowerCase(Locale.getDefault())?.contains(text)== true ||
                             single.contact?.toLowerCase(Locale.getDefault())?.contains(text) == true||
                             single.state?.toLowerCase(Locale.getDefault())?.contains(text) == true||
                             single.degree?.toLowerCase(Locale.getDefault())?.contains(text)== true ||
                             single.city?.toLowerCase(Locale.getDefault())?.contains(text)== true ||
                             single.address?.toLowerCase(Locale.getDefault())?.contains(text)== true ||
                              single.speciality?.toLowerCase(Locale.getDefault())?.contains(text)==true ||
                            single.shortDesc?.toLowerCase(Locale.getDefault())?.contains(text) == true||
                             single.longDesc?.toLowerCase(Locale.getDefault())?.contains(text)==true) toBeShown.add(single)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
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
        val viewType = holder!!.itemViewType
        if (viewType == PojoDoctor.typeSpeciality) {
            val holedrx = holder as ViewHolder?
            holedrx!!.mItem = toBeShown[position]
            holedrx.mIdView.text = holedrx.mItem!!.speciality
            holedrx.mView.tag = holedrx.mItem!!.speciality
        } else if (viewType == PojoDoctor.typeOthers) {
            val othersViewHolder = holder as OthersViewHolder?
            othersViewHolder!!.mItem = toBeShown[position]
            othersViewHolder.mView.tag = othersViewHolder.mItem
            val add = othersViewHolder.mView.findViewById<ImageView>(R.id.Add)
            if (hideAddIcon) add.setImageResource(R.drawable.attachment) else add.setImageResource(if (othersViewHolder.mItem!!.isSelected) R.drawable.check else R.drawable.plus)
            othersViewHolder.name.text = othersViewHolder.mItem!!.name
            if (hideAddIcon) {
                othersViewHolder.other.text = othersViewHolder.mItem!!.visitDate
            } else {
                othersViewHolder.other.text = """${othersViewHolder.mItem!!.speciality}
                    ${othersViewHolder.mItem!!.contact}
                    ${othersViewHolder.mItem!!.degree}
                    ${othersViewHolder.mItem!!.city}
                    ${othersViewHolder.mItem!!.rating}
                    ${othersViewHolder.mItem!!.pin}
                    """
            }
        }
    }



    override fun getItemCount(): Int {
        return toBeShown.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.findViewById(R.id.Name)
        var mItem: PojoDoctor? = null
    }

    inner class OthersViewHolder(var mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        var name: TextView
        var other: TextView
        var mItem: PojoDoctor? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@PojoDoctorAdapter)
        }

        init {
            mView.setOnClickListener(this)
            name = mView.findViewById(R.id.Name)
            other = mView.findViewById(R.id.Other)
        }
    }

    init {
        toBeShown = ArrayList()
        toBeShown.addAll(allValues)
        this.onItemClickListener = onItemClickListener
        this.context = context
    }
}