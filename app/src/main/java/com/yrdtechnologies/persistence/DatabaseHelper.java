package com.yrdtechnologies.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    public static final String typeDrugAllProfile = "drug" ;
    public static final String typeFoodAllProfile = "food" ;
    public static final String typeGeneticAllProfile = "genetic" ;
    public static final String typeFamilyAllProfile = "family" ;
    public static final String typeKnownAllProfile = "known" ;
    public static final String typeDoctorsAllProfile = "doctors" ;
    public static final String typeMedicineAllProfile = "medicine" ;

    public static final String allMyProfileTableAuthority = "allMyProfileTableAuthority" ;
    public static final String allMyProfileTableName = "allMyProfileTable" ;
    public static final String createAllMyProfileTable = "CREATE TABLE "+ allMyProfileTableName +" ("
            + AllMyProfileTable.id + " INTEGER,"
            + AllMyProfileTable.type + " TEXT,"
            + AllMyProfileTable.name + " TEXT,"
            + AllMyProfileTable.userSelected + " INTEGER DEFAULT 0)";


    //MyDocuments

    public static final String typeImageMyDocs = "image" ;
    public static final String typePdfMyDocs = "pdf" ;

    public static final String myDocsAuthority = "allMyDocsAuthority" ;
    public static final String myDocsTableName = "myDocsTable" ;
    public static final String createMyDocsTable = "CREATE TABLE "+ myDocsTableName +" ("
            + TableMyDocuments.id + " INTEGER,"
            + TableMyDocuments.type + " TEXT,"
            + TableMyDocuments.details + " TEXT,"
            + TableMyDocuments.endDate + " TEXT,"
            + TableMyDocuments.filePath + " TEXT,"
            + TableMyDocuments.startDate + " TEXT,"
            + TableMyDocuments.longDesc + " TEXT,"
            + TableMyDocuments.drName + " TEXT,"
            + TableMyDocuments.shortDesc + " TEXT,"
            + TableMyDocuments.hospital + " TEXT,"
            + TableMyDocuments.name + " TEXT )";

    //MyDr

    public static final String myDrAuthority = "allMyDrAuthority" ;
    public static final String myDrTableName = "myDrTable" ;
    public static final String createMyDrTable = "CREATE TABLE "+ myDrTableName +" ("
            + TableDoctors.id + " TEXT,"
            + TableDoctors.speciality + " TEXT,"
            + TableDoctors.address + " TEXT,"
            + TableDoctors.city + " TEXT,"
            + TableDoctors.degree + " TEXT,"
            + TableDoctors.pin + " TEXT,"
            + TableDoctors.visitDate + " TEXT,"
            + TableDoctors.filePath + " TEXT,"
            + TableDoctors.fileType + " TEXT,"
            + TableDoctors.rating + " TEXT,"
            + TableDoctors.contact + " TEXT,"
            + TableDoctors.shortDesc + " TEXT,"
            + TableDoctors.longDesc + " TEXT,"
            + TableDoctors.state + " TEXT,"
            + TableDoctors.userSelected + " INTEGER DEFAULT 0,"
            + TableDoctors.name + " TEXT )";


    //MyMedicine

    public static final String myMedicineAuthority = "myMedicineAuthority" ;
    public static final String myMedicineTableName = "myMedicineTable" ;
    public static final String createMyMedicineTable = "CREATE TABLE "+ myMedicineTableName +" ("
            + TableMedicines.id + " INTEGER,"
            + TableMedicines.type + " TEXT,"
            + TableMedicines.drName + " TEXT,"
            + TableMedicines.endDate + " TEXT,"
            + TableMedicines.startDate + " TEXT,"
            + TableMedicines.evening+ " TEXT,"
            + TableMedicines.morning+ " TEXT,"
            + TableMedicines.night+ " TEXT,"
            + TableDoctors.userSelected + " INTEGER DEFAULT 0,"
            + TableDoctors.name + " TEXT )";

    //Family

    public static final String familyAuthority = "familyAuthority" ;
    public static final String familyTableName = "familyTable" ;
    public static final String createFamilyTable = "CREATE TABLE "+ familyTableName +" ("
            + FamilyTable.id + " INTEGER,"
            + FamilyTable.age + " TEXT,"
            + FamilyTable.name + " TEXT,"
            + FamilyTable.relation + " TEXT,"
            + FamilyTable.number + " TEXT,"
            + FamilyTable.cif + " TEXT,"
            + FamilyTable.isEmergencyContact + " TEXT )";

    //Insurance

    public static final String insuranceAuthority = "insuranceAuthority" ;
    public static final String insuranceTableName = "insuranceTable" ;
    public static final String createInsuranceTable = "CREATE TABLE "+ insuranceTableName +" ("
            + InsuranceTable.id + " INTEGER,"
            + InsuranceTable.endDate + " TEXT,"
            + InsuranceTable.info + " TEXT,"
            + InsuranceTable.name + " TEXT,"
            + InsuranceTable.startDate + " TEXT,"
            + InsuranceTable.number+ " TEXT )";

    private static volatile DatabaseHelper instance;

    private Context context;

    private DatabaseHelper(Context context) {
        super(context, "database.db", null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAllMyProfileTable);
        db.execSQL(createMyDocsTable);
        db.execSQL(createMyDrTable);
        db.execSQL(createMyMedicineTable);
        db.execSQL(createInsuranceTable);
        db.execSQL(createFamilyTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
