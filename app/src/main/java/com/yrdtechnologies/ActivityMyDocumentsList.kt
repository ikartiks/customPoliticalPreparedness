package com.yrdtechnologies

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yrdtechnologies.adapters.MyDocumentsRecyclerViewAdapter
import com.yrdtechnologies.dialogs.AddDocumentDialog
import com.yrdtechnologies.interfaces.OnDialogDismissed
import com.yrdtechnologies.interfaces.OnItemClickListener
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.pojo.PojoMyDocuments
import com.yrdtechnologies.utility.DBUtility
import com.yrdtechnologies.utility.DatePickerFragment
import com.yrdtechnologies.utility.FileUtils

@Suppress("DEPRECATION")
class ActivityMyDocumentsList : ActivityBase(), OnItemClickListener, View.OnClickListener, OnDialogDismissed, TextWatcher {
    lateinit var addDocumentDialog: AddDocumentDialog
    var myDocumentsRecyclerViewAdapter: MyDocumentsRecyclerViewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_documents_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        val search = findViewById<EditText>(R.id.Search)
        search.addTextChangedListener(this)
        val floatingActionButton = findViewById<ImageView>(R.id.fab)
        floatingActionButton.setOnClickListener(this)
        initializeListAndShowRecents()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun initializeListAndShowRecents() {
        val dbUtility = DBUtility(this)
        val pojoMyDocuments = dbUtility.myDocuments
        val emptyParent = findViewById<LinearLayout>(R.id.EmptyParent)
        val nonEmptyParent = findViewById<LinearLayout>(R.id.NonEmptyParent)
        if (pojoMyDocuments.isEmpty()) {
            emptyParent.visibility = View.VISIBLE
            nonEmptyParent.visibility = View.GONE
        } else {
            nonEmptyParent.visibility = View.VISIBLE
            emptyParent.visibility = View.GONE
        }
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        myDocumentsRecyclerViewAdapter = MyDocumentsRecyclerViewAdapter(this, pojoMyDocuments, this)
        recyclerView.adapter = myDocumentsRecyclerViewAdapter
        val one = findViewById<LinearLayout>(R.id.One)
        val two = findViewById<LinearLayout>(R.id.Two)
        val three = findViewById<LinearLayout>(R.id.Three)
        val textViewOne = findViewById<TextView>(R.id.NameOne)
        val textViewTwo = findViewById<TextView>(R.id.NameTwo)
        val textViewThree = findViewById<TextView>(R.id.NameThree)
        val dateOne = findViewById<TextView>(R.id.DateOne)
        val dateTwo = findViewById<TextView>(R.id.DateTwo)
        val dateThree = findViewById<TextView>(R.id.DateThree)
        val imageOne = findViewById<ImageView>(R.id.ImageOne)
        val imageTwo = findViewById<ImageView>(R.id.ImageTwo)
        val imageThree = findViewById<ImageView>(R.id.ImageThree)
        one.setOnClickListener(this)
        two.setOnClickListener(this)
        three.setOnClickListener(this)
        if (pojoMyDocuments.size >= 3) {
            textViewOne.text = pojoMyDocuments[0].name
            textViewTwo.text = pojoMyDocuments[1].name
            textViewThree.text = pojoMyDocuments[2].name
            dateOne.text = pojoMyDocuments[0].startDate + " - " + pojoMyDocuments[0].endDate
            dateTwo.text = pojoMyDocuments[1].startDate + " - " + pojoMyDocuments[1].endDate
            dateThree.text = pojoMyDocuments[2].startDate + " - " + pojoMyDocuments[2].endDate
            imageOne.setImageResource(if (pojoMyDocuments[0].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageTwo.setImageResource(if (pojoMyDocuments[1].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageThree.setImageResource(if (pojoMyDocuments[2].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            one.tag = pojoMyDocuments[0]
            two.tag = pojoMyDocuments[1]
            three.tag = pojoMyDocuments[2]
            two.visibility = View.VISIBLE
            three.visibility = View.VISIBLE
        } else if (pojoMyDocuments.size == 2) {
            textViewOne.text = pojoMyDocuments[0].name
            textViewTwo.text = pojoMyDocuments[1].name
            dateOne.text = pojoMyDocuments[0].startDate + " - " + pojoMyDocuments[0].endDate
            dateTwo.text = pojoMyDocuments[1].startDate + " - " + pojoMyDocuments[1].endDate
            imageOne.setImageResource(if (pojoMyDocuments[0].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            imageTwo.setImageResource(if (pojoMyDocuments[1].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            one.tag = pojoMyDocuments[0]
            two.tag = pojoMyDocuments[1]
            two.visibility = View.VISIBLE
            three.visibility = View.INVISIBLE
        } else if (pojoMyDocuments.size == 1) {
            textViewOne.text = pojoMyDocuments[0].name
            dateOne.text = pojoMyDocuments[0].startDate + " - " + pojoMyDocuments[0].endDate
            imageOne.setImageResource(if (pojoMyDocuments[0].type == DatabaseHelper.typePdfMyDocs) R.drawable.pdf_white else R.drawable.jpg_white)
            one.tag = pojoMyDocuments[0]
            three.visibility = View.INVISIBLE
            two.visibility = View.INVISIBLE
        }
    }

    override fun onItemClick(view: View, position: Int, adapter: RecyclerView.Adapter<*>?) {
        val pojoMyDocuments = view.tag as PojoMyDocuments
        showPopup(pojoMyDocuments)
    }

    private fun showPopup(pojoMyDocuments: PojoMyDocuments) {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        addDocumentDialog = AddDocumentDialog(this, pojoMyDocuments)
        addDocumentDialog.activityMyDocumentsList = this
        addDocumentDialog.width = width
        addDocumentDialog.onDialogDismissed = this
        addDocumentDialog.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fab -> {
                val displayMetrics = DisplayMetrics()
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val width = displayMetrics.widthPixels
                addDocumentDialog = AddDocumentDialog(this)
                addDocumentDialog.activityMyDocumentsList = this
                addDocumentDialog.onDialogDismissed = this
                addDocumentDialog.width = width
                addDocumentDialog.show()
            }
            R.id.One -> showPopup(v.tag as PojoMyDocuments)
            R.id.Two -> showPopup(v.tag as PojoMyDocuments)
            R.id.Three -> showPopup(v.tag as PojoMyDocuments)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_landing, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intent = Intent(this, ActivityMaps::class.java)
            startActivity(intent)
            return true
        }
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityMyDocumentsList.requestCode && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data
            val cR = this.contentResolver
            val mime = MimeTypeMap.getSingleton()
            val extension = mime.getExtensionFromMimeType(cR.getType(uri!!))
            val path = FileUtils.generateFileFromStream(this, uri, extension)
            var extensionDoc: String? = null
            extensionDoc = if (extension.equals("pdf", ignoreCase = true)) DatabaseHelper.typePdfMyDocs else DatabaseHelper.typeImageMyDocs
            addDocumentDialog.setDocumentPathType(path, extensionDoc)
        } else if (requestCode == requestCodeForCamera && resultCode == Activity.RESULT_OK) {
            val bitmap = data!!.extras!!["data"] as Bitmap?
            val path = FileUtils.generateFileFromBitmap(this, bitmap, "jpg")
            addDocumentDialog.setDocumentPathType(path, DatabaseHelper.typeImageMyDocs)
        }
    }

    fun selectFile() {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.isNotEmpty()) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        startActivityForResult(intent, requestCode)
    }

    fun uploadFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraPermission)
        } else {
            val chooserIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(chooserIntent, requestCodeForCamera)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermission) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) uploadFromCamera() else showToast("Please grant camera permission")
        }
    }

    fun showDatePickerDialog(v: View?) {
        val newFragment = DatePickerFragment()
        newFragment.button = v as Button?
        newFragment.show(fragmentManager, "datePicker")
    }

    override fun onDialogDismissed() {
        initializeListAndShowRecents()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        myDocumentsRecyclerViewAdapter!!.filter(null, s.toString())
    }

    companion object {
        const val requestCode = 1
        const val requestCodeForCamera = 2
        const val cameraPermission = 4
    }
}