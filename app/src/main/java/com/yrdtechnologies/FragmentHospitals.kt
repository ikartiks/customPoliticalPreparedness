package com.yrdtechnologies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.adapters.MapRecyclerViewAdapter
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.pojo.maps.Result

class FragmentHospitals : FragmentBase(), OnItemClickListener, View.OnClickListener {
    private var laitude = 0.0
    private var longitude = 0.0

    var mView:View?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mView =  inflater.inflate(R.layout.fragment_hospitals, container, false)
        return mView
    }

    fun onLocationFound(latitude:Double, longitude:Double, results:List<Result>) {

        this.laitude = latitude
        this.longitude = longitude
        val mapRecyclerViewAdapter = MapRecyclerViewAdapter(results, this)
        val list = mView?.findViewById<RecyclerView>(R.id.list)

        list?.adapter = mapRecyclerViewAdapter
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        val result = view.tag as Result
        val location = result.geometry.location
        val uri = ("http://maps.google.com/maps?f=d&hl=en&saddr=" + laitude + "," + longitude
                + "&daddr=" + location.lat + "," + location.lng)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        if (resolveActivity(intent)) startActivity(intent)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.One || v.id == R.id.Two || v.id == R.id.Three) (activity as ActivityDoctorsNew?)!!.showPopup(v.tag as PojoDoctor)
    }

    companion object {
        var fragmentHospitals: FragmentHospitals? = null
        fun newInstance(): FragmentHospitals? {

            return FragmentHospitals()
        }
    }
}