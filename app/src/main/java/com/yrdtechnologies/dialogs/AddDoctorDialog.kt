package com.yrdtechnologies.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.yrdtechnologies.ActivityDoctorsNew
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.persistence.TableDoctors
import com.yrdtechnologies.persistence.TableMyDocuments
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.utility.DatePickerFragment
import java.io.File

class AddDoctorDialog : Dialog, View.OnClickListener, DialogInterface.OnDismissListener {
    var onDoctorChangedListener: OnDoctorChangedListener? = null
    var viewOnly = false

    interface OnDoctorChangedListener {
        fun onDoctorChanged()
    }

    var pojoDoctor: PojoDoctor? = null
    var width = 0

    var activityDoctorsNew: ActivityDoctorsNew? = null

    fun setDocumentPathType(path: String, type: String) {
        pojoDoctor!!.fileType = type
        pojoDoctor!!.filePath = path
    }

    var onDialogDismissed: OnDialogDismissed? = null

    constructor(context: Context) : super(context) {}
    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}
    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}
    constructor(context: Context, pojoDoctor: PojoDoctor?) : super(context) {
        this.pojoDoctor = pojoDoctor
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(activityDoctorsNew!!.fragmentManager, "datePicker")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_doctor)
        val startDate = findViewById<Button>(R.id.VistDateX)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        val browse = findViewById<Button>(R.id.browse)
        val camera = findViewById<ImageView>(R.id.browseCamera)
        val gallery = findViewById<ImageView>(R.id.browseGallery)
        camera.setOnClickListener(this)
        gallery.setOnClickListener(this)
        val name = findViewById<EditText>(R.id.Name)
        val speciality = findViewById<EditText>(R.id.Speciality)
        val shortDescription = findViewById<EditText>(R.id.ShortDescription)
        val longDescription = findViewById<EditText>(R.id.LongDescription)
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        browse.setOnClickListener(this)
        startDate.setOnClickListener(this)
        if (pojoDoctor!!.shortDesc != null) {
            shortDescription.setText(pojoDoctor!!.shortDesc)
        }
        if (pojoDoctor!!.longDesc != null) longDescription.setText(pojoDoctor!!.longDesc)
        if (pojoDoctor!!.visitDate != null) {
            startDate.text = pojoDoctor!!.visitDate
        } else {
            startDate.text = context.resources.getString(R.string.visit_date)
        }
        name.setText(pojoDoctor!!.name)
        speciality.setText(pojoDoctor!!.speciality)
        if (pojoDoctor!!.filePath != null) {
            browse.visibility = View.VISIBLE
            camera.visibility = View.GONE
            gallery.visibility = View.GONE
            viewOnly = true
            name.isEnabled = false
            startDate.isEnabled = false
            speciality.isEnabled = false
            shortDescription.isEnabled = false
            longDescription.isEnabled = false
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
                if (viewOnly) {
                    dismiss()
                    return
                }
                val startDate = findViewById<Button>(R.id.VistDateX)
                if (startDate.text.toString() == context.resources.getString(R.string.visit_date)) {
                    showCustomMessage("Enter visit date")
                    return
                }
                if (pojoDoctor!!.filePath == null) {
                    showCustomMessage("Please select a file")
                    return
                }
                val shortDescription = findViewById<EditText>(R.id.ShortDescription)
                val longDescription = findViewById<EditText>(R.id.LongDescription)


                //it will always be an add here
                var contentValues: ContentValues
                contentValues = ContentValues()
                contentValues.put(TableDoctors.name, pojoDoctor!!.name)
                contentValues.put(TableDoctors.filePath, pojoDoctor!!.filePath)
                contentValues.put(TableDoctors.fileType, pojoDoctor!!.fileType)
                contentValues.put(TableDoctors.userSelected, 1)
                contentValues.put(TableDoctors.speciality, pojoDoctor!!.speciality)
                contentValues.put(TableDoctors.visitDate, startDate.text.toString())
                contentValues.put(TableDoctors.pin, pojoDoctor!!.pin)
                contentValues.put(TableDoctors.city, pojoDoctor!!.city)
                contentValues.put(TableDoctors.rating, pojoDoctor!!.rating)
                contentValues.put(TableDoctors.contact, pojoDoctor!!.contact)
                contentValues.put(TableDoctors.degree, pojoDoctor!!.degree)
                contentValues.put(TableDoctors.shortDesc, shortDescription.text.toString())
                contentValues.put(TableDoctors.longDesc, longDescription.text.toString())
                context.contentResolver.insert(OurContentProvider.uriMyDrTable, contentValues)


                // add document as well.
                contentValues = ContentValues()
                contentValues.put(TableMyDocuments.details, " - ")
                contentValues.put(TableMyDocuments.type, if (pojoDoctor!!.fileType.equals("pdf", ignoreCase = true)) DatabaseHelper.typePdfMyDocs else DatabaseHelper.typeImageMyDocs)
                contentValues.put(TableMyDocuments.name, pojoDoctor!!.name + "'s visit")
                contentValues.put(TableMyDocuments.startDate, startDate.text.toString())
                contentValues.put(TableMyDocuments.endDate, startDate.text.toString())
                contentValues.put(TableMyDocuments.drName, pojoDoctor!!.name)
                contentValues.put(TableMyDocuments.filePath, pojoDoctor!!.filePath)
                contentValues.put(TableMyDocuments.hospital, " NA ")
                contentValues.put(TableMyDocuments.shortDesc, shortDescription.text.toString())
                contentValues.put(TableMyDocuments.longDesc, longDescription.text.toString())
                context.contentResolver.insert(OurContentProvider.uriMyDocsTable, contentValues)
                if (onDoctorChangedListener != null) onDoctorChangedListener!!.onDoctorChanged()
                dismiss()
            }
            R.id.browse -> if (pojoDoctor!!.filePath != null) {
                val sharedFileUri = FileProvider.getUriForFile(this.context, this.context.packageName, File(pojoDoctor!!.filePath))
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = sharedFileUri
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                this.context.startActivity(intent)
            }
            R.id.browseCamera -> activityDoctorsNew!!.uploadFromCamera()
            R.id.browseGallery -> activityDoctorsNew!!.selectFile()
            R.id.Cancel -> dismiss()
            R.id.VistDateX -> showDatePickerDialog(v)
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