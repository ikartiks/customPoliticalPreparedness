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
import android.widget.Spinner
import androidx.core.content.FileProvider
import com.yrdtechnologies.ActivityMyDocumentsList
import com.yrdtechnologies.R
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.persistence.TableMyDocuments
import com.yrdtechnologies.pojo.PojoMyDocuments
import com.yrdtechnologies.utility.DatePickerFragment
import java.io.File

class AddDocumentDialog : Dialog, View.OnClickListener, DialogInterface.OnDismissListener {
    lateinit var pojoMyDocuments: PojoMyDocuments
    var actAsUpdate = false
    var width = 0

    var activityMyDocumentsList: ActivityMyDocumentsList? = null

    fun setDocumentPathType(path: String, type: String) {
        pojoMyDocuments.type = type
        pojoMyDocuments.filePath = path
    }

    var onDialogDismissed: OnDialogDismissed? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}
    constructor(context: Context, pojoMyDocuments: PojoMyDocuments) : super(context) {
        this.pojoMyDocuments = pojoMyDocuments
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(activityMyDocumentsList!!.fragmentManager, "datePicker")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dcoument)
        val startDate = findViewById<Button>(R.id.StartDate)
        val endDate = findViewById<Button>(R.id.EndDate)
        val done = findViewById<Button>(R.id.Done)
        val cancel = findViewById<Button>(R.id.Cancel)
        val browse = findViewById<Button>(R.id.browse)
        val camera = findViewById<ImageView>(R.id.browseCamera)
        val gallery = findViewById<ImageView>(R.id.browseGallery)
        camera.setOnClickListener(this)
        gallery.setOnClickListener(this)
        val shortDescription = findViewById<EditText>(R.id.ShortDescription)
        val longDescription = findViewById<EditText>(R.id.LongDescription)
        val name = findViewById<EditText>(R.id.Name)
        val drName = findViewById<EditText>(R.id.DrName)
        val diagnosis = findViewById<EditText>(R.id.Diagnosis)
        done.setOnClickListener(this)
        cancel.setOnClickListener(this)
        browse.setOnClickListener(this)
        if (!this::pojoMyDocuments.isInitialized) {
            pojoMyDocuments = PojoMyDocuments()
        } else {
            actAsUpdate = true
            browse.visibility = View.VISIBLE
            camera.visibility = View.GONE
            gallery.visibility = View.GONE
            val hospital = findViewById<Spinner>(R.id.Hospital)
            val hospitalX = pojoMyDocuments.hospital
            val hospitals = this.context.resources.getStringArray(R.array.array_hospitals)
            var i: Int
            i = 0
            while (i < hospitals.size) {
                if (hospitals[i] == hospitalX) {
                    hospital.setSelection(i)
                    break
                }
                i++
            }
            startDate.text = pojoMyDocuments.startDate
            endDate.text = pojoMyDocuments.endDate
            diagnosis.setText(pojoMyDocuments.details)
            shortDescription.setText(pojoMyDocuments.shortDesc)
            longDescription.setText(pojoMyDocuments.longDesc)
            name.setText(pojoMyDocuments.name)
            drName.setText(pojoMyDocuments.drName)
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
                val drName = findViewById<EditText>(R.id.DrName)
                val diagnosis = findViewById<EditText>(R.id.Diagnosis)
                val hospital = findViewById<Spinner>(R.id.Hospital)
                val hospitalX = hospital.selectedItem.toString()
                //                if(hospitalX.equalsIgnoreCase("Select hospital")){
//                    showCustomMessage("select hospital");
//                    return;
//                }
                val nameX = name.text.toString()
                if (nameX.trim { it <= ' ' }.isEmpty()) {
                    showCustomMessage("Enter name")
                    return
                }
                val drNameX = drName.text.toString()
                val shortDescriptionX = shortDescription.text.toString()
                val longDesc = longDescription.text.toString()
                val diagnosisX = diagnosis.text.toString()
                if (pojoMyDocuments.filePath == null) {
                    showCustomMessage("Please select a file")
                    return
                }
                val contentValues: ContentValues
                if (actAsUpdate) {
                    val browse = findViewById<Button>(R.id.browse)
                    browse.text = "View file"
                    contentValues = ContentValues()
                    contentValues.put(TableMyDocuments.details, diagnosisX)
                    contentValues.put(TableMyDocuments.type, pojoMyDocuments.type)
                    contentValues.put(TableMyDocuments.name, nameX)
                    contentValues.put(TableMyDocuments.startDate, startDate.text.toString())
                    contentValues.put(TableMyDocuments.endDate, endDate.text.toString())
                    contentValues.put(TableMyDocuments.drName, drNameX)
                    contentValues.put(TableMyDocuments.filePath, pojoMyDocuments.filePath)
                    contentValues.put(TableMyDocuments.hospital, hospitalX)
                    contentValues.put(TableMyDocuments.shortDesc, shortDescriptionX)
                    contentValues.put(TableMyDocuments.longDesc, longDesc)
                    context.contentResolver.update(OurContentProvider.uriMyDocsTable, contentValues,
                            TableMyDocuments.filePath + "=?", arrayOf(pojoMyDocuments.filePath))
                } else {
                    contentValues = ContentValues()
                    contentValues.put(TableMyDocuments.details, diagnosisX)
                    contentValues.put(TableMyDocuments.type, pojoMyDocuments.type)
                    contentValues.put(TableMyDocuments.name, nameX)
                    contentValues.put(TableMyDocuments.startDate, startDate.text.toString())
                    contentValues.put(TableMyDocuments.endDate, endDate.text.toString())
                    contentValues.put(TableMyDocuments.drName, drNameX)
                    contentValues.put(TableMyDocuments.filePath, pojoMyDocuments.filePath)
                    contentValues.put(TableMyDocuments.hospital, hospitalX)
                    contentValues.put(TableMyDocuments.shortDesc, shortDescriptionX)
                    contentValues.put(TableMyDocuments.longDesc, longDesc)
                    context.contentResolver.insert(OurContentProvider.uriMyDocsTable, contentValues)
                }
                dismiss()
            }
            R.id.browseCamera -> activityMyDocumentsList!!.uploadFromCamera()
            R.id.browseGallery -> activityMyDocumentsList!!.selectFile()
            R.id.browse -> if (actAsUpdate) {
                val sharedFileUri = FileProvider.getUriForFile(this.context, this.context.packageName, File(pojoMyDocuments.filePath!!))
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = sharedFileUri
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                this.context.startActivity(intent)
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