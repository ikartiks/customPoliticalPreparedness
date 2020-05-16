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
import android.widget.TextView
import com.yrdtechnologies.ActivityMyProfileNew
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.persistence.FamilyTable
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.utility.DatePickerFragment

class AddEmergencyContactDialog(context: Context, var pojoFamily: PojoFamily) : Dialog(context), View.OnClickListener, DialogInterface.OnDismissListener {
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
        setContentView(R.layout.activity_add_family)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        val cif = findViewById<EditText>(R.id.ShortDescription)
        val relation = findViewById<EditText>(R.id.LongDescription)
        val name = findViewById<EditText>(R.id.Name)
        val age = findViewById<EditText>(R.id.DrName)
        val number = findViewById<EditText>(R.id.Diagnosis)
        val header = findViewById<TextView>(R.id.Header)
        header.text = "Add emergency contact"
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        if (pojoFamily.relation == null) {
            number.setText(pojoFamily.number)
            name.setText(pojoFamily.name)
        } else {
            viewOnly = true
            number.setText(pojoFamily.number)
            cif.setText(pojoFamily.cif)
            relation.setText(pojoFamily.relation)
            name.setText(pojoFamily.name)
            age.setText(pojoFamily.age)
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
                val cif = findViewById<EditText>(R.id.ShortDescription)
                val relation = findViewById<EditText>(R.id.LongDescription)
                val name = findViewById<EditText>(R.id.Name)
                val age = findViewById<EditText>(R.id.DrName)
                val number = findViewById<EditText>(R.id.Diagnosis)
                val cifX = cif.text.toString()
                if (cifX.trim { it <= ' ' }.isEmpty()) {
                    showCustomMessage("Enter CIF")
                    return
                }
                val relationX = relation.text.toString()
                if (relationX.trim { it <= ' ' }.isEmpty()) {
                    showCustomMessage("Enter relation")
                    return
                }
                val contentValues: ContentValues
                if (viewOnly) {
                } else {
                    contentValues = ContentValues()
                    contentValues.put(FamilyTable.number, number.text.toString())
                    contentValues.put(FamilyTable.name, name.text.toString())
                    contentValues.put(FamilyTable.age, age.text.toString())
                    contentValues.put(FamilyTable.cif, cifX)
                    contentValues.put(FamilyTable.relation, relationX)
                    contentValues.put(FamilyTable.isEmergencyContact, "1")
                    context.contentResolver.insert(OurContentProvider.uriFamilyTable, contentValues)
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