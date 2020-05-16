package com.yrdtechnologies.utility

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import com.yrdtechnologies.persistence.AllMyProfileTable
import com.yrdtechnologies.persistence.DatabaseHelper
import com.yrdtechnologies.persistence.FamilyTable
import com.yrdtechnologies.persistence.InsuranceTable
import com.yrdtechnologies.persistence.OurContentProvider
import com.yrdtechnologies.persistence.TableDoctors
import com.yrdtechnologies.persistence.TableMedicines
import com.yrdtechnologies.persistence.TableMyDocuments
import com.yrdtechnologies.pojo.PojoAllProfile
import com.yrdtechnologies.pojo.PojoDoctor
import com.yrdtechnologies.pojo.PojoFamily
import com.yrdtechnologies.pojo.PojoInsurancePolicy
import com.yrdtechnologies.pojo.PojoMedicine
import com.yrdtechnologies.pojo.PojoMyDocuments
import java.util.ArrayList
import java.util.Collections

/**
 * Created by kartikshah on 21/05/16.
 */
class DBUtility(context: Context) {
    private val contentResolver: ContentResolver
    fun checkIfDataIsInserted(): Boolean {
        val countCursor = contentResolver.query(OurContentProvider.uriAllProfileTable, arrayOf("count(*) AS count"),
                null,
                null,
                null)
        if (countCursor != null) {
            countCursor.moveToFirst()
            val count = countCursor.getInt(0)
            countCursor.close()
            return count > 0
        }
        return false
    }

    fun addMyProfileType(type: String?, name: String?, selected: Boolean) {
        val contentValues = ContentValues()
        contentValues.put(AllMyProfileTable.name, name)
        contentValues.put(AllMyProfileTable.type, type)
        contentValues.put(AllMyProfileTable.userSelected, if (selected) 1 else 0)
        contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
    }

    val uniqueSpeciality: ArrayList<PojoDoctor>
        get() {
            val list = ArrayList<PojoDoctor>()
            val c = contentResolver.query(OurContentProvider.uriMyDrTable, arrayOf("distinct " + TableDoctors.speciality), null,
                    null, null)
            while (c != null && c.moveToNext()) {
                val i = PojoDoctor()
                //i.setName(c.getString(c.getColumnIndex(TableDoctors.speciality)));
                i.speciality = c.getString(c.getColumnIndex(TableDoctors.speciality))
                i.type = PojoDoctor.typeSpeciality
                list.add(i)
            }
            c!!.close()
            return list
        }

    val listedMedicines: ArrayList<PojoMedicine>
        get() {
            val list = ArrayList<PojoMedicine>()
            val where = TableMedicines.type + " =? "
            val selectionArgs = arrayOf("" + TableMedicines.typeList)
            val c = contentResolver.query(OurContentProvider.uriMyMedicinesTable, null, where,
                    selectionArgs, null)
            while (c != null && c.moveToNext()) {
                val i = PojoMedicine()
                i.name = c.getString(c.getColumnIndex(TableMedicines.name))
                i.type = TableMedicines.typeList
                list.add(i)
            }
            c!!.close()
            return list
        }

    val usersMedicines: ArrayList<PojoMedicine>
        get() {
            val list = ArrayList<PojoMedicine>()
            val where = TableMedicines.type + " =? "
            val selectionArgs = arrayOf("" + TableMedicines.typeUserMedicine)
            val c = contentResolver.query(OurContentProvider.uriMyMedicinesTable, null, where,
                    selectionArgs, null)
            while (c != null && c.moveToNext()) {
                val i = PojoMedicine()
                i.name = c.getString(c.getColumnIndex(TableMedicines.name))
                i.type = TableMedicines.typeUserMedicine
                i.drName = c.getString(c.getColumnIndex(TableMedicines.drName))
                i.endDate = c.getString(c.getColumnIndex(TableMedicines.endDate))
                i.startDate = c.getString(c.getColumnIndex(TableMedicines.startDate))
                i.evening = c.getString(c.getColumnIndex(TableMedicines.evening))
                i.morning = c.getString(c.getColumnIndex(TableMedicines.morning))
                i.night = c.getString(c.getColumnIndex(TableMedicines.night))
                list.add(i)
            }
            c!!.close()
            return list
        }

    val emergencyContacts: ArrayList<PojoFamily>
        get() {
            val list = ArrayList<PojoFamily>()
            val where = FamilyTable.isEmergencyContact + " =? "
            val selectionArgs = arrayOf("" + 1)
            val c = contentResolver.query(OurContentProvider.uriFamilyTable, null, where,
                    selectionArgs, null)
            while (c != null && c.moveToNext()) {
                val i = PojoFamily()
                i.name = c.getString(c.getColumnIndex(FamilyTable.name))
                i.isEmergencyContact = true
                i.age = c.getString(c.getColumnIndex(FamilyTable.age))
                i.number = c.getString(c.getColumnIndex(FamilyTable.number))
                i.relation = c.getString(c.getColumnIndex(FamilyTable.relation))
                i.cif = c.getString(c.getColumnIndex(FamilyTable.cif))
                list.add(i)
            }
            c!!.close()
            return list
        }

    val familyMembers: ArrayList<PojoFamily>
        get() {
            val list = ArrayList<PojoFamily>()
            val where = FamilyTable.isEmergencyContact + " =? "
            val selectionArgs = arrayOf("" + 0)
            val c = contentResolver.query(OurContentProvider.uriFamilyTable, null, where,
                    selectionArgs, null)
            while (c != null && c.moveToNext()) {
                val i = PojoFamily()
                i.name = c.getString(c.getColumnIndex(FamilyTable.name))
                i.isEmergencyContact = false
                i.age = c.getString(c.getColumnIndex(FamilyTable.age))
                i.number = c.getString(c.getColumnIndex(FamilyTable.number))
                i.relation = c.getString(c.getColumnIndex(FamilyTable.relation))
                i.cif = c.getString(c.getColumnIndex(FamilyTable.cif))
                list.add(i)
            }
            c!!.close()
            return list
        }

    val insurancePolicies: ArrayList<PojoInsurancePolicy>
        get() {
            val list = ArrayList<PojoInsurancePolicy>()
            val c = contentResolver.query(OurContentProvider.uriInsuranceTable, null, null,
                    null, null)
            while (c != null && c.moveToNext()) {
                val i = PojoInsurancePolicy()
                i.name = c.getString(c.getColumnIndex(InsuranceTable.name))
                i.endDate = c.getString(c.getColumnIndex(InsuranceTable.endDate))
                i.startDate = c.getString(c.getColumnIndex(InsuranceTable.startDate))
                i.info = c.getString(c.getColumnIndex(InsuranceTable.info))
                i.policyNumber = c.getString(c.getColumnIndex(InsuranceTable.number))
                list.add(i)
            }
            c!!.close()
            return list
        }

    val allDoctors: ArrayList<PojoDoctor>
        get() {
            val list = ArrayList<PojoDoctor>()
            val c = contentResolver.query(OurContentProvider.uriMyDrTable, null, null,
                    null, null)
            while (c != null && c.moveToNext()) {
                val i = PojoDoctor()
                i.name = c.getString(c.getColumnIndex(TableDoctors.name))
                i.address = c.getString(c.getColumnIndex(TableDoctors.address))
                i.speciality = c.getString(c.getColumnIndex(TableDoctors.speciality))
                i.city = c.getString(c.getColumnIndex(TableDoctors.city))
                i.degree = c.getString(c.getColumnIndex(TableDoctors.degree))
                i.contact = c.getString(c.getColumnIndex(TableDoctors.contact))
                i.pin = c.getString(c.getColumnIndex(TableDoctors.pin))
                i.rating = c.getString(c.getColumnIndex(TableDoctors.rating))
                i.state = c.getString(c.getColumnIndex(TableDoctors.state))
                i.isSelected = if (c.getInt(c.getColumnIndex(TableDoctors.userSelected)) == 1) true else false
                i.type = PojoDoctor.typeOthers
                list.add(i)
            }
            c!!.close()
            return list
        }

    val selectedDoctors: ArrayList<PojoDoctor>
        get() {
            val list = ArrayList<PojoDoctor>()
            val where = TableDoctors.userSelected + " =? "
            val selectionArgs = arrayOf("1")
            val c = contentResolver.query(OurContentProvider.uriMyDrTable, null, where,
                    selectionArgs, null)
            while (c != null && c.moveToNext()) {
                val i = PojoDoctor()
                i.name = c.getString(c.getColumnIndex(TableDoctors.name))
                i.address = c.getString(c.getColumnIndex(TableDoctors.address))
                i.speciality = c.getString(c.getColumnIndex(TableDoctors.speciality))
                i.city = c.getString(c.getColumnIndex(TableDoctors.city))
                i.degree = c.getString(c.getColumnIndex(TableDoctors.degree))
                i.contact = c.getString(c.getColumnIndex(TableDoctors.contact))
                i.pin = c.getString(c.getColumnIndex(TableDoctors.pin))
                i.rating = c.getString(c.getColumnIndex(TableDoctors.rating))
                i.state = c.getString(c.getColumnIndex(TableDoctors.state))
                i.isSelected = true
                i.filePath = c.getString(c.getColumnIndex(TableDoctors.filePath))
                i.fileType = c.getString(c.getColumnIndex(TableDoctors.fileType))
                i.visitDate = c.getString(c.getColumnIndex(TableDoctors.visitDate))
                i.shortDesc = c.getString(c.getColumnIndex(TableDoctors.shortDesc))
                i.longDesc = c.getString(c.getColumnIndex(TableDoctors.longDesc))
                i.type = PojoDoctor.typeOthers
                list.add(i)
            }
            c!!.close()
            return list
        }

    fun getDoctorsByType(type: String): ArrayList<PojoDoctor> {
        val where = TableDoctors.speciality + " =? and " + TableDoctors.userSelected + " =?"
        val selectionArgs = arrayOf(type, "0")
        val list = ArrayList<PojoDoctor>()
        val c = contentResolver.query(OurContentProvider.uriMyDrTable, null, where,
                selectionArgs, null)
        while (c != null && c.moveToNext()) {
            val i = PojoDoctor()
            i.name = c.getString(c.getColumnIndex(TableDoctors.name))
            c.getString(c.getColumnIndex(TableDoctors.address))?.let { i.address = it }
            i.speciality = c.getString(c.getColumnIndex(TableDoctors.speciality))
            i.city = c.getString(c.getColumnIndex(TableDoctors.city))
            i.degree = c.getString(c.getColumnIndex(TableDoctors.degree))
            i.contact = c.getString(c.getColumnIndex(TableDoctors.contact))
            i.pin = c.getString(c.getColumnIndex(TableDoctors.pin))
            i.rating = c.getString(c.getColumnIndex(TableDoctors.rating))
            i.state = c.getString(c.getColumnIndex(TableDoctors.state))
            i.shortDesc = c.getString(c.getColumnIndex(TableDoctors.shortDesc))
            i.longDesc = c.getString(c.getColumnIndex(TableDoctors.longDesc))
            i.type = PojoDoctor.typeOthers
            i.isSelected = false
            list.add(i)
        }
        c!!.close()
        return list
    }

    fun getByType(type: String): ArrayList<PojoAllProfile> {
        var where = ""
        val selectionArgs = arrayOf(type)
        where = AllMyProfileTable.type + " =?"
        val c = contentResolver.query(OurContentProvider.uriAllProfileTable, null, where,
                selectionArgs, AllMyProfileTable.userSelected + " DESC")
        val list = ArrayList<PojoAllProfile>()
        while (c != null && c.moveToNext()) {
            val i = PojoAllProfile()
            val isCorrent = c.getInt(c.getColumnIndex(AllMyProfileTable.userSelected))
            i.isSelected = isCorrent != 0
            i.name = c.getString(c.getColumnIndex(AllMyProfileTable.name))
            i.type = c.getString(c.getColumnIndex(AllMyProfileTable.type))
            list.add(i)
        }
        c!!.close()
        return list
    }

    fun getByTypeSelected(type: String): ArrayList<PojoAllProfile> {
        var where = ""
        val selectionArgs = arrayOf(type, "1")
        where = AllMyProfileTable.type + " =? and " + AllMyProfileTable.userSelected + " =?"
        val c = contentResolver.query(OurContentProvider.uriAllProfileTable, null, where,
                selectionArgs, AllMyProfileTable.userSelected + " DESC")
        val list = ArrayList<PojoAllProfile>()
        while (c != null && c.moveToNext()) {
            val i = PojoAllProfile()
            val isCorrent = c.getInt(c.getColumnIndex(AllMyProfileTable.userSelected))
            i.isSelected = isCorrent != 0
            i.name = c.getString(c.getColumnIndex(AllMyProfileTable.name))
            i.type = c.getString(c.getColumnIndex(AllMyProfileTable.type))
            list.add(i)
        }
        c!!.close()
        return list
    }

    val myDocuments: ArrayList<PojoMyDocuments>
        get() {
            val c = contentResolver.query(OurContentProvider.uriMyDocsTable, null, null,
                    null, null)
            val list = ArrayList<PojoMyDocuments>()
            while (c != null && c.moveToNext()) {
                val i = PojoMyDocuments()
                i.name = c.getString(c.getColumnIndex(TableMyDocuments.name))
                i.drName = c.getString(c.getColumnIndex(TableMyDocuments.drName))
                i.hospital = c.getString(c.getColumnIndex(TableMyDocuments.hospital))
                i.type = c.getString(c.getColumnIndex(TableMyDocuments.type))
                i.filePath = c.getString(c.getColumnIndex(TableMyDocuments.filePath))
                i.details = c.getString(c.getColumnIndex(TableMyDocuments.details))
                i.startDate = c.getString(c.getColumnIndex(TableMyDocuments.startDate))
                i.endDate = c.getString(c.getColumnIndex(TableMyDocuments.endDate))
                i.shortDesc = c.getString(c.getColumnIndex(TableMyDocuments.shortDesc))
                i.longDesc = c.getString(c.getColumnIndex(TableMyDocuments.longDesc))
                list.add(i)
            }
            c!!.close()
            list.reverse()
            return list
        }

    fun updateUserSelected(type: String, name: String, selected: Boolean) {
        val contentValues: ContentValues
        contentValues = ContentValues()
        contentValues.put(AllMyProfileTable.userSelected, if (selected) 1 else 0)
        val where = AllMyProfileTable.type + " =? and " + AllMyProfileTable.name + " =?"
        val selectionArgs = arrayOf(type, name)
        contentResolver.update(OurContentProvider.uriAllProfileTable, contentValues, where, selectionArgs)
    }

    fun insertDrugs() {
        var contentValues: ContentValues
        val drugs = "Antibiotics - amoxicillin,Anti-inflammatory - Ibuprofen,Anti-inflammatory - Naproxen,Aspirin,Sulfa drugs,Chemotherapy drugs,HIV drugs - Abacavir,HIV drugs - Nevirapine ,HIV drugs - Others,Insulin,Antiseizure drugs - Lamotrigine,Antiseizure drugs - Phenytoin,Antiseizure drugs - Carbamazepine,Antiseizure drugs - Others,Antibiotics - Ampicillin,Antibiotics - Penicillin,Antibiotics - Tetracycline,Antibiotics - Others"
        val drugsArray = drugs.split(",").toTypedArray()
        for (drug in drugsArray) {
            contentValues = ContentValues()
            contentValues.put(AllMyProfileTable.name, drug)
            contentValues.put(AllMyProfileTable.type, DatabaseHelper.typeDrugAllProfile)
            contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
        }
    }

    fun insertFood() {
        var contentValues: ContentValues
        val drugs = "Nuts and Seeds,Cereals and Grains,Legumes (incl. peanut),Fruit,Vegetables,Herbs and Spices,Shellfish and Snails,Fish,Milk and Egg"
        val drugsArray = drugs.split(",").toTypedArray()
        for (food in drugsArray) {
            contentValues = ContentValues()
            contentValues.put(AllMyProfileTable.name, food)
            contentValues.put(AllMyProfileTable.type, DatabaseHelper.typeFoodAllProfile)
            contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
        }
    }

    fun insertKnown() {
        var contentValues: ContentValues
        val drugs = "Alzheimer,Arthritis,Asthma,Blood Pressure,Cancer,Cholesterol,Chronic Pain,Cold & Flu,Depression,Diabetes,Digestion Aliment,Eyesight,Hearing Aliment,Heart Aliment,HIV/AIDS,Infectious Disease,Lung Conditions,Menopause,Migraine,Pregnancy,Senior Health,Sleep disorder,Thyroid"
        val drugsArray = drugs.split(",").toTypedArray()
        for (food in drugsArray) {
            contentValues = ContentValues()
            contentValues.put(AllMyProfileTable.name, food)
            contentValues.put(AllMyProfileTable.type, DatabaseHelper.typeKnownAllProfile)
            contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
        }
    }

    fun insertGenetic() {
        var contentValues: ContentValues
        val drugs = "Cystic Fibrosis,Sickle Cell Anemia,Marfan Syndrome,Huntington's disease ,Hemochromatosis"
        val drugsArray = drugs.split(",").toTypedArray()
        for (food in drugsArray) {
            contentValues = ContentValues()
            contentValues.put(AllMyProfileTable.name, food)
            contentValues.put(AllMyProfileTable.type, DatabaseHelper.typeGeneticAllProfile)
            contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
        }
    }

    fun insertFamily() {
        var contentValues: ContentValues
        val drugs = "Heart Disease,High Blood Pressure,Alzheimer'S Disease,Arthritis,Diabetes,Cancer,Obesity"
        val drugsArray = drugs.split(",").toTypedArray()
        for (food in drugsArray) {
            contentValues = ContentValues()
            contentValues.put(AllMyProfileTable.name, food)
            contentValues.put(AllMyProfileTable.type, DatabaseHelper.typeFamilyAllProfile)
            contentResolver.insert(OurContentProvider.uriAllProfileTable, contentValues)
        }
    }

    fun insertMedicinesFromAPI(contentValues:Array<ContentValues>) {
        contentResolver.bulkInsert(OurContentProvider.uriMyMedicinesTable, contentValues)
    }

    fun insertDrs(contentValues: Array<ContentValues>) {

        contentResolver.bulkInsert(OurContentProvider.uriMyDrTable, contentValues)
    }

    init {
        contentResolver = context.contentResolver
    }
}