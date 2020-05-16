package com.yrdtechnologies.adapters

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.adapters.UserProfileAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.pojo.PojoInsurancePolicy

class UserProfileAdapter(var context: Context, private val toBeShown: List<Any>,
                         var onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
        val `object` = toBeShown[position]
        if (`object` is PojoFamily) {
            return if (`object`.isHeader) typeHeaderFamily else typeFamily
        } else if (`object` is PojoInsurancePolicy) {
            return if (`object`.isHeader) typeHeaderInsurance else typeInsurance
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View? = null
        if (viewType == typeHeaderFamily || viewType == typeHeaderInsurance) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_policy_family_header, parent, false)
            return FamilyHeader(view)
        } else if (viewType == typeInsurance || viewType == typeFamily) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_search_medicine, parent, false)
            return InsuranceHolder(view)
        }

        //should never come here
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_policy_family_header, parent, false)
        return FamilyHeader(view)
    }

    override fun getItemCount(): Int {
        return toBeShown.size
    }

    inner class InsuranceHolder(var mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@UserProfileAdapter)
        }

        init {
            mView.setOnClickListener(this)
        }
    }

    inner class FamilyHeader(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView = mView!!.findViewById(R.id.Name)
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@UserProfileAdapter)
        }

        init {
            mView.setOnClickListener(this)
        }
    }

    companion object {
        const val typeHeaderFamily = 1
        const val typeHeaderInsurance = 2
        const val typeInsurance = 3
        const val typeFamily = 4
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder!!.itemViewType
        val o = toBeShown[position]
        holder.itemView.tag = o
        if (viewType == typeHeaderFamily || viewType == typeHeaderInsurance) {
            val familyHeader = holder as FamilyHeader?
            if (viewType == typeHeaderInsurance) {
                val pojoInsurancePolicy = o as PojoInsurancePolicy
                familyHeader!!.mIdView.text = pojoInsurancePolicy.name
            } else if (viewType == typeHeaderFamily) {
                val pojoFamily = o as PojoFamily
                familyHeader!!.mIdView.text = pojoFamily.name
            }
        } else if (viewType == typeInsurance || viewType == typeFamily) {
            val insuranceHolder = holder as InsuranceHolder?
            insuranceHolder!!.mView!!.findViewById<View>(R.id.Add).visibility = View.GONE
            val name = insuranceHolder.mView!!.findViewById<TextView>(R.id.Name)
            val other = insuranceHolder.mView!!.findViewById<TextView>(R.id.Other)
            if (viewType == typeInsurance) {
                val pojoInsurancePolicy = o as PojoInsurancePolicy
                name.text = pojoInsurancePolicy.name
                other.text = """${pojoInsurancePolicy.startDate} - ${pojoInsurancePolicy.endDate}
${pojoInsurancePolicy.policyNumber}
${pojoInsurancePolicy.info}"""
            } else if (viewType == typeFamily) {
                val pojoFamily = o as PojoFamily
                name.text = pojoFamily.name
                other.text = """${pojoFamily.relation}
${pojoFamily.number}
${pojoFamily.age}"""
            }
        }
    }

}