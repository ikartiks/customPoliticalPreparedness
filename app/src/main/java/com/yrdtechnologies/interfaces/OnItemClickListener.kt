package com.yrdtechnologies.interfaces

import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * Created by kartikshah on 05/02/18.
 */
interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?)
}