package com.yrdtechnologies.persistence;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by kartikshah on 29/05/17.
 */
public class OurContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.yrdtechnologies.provider";



    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int intUriAllProfileTable = 0;
    public static final Uri uriAllProfileTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.allMyProfileTableAuthority);


    private static final int intUriMyDocsTable = 1;
    public static final Uri uriMyDocsTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.myDocsAuthority);

    private static final int intUriMyDrTable = 2;
    public static final Uri uriMyDrTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.myDrAuthority);

    private static final int intUriMyMedicinesTable = 3;
    public static final Uri uriMyMedicinesTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.myMedicineAuthority);

    private static final int intUriInsuranceTable = 4;
    public static final Uri uriInsuranceTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.insuranceAuthority);

    private static final int intUriFamilyTable = 5;
    public static final Uri uriFamilyTable = Uri.parse("content://" + AUTHORITY + "/"+DatabaseHelper.familyAuthority);

    static {
        MATCHER.addURI(AUTHORITY, DatabaseHelper.allMyProfileTableAuthority, intUriAllProfileTable);
        MATCHER.addURI(AUTHORITY, DatabaseHelper.myDocsAuthority, intUriMyDocsTable);
        MATCHER.addURI(AUTHORITY, DatabaseHelper.myDrAuthority, intUriMyDrTable);
        MATCHER.addURI(AUTHORITY, DatabaseHelper.myMedicineAuthority, intUriMyMedicinesTable);
        MATCHER.addURI(AUTHORITY, DatabaseHelper.insuranceAuthority, intUriInsuranceTable);
        MATCHER.addURI(AUTHORITY, DatabaseHelper.familyAuthority, intUriFamilyTable);
    }

    private SQLiteOpenHelper database;

    @Override
    public boolean onCreate() {
        database = DatabaseHelper.getInstance(getContext());
        return true;
    }

    private SelectionBuilder getBuilder(String table) {
        SelectionBuilder builder = new SelectionBuilder();
        return builder;
    }

    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = values[i];
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            switch(MATCHER.match(uri)) {
                case intUriAllProfileTable: {
                    long[] ids = insertValues(db, DatabaseHelper.allMyProfileTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case intUriMyDocsTable: {
                    long[] ids = insertValues(db, DatabaseHelper.myDocsTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case intUriMyDrTable: {
                    long[] ids = insertValues(db, DatabaseHelper.myDrTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case intUriMyMedicinesTable: {
                    long[] ids = insertValues(db, DatabaseHelper.myMedicineTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case intUriFamilyTable: {
                    long[] ids = insertValues(db, DatabaseHelper.familyTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
                case intUriInsuranceTable: {
                    long[] ids = insertValues(db, DatabaseHelper.insuranceTableName, values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
        ContentProviderResult[] results;
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            results = super.applyBatch(ops);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return results;
    }

    @Override
    public String getType(Uri uri) {
        switch(MATCHER.match(uri)) {
            case intUriAllProfileTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.allMyProfileTableAuthority;
            }

            case intUriMyDocsTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.myDocsAuthority;
            }
            case intUriMyDrTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.myDrAuthority;
            }
            case intUriMyMedicinesTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.myMedicineAuthority;
            }
            case intUriFamilyTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.familyAuthority;
            }
            case intUriInsuranceTable: {
                return "vnd.android.cursor.dir/"+DatabaseHelper.insuranceAuthority;
            }

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch(MATCHER.match(uri)) {
            case intUriAllProfileTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.allMyProfileTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.allMyProfileTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }

            case intUriMyDocsTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDocsTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.myDocsTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case intUriMyDrTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDrTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.myDrTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }

            case intUriMyMedicinesTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myMedicineTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.myMedicineTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case intUriFamilyTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.familyTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.familyTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            case intUriInsuranceTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.insuranceTableName);
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(DatabaseHelper.insuranceTableName)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch(MATCHER.match(uri)) {
            case intUriAllProfileTable: {
                final long id = db.insertOrThrow(DatabaseHelper.allMyProfileTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }

            case intUriMyDocsTable: {
                final long id = db.insertOrThrow(DatabaseHelper.myDocsTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }

            case intUriMyDrTable: {
                final long id = db.insertOrThrow(DatabaseHelper.myDrTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }

            case intUriMyMedicinesTable: {
                final long id = db.insertOrThrow(DatabaseHelper.myMedicineTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            case intUriFamilyTable: {
                final long id = db.insertOrThrow(DatabaseHelper.familyTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }

            case intUriInsuranceTable: {
                final long id = db.insertOrThrow(DatabaseHelper.insuranceTableName, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch(MATCHER.match(uri)) {
            case intUriAllProfileTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.allMyProfileTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.allMyProfileTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            case intUriMyDocsTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDocsTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.myDocsTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            case intUriMyDrTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDrTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.myDrTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            case intUriMyMedicinesTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myMedicineTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.myMedicineTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            case intUriFamilyTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.familyTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.familyTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            case intUriInsuranceTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.insuranceTableName);
                builder.where(where, whereArgs);
                final int count = builder.table(DatabaseHelper.insuranceTableName)
                        .update(db, values);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;
            }

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch(MATCHER.match(uri)) {
            case intUriAllProfileTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.allMyProfileTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.allMyProfileTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            case intUriMyDocsTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDocsTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.myDocsTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            case intUriMyDrTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myDrTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.myDrTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            case intUriMyMedicinesTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.myMedicineTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.myMedicineTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            case intUriFamilyTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.familyTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.familyTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            case intUriInsuranceTable: {
                SelectionBuilder builder = getBuilder(DatabaseHelper.insuranceTableName);
                builder.where(where, whereArgs);
                final int count = builder
                        .table(DatabaseHelper.insuranceTableName)
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }

            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }
}
