package com.yrdtechnologies.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.yrdtechnologies.ActivityMedicinesNew
import com.yrdtechnologies.FragmentMedicinesSearch
import com.yrdtechnologies.R
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.persistence.TableMedicines
import com.yrdtechnologies.pojo.PojoMedicine
import com.yrdtechnologies.utility.DatePickerFragment

class AddMedicineDialog : Dialog, View.OnClickListener, DialogInterface.OnDismissListener {
    var pojoMedicine: PojoMedicine? = null
    var activityMedicinesNew: ActivityMedicinesNew? = null
    var actAsUpdate = false
    var width = 0
    var fragmentMedicinesSearch: FragmentMedicinesSearch? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}
    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}
    constructor(context: Context, pojoMedicine: PojoMedicine?) : super(context) {
        this.pojoMedicine = pojoMedicine
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(activityMedicinesNew!!.fragmentManager, "datePicker")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_medicine)
        val startDate = findViewById<Button>(R.id.StartDate)
        val endDate = findViewById<Button>(R.id.EndDate)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        val morning = findViewById<EditText>(R.id.Morning)
        val evening = findViewById<EditText>(R.id.Evening)
        val name = findViewById<EditText>(R.id.Name)
        val drName = findViewById<EditText>(R.id.DrName)
        val night = findViewById<EditText>(R.id.Night)
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        if (pojoMedicine!!.type == TableMedicines.typeList) {
            name.setText(pojoMedicine!!.name)
        } else {
            actAsUpdate = true
            startDate.text = pojoMedicine!!.startDate
            endDate.text = pojoMedicine!!.endDate
            night.setText(pojoMedicine!!.night)
            morning.setText(pojoMedicine!!.morning)
            evening.setText(pojoMedicine!!.evening)
            name.setText(pojoMedicine!!.name)
            drName.setText(pojoMedicine!!.drName)
        }
        setOnDismissListener(this)
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
        when (v.id) {
            R.id.Done -> {
                val startDate = findViewById<Button>(R.id.StartDate)
                val endDate = findViewById<Button>(R.id.EndDate)
                val morning = findViewById<EditText>(R.id.Morning)
                val evening = findViewById<EditText>(R.id.Evening)
                val name = findViewById<EditText>(R.id.Name)
                val drName = findViewById<EditText>(R.id.DrName)
                val night = findViewById<EditText>(R.id.Night)
                if (startDate.text.toString() == context.resources.getString(R.string.startDate) || endDate.text.toString() == context.resources.getString(R.string.endDate)) {
                    showCustomMessage("Enter start date & end date")
                    return
                }
                val nameX = name.text.toString()
                if (nameX.trim { it <= ' ' }.isEmpty()) {
                    showCustomMessage("Enter name")
                    return
                }
                val drNameX = drName.text.toString()
                val morningX = morning.text.toString()
                val eveningX = evening.text.toString()
                val nightX = night.text.toString()
                if ("".equals(morningX, ignoreCase = true) || "".equals(eveningX, ignoreCase = true) || "".equals(nightX, ignoreCase = true)) {
                    showCustomMessage("Enter dosages")
                    return
                }
                val contentValues: ContentValues
                if (actAsUpdate) {
                } else {
                    contentValues = ContentValues()
                    contentValues.put(TableMedicines.name, nameX)
                    contentValues.put(TableMedicines.drName, drNameX)
                    contentValues.put(TableMedicines.startDate, startDate.text.toString())
                    contentValues.put(TableMedicines.endDate, endDate.text.toString())
                    contentValues.put(TableMedicines.type, TableMedicines.typeUserMedicine)
                    contentValues.put(TableMedicines.morning, morningX)
                    contentValues.put(TableMedicines.evening, eveningX)
                    contentValues.put(TableMedicines.night, nightX)
                    context.contentResolver.insert(OurContentProvider.uriMyMedicinesTable, contentValues)
                    activityMedicinesNew!!.onMedicineChanged()
                }
                dismiss()
            }
            R.id.Cancel -> dismiss()
            else -> {
            }
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

    override fun onDismiss(dialog: DialogInterface) {}
}