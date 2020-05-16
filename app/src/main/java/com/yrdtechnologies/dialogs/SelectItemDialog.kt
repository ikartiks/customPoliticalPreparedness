package com.yrdtechnologies.dialogs

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yrdtechnologies.R
import com.yrdtechnologies.adapters.CustomSimpleItemAnimator
import com.yrdtechnologies.adapters.PojoAllProfileRecyclerViewAdapter
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.pojo.PojoAllProfile
import com.yrdtechnologies.utility.DBUtility
import java.util.ArrayList

class SelectItemDialog : Dialog, View.OnClickListener, TextView.OnEditorActionListener, OnItemClickListener, DialogInterface.OnDismissListener {
    private var allergy: ArrayList<PojoAllProfile>? = null
    private var toBeChanged: ArrayList<PojoAllProfile>? = null

    var type: String? = null
    var onDialogDismissed: OnDialogDismissed? = null
    var width = 0


    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window!!.setLayout(500, 500)
        setContentView(R.layout.fragment_myprofile_pop)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        val add = findViewById<ImageView>(R.id.Add)
        add.setOnClickListener(this)
        val dbUtility = DBUtility(this.context)
        allergy = dbUtility.getByType(type!!)
        toBeChanged = ArrayList()
        // Set the adapter
        val recyclerView = findViewById<View>(R.id.list) as RecyclerView
        val addAllergy = findViewById<EditText>(R.id.AddProfile)
        addAllergy.hint = "Enter your missing $type"
        addAllergy.setOnEditorActionListener(this)
        val allProfileRecyclerViewAdapter = PojoAllProfileRecyclerViewAdapter(this.context, allergy!!, this)
        recyclerView.adapter = allProfileRecyclerViewAdapter
        recyclerView.itemAnimator = CustomSimpleItemAnimator(allProfileRecyclerViewAdapter)
        setOnDismissListener(this)
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
            findViewById<View>(R.id.Add).performClick()
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        val window = this.window
        val lp = window!!.attributes
        lp.width = width
        lp.height = width
        window.attributes = lp
    }

    override fun onClick(v: View) {
        if (v.id == R.id.Add) {
            val addAllergy = findViewById<EditText>(R.id.AddProfile)
            val recyclerView: RecyclerView = findViewById(R.id.list)
            val dbUtility = DBUtility(this.context)
            val name = addAllergy.text.toString()
            if (name.trim { it <= ' ' }.isEmpty()) {
                showCustomMessage("Kindly enter something")
                return
            }
            dbUtility.addMyProfileType(type, name, true)
            addAllergy.setText("")
            allergy!!.add(0, PojoAllProfile(name, type!!, true))
            recyclerView.adapter!!.notifyItemInserted(0)
            recyclerView.scrollToPosition(0)
            //hideSoftInput(addAllergy.getWindowToken());
            return
        } else if (v.id == R.id.Done) {
            val dbUtility = DBUtility(this.context)
            for (pojoAllProfile in toBeChanged!!) {
                dbUtility.updateUserSelected(pojoAllProfile.type, pojoAllProfile.name, pojoAllProfile.isSelected)
            }
            dismiss()
        } else if (v.id == R.id.Cancel) {
            dismiss()
        }
    }

    fun showCustomMessage(message: String?): AlertDialog? {
        val adb = AlertDialog.Builder(this.context)
        adb.setTitle(this.context.resources.getString(R.string.app_name))
        adb.setMessage(message)
        adb.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        adb.create().show()
        return null
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        if (position == -1) return
        val pojoAllProfile = allergy!![position]
        pojoAllProfile.isSelected = !pojoAllProfile.isSelected
        val selected = view.findViewById<ImageView>(R.id.Selected)
        selected.visibility = View.VISIBLE
        if (pojoAllProfile.isSelected) {
            val anim = ObjectAnimator.ofFloat(selected, View.ALPHA, 0f, 1f)
            anim.duration = 700
            anim.target = selected
            anim.interpolator = LinearInterpolator()
            anim.start()

            //recyclerView.getAdapter().notifyItemMoved(position,0);
        } else {
            //recyclerView.getAdapter().notifyItemMoved(position,allergy.size()-1);
            val anim = ObjectAnimator.ofFloat(selected, View.ALPHA, 1f, 0f)
            anim.duration = 700
            anim.target = selected
            anim.interpolator = LinearInterpolator()
            anim.start()
        }
        toBeChanged!!.add(pojoAllProfile)
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissed?.onDialogDismissed()
    }
}