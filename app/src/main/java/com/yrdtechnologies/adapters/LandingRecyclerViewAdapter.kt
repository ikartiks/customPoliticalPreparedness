package com.yrdtechnologies.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yrdtechnologies.FragmentUserProfile
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.AppPreferences
import com.yrdtechnologies.pojo.PojoLanding
import java.io.File

class LandingRecyclerViewAdapter(var context: Context, private val mValues: List<PojoLanding>, var onItemClickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val normal = 1
    private val profile = 2
    override fun getItemViewType(position: Int): Int {

//        if(mValues.get(position) instanceof String)
        return normal
        //        else
//            return profile;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == normal) {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_landing, parent, false)
            MyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.inflate_landing_profile, parent, false)
            //return null;
            ProfileViewHolder(view)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            profile -> {
                //holder.itemView.findViewById(R.id.Image);
                val name = holder.itemView.findViewById<TextView>(R.id.Name)
                val contact = holder.itemView.findViewById<TextView>(R.id.Contact)
                val image = holder.itemView.findViewById<ImageView>(R.id.Image)
                val appPreferences = AppPreferences.getAppPreferences(name.context)
                name.text = """${appPreferences.getString(FragmentUserProfile.uName, "Name")}, ${appPreferences.getString(FragmentUserProfile.uGender, "G")}
Age ${appPreferences.getString(FragmentUserProfile.uAge, "")}"""
                contact.text = appPreferences.getString(FragmentUserProfile.uNumber, "")
                val path = appPreferences.getString(FragmentUserProfile.uImage, "")
                if (!path.isEmpty()) {
                    image.setImageURI(Uri.fromFile(File(path)))
                }
            }
            normal -> {
                val pojoLanding = mValues[position]
                val name = holder.itemView.findViewById<TextView>(R.id.Name)
                val image = holder.itemView.findViewById<ImageView>(R.id.Image)
                val container = holder.itemView.findViewById<LinearLayout>(R.id.Container)
                container.setBackgroundColor(Color.parseColor("#" + pojoLanding.color))
                image.setImageResource(pojoLanding.drawableX)
                name.text = pojoLanding.name
                holder.itemView.tag = pojoLanding.name
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class MyViewHolder(var mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@LandingRecyclerViewAdapter)
        }

        init {
            mView.setOnClickListener(this)
        }
    }

    inner class ProfileViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIdView: TextView
        var mItem: Any? = null
        override fun onClick(v: View) {
            if (onItemClickListener != null) onItemClickListener!!.onItemClick(mView, adapterPosition,
                    this@LandingRecyclerViewAdapter)
        }

        init {
            mIdView = mView.findViewById<View>(R.id.id) as TextView
            mView.setOnClickListener(this)
        }
    }

}