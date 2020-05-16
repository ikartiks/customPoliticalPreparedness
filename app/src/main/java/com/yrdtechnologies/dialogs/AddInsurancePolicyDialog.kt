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
import com.yrdtechnologies.ActivityMyProfileNew
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.persistence.InsuranceTable
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.pojo.PojoInsurancePolicy
import com.yrdtechnologies.utility.DatePickerFragment

class AddInsurancePolicyDialog(context: Context, var pojoInsurancePolicy: PojoInsurancePolicy) : Dialog(context), View.OnClickListener, DialogInterface.OnDismissListener {
    var viewOnly = false
    var width = 0


    var activityMyProfileNew: ActivityMyProfileNew? = null

    var onDialogDismissed: OnDialogDismissed? = null


    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(activityMyProfileNew!!.fragmentManager, "datePicker")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_insurance)
        val startDate = findViewById<Button>(R.id.StartDate)
        val endDate = findViewById<Button>(R.id.EndDate)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        val shortDescription = findViewById<EditText>(R.id.ShortDescription)
        val longDescription = findViewById<EditText>(R.id.LongDescription)
        val name = findViewById<EditText>(R.id.Name)
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        if (pojoInsurancePolicy.startDate == null) {
        } else {
            viewOnly = true
            startDate.text = pojoInsurancePolicy.startDate
            endDate.text = pojoInsurancePolicy.endDate
            shortDescription.setText(pojoInsurancePolicy.policyNumber)
            longDescription.setText(pojoInsurancePolicy.info)
            name.setText(pojoInsurancePolicy.name)
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
                if (startDate.text.toString() == context.resources.getString(R.string.startDate) || endDate.text.toString() == context.resources.getString(R.string.endDate)) {
                    showCustomMessage("Enter start date & end date")
                    return
                }
                val shortDescription = findViewById<EditText>(R.id.ShortDescription)
                val longDescription = findViewById<EditText>(R.id.LongDescription)
                val name = findViewById<EditText>(R.id.Name)
                val nameX = name.text.toString()
                if (nameX.trim { it <= ' ' }.isEmpty()) {
                    showCustomMessage("Enter name")
                    return
                }
                val shortDescriptionX = shortDescription.text.toString()
                val longDesc = longDescription.text.toString()
                val contentValues: ContentValues
                if (viewOnly) {
                } else {
                    contentValues = ContentValues()
                    contentValues.put(InsuranceTable.name, nameX)
                    contentValues.put(InsuranceTable.startDate, startDate.text.toString())
                    contentValues.put(InsuranceTable.endDate, endDate.text.toString())
                    contentValues.put(InsuranceTable.number, shortDescriptionX)
                    contentValues.put(InsuranceTable.info, longDesc)
                    context.contentResolver.insert(OurContentProvider.uriInsuranceTable, contentValues)
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

    override fun onDismiss(dialog: DialogInterface) {
        onDialogDismissed?.onDialogDismissed()
    }

}