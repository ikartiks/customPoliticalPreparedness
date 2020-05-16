package com.yrdtechnologies.adapters

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.Allergy
import me.kaede.tagview.Tag
import me.kaede.tagview.TagView
import java.util.ArrayList

class PojoAllergiesRecyclerViewAdapter //private final OnListFragmentInteractionListener mListener;
(var context: Context, private val mValues: ArrayList<Allergy>,
 var onItemClickListener: OnItemClickListener? /*, OnListFragmentInteractionListener listener*/
) : RecyclerView.Adapter<PojoAllergiesRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inflate_allergy, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        val type = holder.mView.findViewById<TextView>(R.id.Type)
        val image = holder.mView.findViewById<ImageView>(R.id.Image)
        val typeX = holder.mItem!!.allergyHeader?.type
        if (typeX.equals(DatabaseHelper.typeDrugAllProfile, ignoreCase = true)) {
            image.setImageDrawable(context.resources.getDrawable(R.drawable.na_layer))
        } else if (typeX.equals(DatabaseHelper.typeFamilyAllProfile, ignoreCase = true)) {
            image.setImageDrawable(context.resources.getDrawable(R.drawable.bp_layer))
        } else if (typeX.equals(DatabaseHelper.typeFoodAllProfile, ignoreCase = true)) {
            image.setImageDrawable(context.resources.getDrawable(R.drawable.ff_layer))
        } else if (typeX.equals(DatabaseHelper.typeGeneticAllProfile, ignoreCase = true)) {
            image.setImageDrawable(context.resources.getDrawable(R.drawable.dna_layer))
        } else if (typeX.equals(DatabaseHelper.typeKnownAllProfile, ignoreCase = true)) {
            image.setImageDrawable(context.resources.getDrawable(R.drawable.bp_layer))
        }
        type.text = typeX?.substring(0, 1)?.toUpperCase() + typeX?.substring(1) + " Allergies"
        val tagView: TagView = holder.mView.findViewById(R.id.TagView)
        val items = holder.mItem!!.allergies
        if (items?.size!! > 0) {
            for (profile in items) {
                var tag = Tag(profile.name)
                tag = beautifyTab(tag)
                //tag.isDeletable = true;
                tagView.addTag(tag)
            }
        } else {
            var tag = Tag("None")
            tag = beautifyTab(tag)
            //tag.isDeletable = true;
            tagView.addTag(tag)
        }
    }

    private fun beautifyTab(tag: Tag): Tag {
        tag.tagTextColor = Color.parseColor("#000000")
        tag.layoutColor = Color.parseColor("#DDDDDD")
        tag.layoutColorPress = Color.parseColor("#DDDDDD")
        //or tag.background = this.getResources().getDrawable(R.drawable.custom_bg);
        tag.radius = 20f
        tag.tagTextSize = 14f
        tag.layoutBorderSize = 1f
        tag.layoutBorderColor = Color.parseColor("#FFFFFF")
        return tag
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        var mItem: Allergy? = null
        override fun onClick(v: View) {
            onItemClickListener?.onItemClick(mView, adapterPosition,
                    this@PojoAllergiesRecyclerViewAdapter)
        }

        init {
            mView.setOnClickListener(this)
        }
    }

}