package com.yrdtechnologies.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.TableMedicines
import com.yrdtechnologies.pojo.PojoMedicine
import java.util.ArrayList
import java.util.Locale

class PojoMedicineAdapter(context: Context, private val allValues: List<PojoMedicine>,
                          onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val toBeShown: MutableList<PojoMedicine>
    var onItemClickListener: OnItemClickListener?
    var context: Context
    var hideAddIcon = false
    fun hideAddIcon() {
        hideAddIcon = true
    }

    fun filter(type: String?, text: String) {
        toBeShown.clear()
        if (type == null) {
            if (text.isEmpty()) {
                toBeShown.addAll(allValues)
            } else {
                for (single in allValues) {
                    if (single.name.toLowerCase(Locale.getDefault()).contains(text)
                            || single.drName.toLowerCase(Locale.getDefault()).contains(text)) toBeShown.add(single)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_medicine, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holedrx = holder as ViewHolder
        holedrx.mItem = toBeShown[position]
        holedrx.mView.tag = holedrx.mItem
        holedrx.name.text = holedrx.mItem!!.name
        if (holedrx.mItem!!.type == TableMedicines.typeUserMedicine) {
            holedrx.other.visibility = View.VISIBLE
            holedrx.other.text = """${holedrx.mItem!!.startDate} - ${holedrx.mItem!!.endDate}
${holedrx.mItem!!.morning}-${holedrx.mItem!!.evening}-${holedrx.mItem!!.night}"""
        } else {
            holedrx.imageView.visibility = View.VISIBLE
            val params = holedrx.name.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.CENTER_VERTICAL)
            holedrx.name.layoutParams = params
        }
    }

    override fun getItemCount(): Int {
        return toBeShown.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        var name: TextView
        var other: TextView
        var imageView: ImageView
        var mItem: PojoMedicine? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@PojoMedicineAdapter)
        }

        init {
            name = mView.findViewById(R.id.Name)
            other = mView.findViewById(R.id.Other)
            imageView = mView.findViewById(R.id.Add)
            mView.setOnClickListener(this)
        }
    }

    init {
        toBeShown = ArrayList()
        toBeShown.addAll(allValues)
        this.onItemClickListener = onItemClickListener
        this.context = context
    }
}