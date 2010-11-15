package info.collide.android.scydatacollector;

import info.collide.android.scydatacollector.DataFormElementModel.DataFormElementTypes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class DataCollectorContentProvider extends ContentProvider {

    private static final String TAG = "DBDataCollector";

    private static final String DATABASE_NAME = "datacollector";

    private static final int DATABASE_VERSION = 9;

    // ContentProvider Name
    public static final String PROVIDER_NAME = "info.collide.android.scydatacollector.DataCollectorProvider.Forms";

    // URI fŸr ContentProvider
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/forms");

    // ContentProvider URI IDs
    private static final int FORMS = 1;

    private static final int FORMS_ID = 2;

    private static final int FORMS_ID_ELEMENTS = 3;

    private static final int FORMS_ELEMENTS_ID = 4;

    private static final int FORMS_ELEMENTS_ID_EVENTS = 5;

    private static final int FORMS_ELEMENTS_EVENTS_ID = 6;

    private static final int FORMS_ELEMENTS_FORM_ID_POS = 7;

    // private static final int FORMS_ELEMENTS_ID_EVENTS_ID = 6;

    // URI Matcher fŸr Anfragen an ContentProvider
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Anfrage Ÿber alle Forms
        uriMatcher.addURI(PROVIDER_NAME, "forms", FORMS);
        // Anfrage an ein Form mit Angabe der ID
        uriMatcher.addURI(PROVIDER_NAME, "forms/#", FORMS_ID);
        // Anfrage an alle Felder eines Forms mit Angabe der Feld ID
        uriMatcher.addURI(PROVIDER_NAME, "forms/#/elements", FORMS_ID_ELEMENTS);
        // Anfrage an alle Gepeicherten Daten zu einem Feld mit Angabe der Feld
        // ID
        uriMatcher.addURI(PROVIDER_NAME, "forms/elements/#/elementdata", FORMS_ELEMENTS_ID);
        uriMatcher.addURI(PROVIDER_NAME, "forms/elements/#/events", FORMS_ELEMENTS_ID_EVENTS);
        uriMatcher.addURI(PROVIDER_NAME, "forms/elements/events/#/eventdata", FORMS_ELEMENTS_EVENTS_ID);
        uriMatcher.addURI(PROVIDER_NAME, "forms/*/elements/#/elementdata/#", FORMS_ELEMENTS_FORM_ID_POS);

    }

    // Beschreibung der Tabellen und der Felder
    // Felder der Formulartabelle
    private static final String DATABASE_FORMTABLE = "forms";

    public static final String KEY_FORMID = "_id";

    public static final String KEY_FORMDESCRIPTION = "description";

    public static final String KEY_FORMTITLE = "title";

    public static final String KEY_FORMUSER = "username";

    public static final String KEY_FORMVERSION = "version";

    private static final String DATABASE_CREATEFORM = "create table " + DATABASE_FORMTABLE + " (" + KEY_FORMID + " integer primary key autoincrement, " + KEY_FORMDESCRIPTION + " text, " + KEY_FORMTITLE + " text, " + KEY_FORMUSER + " text, " + KEY_FORMVERSION + " text);";

    // Felder der Feldertabelle (Felder eines Formulars)
    private static final String DATABASE_ELEMENTTABLE = "elements";

    public static final String KEY_ELEMENTFORMID = "formid";

    public static final String KEY_ELEMENTID = "_id";

    public static final String KEY_ELEMENTTITLE = "title";

    public static final String KEY_ELEMENTTYPE = "type";

    public static final String KEY_ELEMENTCARDINALITY = "cardinality";

    // Referentielle IntegritŠt wir nicht unterstŸtzt in SQLLite 3.0
    private static final String DATABASE_CREATEELEMENTS = "create table " + DATABASE_ELEMENTTABLE + " (" + KEY_ELEMENTFORMID + " integer not null, " + KEY_ELEMENTID + " integer primary key autoincrement, " + KEY_ELEMENTTITLE + " text not null, " + KEY_ELEMENTTYPE + " text not null, " + KEY_ELEMENTCARDINALITY + " text not null);";

    // Felder der Felddatentabelle (Daten eines Feldes in einem Formular)
    private static final String DATABASE_ELEMENTDATATABLE = "elementsdata";

    public static final String KEY_ELEMENTDATAELEMENID = "elementid";

    public static final String KEY_ELEMENTDATAID = "_id";

    public static final String KEY_ELEMENTDATASTOREDDATA = "storeddata";

    // Referentielle IntegritŠt wir nicht unterstŸtzt in SQLLite 3.0
    private static final String DATABASE_CREATEELEMENTSDATA = "create table " + DATABASE_ELEMENTDATATABLE + " (" + KEY_ELEMENTDATAELEMENID + " integer not null, " + KEY_ELEMENTDATAID + " integer primary key autoincrement, " + KEY_ELEMENTDATASTOREDDATA + " blob);";

    // Felder der Eventstabelle (Events eines Feldes)
    private static final String DATABASE_EVENTSTABLE = "events";

    public static final String KEY_EVENTELEMENTID = "elementid";

    public static final String KEY_EVENTID = "_id";

    public static final String KEY_EVENTTYPE = "eventtype";

    public static final String KEY_EVENTDATATYPE = "eventdatatype";

    // public static final String KEY_EVENTDATA = "eventdata";

    private static final String DATABASE_CREATEEVENTS = "create table " + DATABASE_EVENTSTABLE + " (" + KEY_EVENTELEMENTID + " integer not null, " + KEY_EVENTID + " integer primary key autoincrement, " + KEY_EVENTTYPE + " text not null, " + KEY_EVENTDATATYPE + " text not null);";

    // Felder der EventDatentabelle (Daten eines Events)
    private static final String DATABASE_EVENTDATATABLE = "eventdata";

    //
    public static final String KEY_EVENTDATAEVENTID = "eventid";

    public static final String KEY_EVENTDATAID = "eventdataid";

    // public static final String KEY_EVENTDATATYPE = "eventdatatype";
    public static final String KEY_EVENTDATA = "eventdata";

    private static final String DATABASE_CREATEEVENTSDATA = "create table " + DATABASE_EVENTDATATABLE + " (" + KEY_EVENTDATAEVENTID + " integer not null, " + KEY_EVENTDATAID + " integer primary key autoincrement, " + KEY_EVENTDATA + " blob);";

    private static DatabaseHelper DBHelper;

    private SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATEFORM);
                db.execSQL(DATABASE_CREATEELEMENTS);
                db.execSQL(DATABASE_CREATEELEMENTSDATA);
                db.execSQL(DATABASE_CREATEEVENTS);
                db.execSQL(DATABASE_CREATEEVENTSDATA);
            } catch (Exception ex) {
                Log.e("SQL CREATE DB", ex.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_FORMTABLE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ELEMENTTABLE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_ELEMENTDATATABLE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_EVENTSTABLE + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_EVENTDATATABLE + ";");
            onCreate(db);
        }
    }

    // ---opens the database---
    private DataCollectorContentProvider open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }

    // --- returns DataCollectorFormModell
    public void getDCFM(Activity context, long formID, DataCollectorFormModel dcfm) {
        Cursor formsCursor = getFormsCursor(context, formID);
        Cursor formElementsCursor = getFormElementsCursor(context, formID);
        cursorToDCFM(dcfm, formsCursor, formElementsCursor, context);
    }

    /**
     * @param _context
     * @param formID
     * @return
     */
    private static Cursor getFormsCursor(Activity _context, long formID) {
        return _context.managedQuery(Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/" + formID), null, null, null, null);
    }

    private Cursor getFormElementsCursor(long formID) {
        Uri uri = Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/" + formID + "/elements");
        return query(uri, null, null, null, null);
    }

    private static Cursor getFormElementsCursor(Activity _context, long formID) {
        return _context.managedQuery(Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/" + formID + "/elements"), null, null, null, null);
    }

    private static Cursor getFormElementsDataCursor(Activity _context, long elementID) {
        return _context.managedQuery(Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/elements/" + elementID + "/elementdata"), null, null, null, null);
    }

    private Cursor getFormElementsEventsCursor(long elementID) {
        Uri uri = Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/elements/" + elementID + "/events");
        return query(uri, null, null, null, null);
    }

    private static Cursor getFormElementsEventsCursor(Activity _context, long elementID) {
        return _context.managedQuery(Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/elements/" + elementID + "/events"), null, null, null, null);

    }

    private static Cursor getFormElementsEventsDataCursor(Activity _context, long eventID) {
        return _context.managedQuery(Uri.parse("content://info.collide.android.scydatacollector.DataCollectorProvider.Forms/forms/elements/events/" + eventID + "/eventdata"), null, null, null, null);

    }

    // ---insert a title into the database---
    public long insertForm(DataCollectorFormModel dcfm, String formuser) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FORMDESCRIPTION, dcfm.getDescription());
        initialValues.put(KEY_FORMTITLE, dcfm.getTitle());
        initialValues.put(KEY_FORMUSER, formuser);
        return db.insert(DATABASE_FORMTABLE, null, initialValues);
    }

    // ---deletes a particular title---
    public boolean deleteForm(String title) {
        // db.de
        // db.
        return db.delete(DATABASE_FORMTABLE, KEY_FORMTITLE + "='" + title + "'", null) > 0;
    }

    public boolean deleteForm(Long id) {
        // db.de
        // db.
        return db.delete(DATABASE_FORMTABLE, KEY_FORMID + "=" + id + "", null) > 0;
    }

    // ---deletes a particular title---
    public int deleteElements(long formid) {
        Cursor cElements = getFormElementsCursor(formid);
        cElements.moveToFirst();
        for (int i = 0; i < cElements.getCount(); i++) {
            long elementId = cElements.getLong(cElements.getColumnIndex(KEY_ELEMENTID));
            db.delete(DATABASE_ELEMENTDATATABLE, KEY_ELEMENTDATAELEMENID + "=" + elementId, null);
            Cursor cEvents = getFormElementsEventsCursor(elementId);
            cEvents.moveToFirst();
            for (int j = 0; j < cEvents.getCount(); j++) {
                long eventId = cEvents.getLong(cEvents.getColumnIndex(KEY_EVENTID));
                db.delete(DATABASE_EVENTDATATABLE, KEY_EVENTDATAEVENTID + "=" + eventId, null);
                cEvents.moveToNext();
            }
            cEvents.close();
            db.delete(DATABASE_EVENTSTABLE, KEY_EVENTELEMENTID + "=" + elementId, null);
            cElements.moveToNext();
        }
        cElements.close();
        return db.delete(DATABASE_ELEMENTTABLE, KEY_ELEMENTFORMID + "=" + formid, null);
    }

    // ---retrieves all the titles---
    public Cursor getAllForms() {
        return db.query(DATABASE_FORMTABLE, new String[] { KEY_FORMID, KEY_FORMDESCRIPTION, KEY_FORMTITLE, KEY_FORMUSER }, null, null, null, null, null);
    }

    // ---retrieves a particular title---
    public Cursor getForm(String formtitle) throws SQLException {
        Cursor mCursor;
        try {
            mCursor = db.query(true, DATABASE_FORMTABLE, new String[] { KEY_FORMID, KEY_FORMDESCRIPTION, KEY_FORMTITLE, KEY_FORMUSER }, KEY_FORMTITLE + "=?", new String[] { formtitle }, null, null, null, null);
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        } catch (Exception e) {
            mCursor = null;
        }
        return mCursor;
    }

    public long getFormKey(String title) {
        long result = -1;
        if (getForm(title) != null) {
            Cursor cursor = getForm(title);
            if (cursor.getCount() > 0) {
                result = cursor.getLong(cursor.getColumnIndex(KEY_FORMID));
            }
            cursor.close();
        }
        return result;
    }

    public boolean updateFormElements(DataCollectorFormModel dcfm) {
        long key = getFormKey(dcfm.getTitle());

        if (key > -1) {
            deleteElements(key);
            insertFormElements(dcfm);
        }
        return true;
    }

    private boolean insertFormElements(DataCollectorFormModel dcfm) {
        for (DataFormElementModel dfem : dcfm.getDfElements()) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_ELEMENTFORMID, getFormKey(dcfm.getTitle()));
            initialValues.put(KEY_ELEMENTTITLE, dfem.getTitle());
            initialValues.put(KEY_ELEMENTTYPE, dfem.getType().toString());
            initialValues.put(KEY_ELEMENTCARDINALITY, dfem.getCardinality().toString());
            long newElementID = -1;
            try {
                newElementID = dbInsertReturnID(DATABASE_ELEMENTTABLE, KEY_ELEMENTID, initialValues);
            } catch (Exception ex) {
                Log.e("SQL INSERT ELEMENTS", ex.getLocalizedMessage());
            }
            if (newElementID > -1) {
                if (dfem.getEvents() != null) {
                    for (DataFormElementEventModel dfeem : dfem.getEvents()) {
                        ContentValues cvEvent = new ContentValues();

                        cvEvent.put(KEY_EVENTELEMENTID, newElementID);
                        cvEvent.put(KEY_EVENTTYPE, dfeem.getEventType().toString());
                        cvEvent.put(KEY_EVENTDATATYPE, dfeem.getEventDataType().toString());

                        long newEventId = dbInsertReturnID(DATABASE_EVENTSTABLE, KEY_EVENTID, cvEvent);

                        for (byte[] bytearray : dfeem.getDataList()) {
                            ContentValues cvEventData = new ContentValues();
                            cvEventData.put(KEY_EVENTDATAEVENTID, newEventId);
                            cvEventData.put(KEY_EVENTDATA, bytearray);
                            db.insert(DATABASE_EVENTDATATABLE, null, cvEventData);

                        }
                    }
                }
                if (dfem.getDataList() != null) {
                    for (byte[] array_element : dfem.getDataList()) {
                        ContentValues cvElementData = new ContentValues();
                        cvElementData.put(KEY_ELEMENTDATAELEMENID, newElementID);
                        cvElementData.put(KEY_ELEMENTDATASTOREDDATA, array_element);
                        db.insert(DATABASE_ELEMENTDATATABLE, null, cvElementData);
                    }
                }
            }
        }
        return true;
    }

    public long dbInsertReturnID(String table, String idField, ContentValues cv) {
        long newRowId = -1;
        newRowId = db.insert(table, null, cv);
        // Cursor newRow = db.rawQuery("SELECT " + idField + " FROM " + table
        // + " WHERE ROWID = " + newRowId + ";", null);
        // newRow.moveToFirst();
        // Long newFieldID = newRow.getLong(newRow.getColumnIndex(idField));
        // return newFieldID;
        return newRowId;

    }

    // ---updates a title---
    public int updateForm(DataCollectorFormModel dcfm, String username) {
        updateFormElements(dcfm);
        ContentValues args = new ContentValues();
        args.put(KEY_FORMDESCRIPTION, dcfm.getDescription());
        args.put(KEY_FORMTITLE, dcfm.getTitle());
        args.put(KEY_FORMUSER, username);
        return db.update(DATABASE_FORMTABLE, args, KEY_FORMTITLE + "=? AND " + KEY_FORMUSER + "=?", new String[] { dcfm.getTitle(), username });

    }

    public static DataCollectorFormModel cursorToDCFM(DataCollectorFormModel dcfm, Cursor form, Cursor formElements, Activity _context) {

        form.moveToFirst();
        dcfm.setTitle(form.getString(form.getColumnIndex(KEY_FORMTITLE)));
        dcfm.setDescription(form.getString(form.getColumnIndex(KEY_FORMDESCRIPTION)));

        ArrayList<DataFormElementModel> dfElements = new ArrayList<DataFormElementModel>();
        formElements.moveToFirst();
        if (formElements.getCount() > 0) {
            form.moveToFirst();

            // formElements.moveToFirst();
            for (int i = 0; i < formElements.getCount(); i++) {
                DataFormElementModel dfem = new DataFormElementModel(dcfm);

                dfem.setTitle(formElements.getString(formElements.getColumnIndex(KEY_ELEMENTTITLE)));
                dfem.setType(DataFormElementTypes.valueOf(formElements.getString(formElements.getColumnIndex(KEY_ELEMENTTYPE))));
                dfem.setCardinality(formElements.getString(formElements.getColumnIndex(KEY_ELEMENTCARDINALITY)));

                Cursor elementData = getFormElementsDataCursor(_context, formElements.getLong(formElements.getColumnIndex(KEY_ELEMENTID)));

                elementData.moveToFirst();
                ArrayList<byte[]> data = new ArrayList<byte[]>();
                if (elementData.getCount() > 0) {
                    elementData.moveToFirst();

                    for (int j = 0; j < elementData.getCount(); j++) {
                        data.add(elementData.getBlob(elementData.getColumnIndex(KEY_ELEMENTDATASTOREDDATA)));
                        elementData.moveToNext();
                    }
                }
                dfem.setDataList(data);

                ArrayList<DataFormElementEventModel> dfeemElements = new ArrayList<DataFormElementEventModel>();
                Cursor elementEvents = getFormElementsEventsCursor(_context, formElements.getLong(formElements.getColumnIndex(KEY_ELEMENTID)));
                elementEvents.moveToFirst();

                if (elementEvents.getCount() > 0) {
                    for (int j = 0; j < elementEvents.getCount(); j++) {
                        DataFormElementEventModel dfeem = new DataFormElementEventModel(dfem);
                        dfeem.setEventType(DataFormElementEventModel.DataFormElementEventTypes.valueOf(elementEvents.getString(elementEvents.getColumnIndex(KEY_EVENTTYPE))));
                        dfeem.setEventDataType(DataFormElementEventModel.DataFormElementEventDataTypes.valueOf(elementEvents.getString(elementEvents.getColumnIndex(KEY_EVENTDATATYPE))));
                        Cursor cEventData = getFormElementsEventsDataCursor(_context, elementEvents.getLong(elementEvents.getColumnIndex(KEY_EVENTID)));
                        cEventData.moveToFirst();

                        ArrayList<byte[]> eventdata = new ArrayList<byte[]>();

                        if (cEventData.getCount() > 0) {
                            for (int k = 0; k < cEventData.getCount(); k++) {
                                eventdata.add(cEventData.getBlob(cEventData.getColumnIndex(KEY_EVENTDATA)));
                                cEventData.moveToNext();
                            }
                        }
                        dfeem.setDataList(eventdata);
                        dfeemElements.add(dfeem);
                        elementEvents.moveToNext();
                    }

                }
                dfem.setEvents(dfeemElements);
                dfElements.add(dfem);
                formElements.moveToNext();
            }

            dcfm.getDfElements().clear();
            dcfm.getDfElements().addAll(dfElements);

        }
        return dcfm;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Cursor c = null;

        switch (uriMatcher.match(uri)) {
            case FORMS_ELEMENTS_FORM_ID_POS:

                // forms/#/elements/#/elementdata/#

                int formID = (int) getFormKey(uri.getPathSegments().get(1));
                int elementPos = Integer.valueOf(uri.getPathSegments().get(3));
                int elementDataPos = Integer.valueOf(uri.getPathSegments().get(5));

                SQLiteQueryBuilder sqlBuilder1 = new SQLiteQueryBuilder();

                sqlBuilder1.setTables(DATABASE_ELEMENTTABLE);
                sqlBuilder1.appendWhere(KEY_ELEMENTFORMID + " = " + formID);
                c = sqlBuilder1.query(db, new String[] { KEY_ELEMENTID }, null, null, null, null, null);

                c.moveToFirst();
                c.move((int) elementPos);

                long elementID = c.getLong(c.getColumnIndex(KEY_ELEMENTID));

                SQLiteQueryBuilder sqlBuilder2 = new SQLiteQueryBuilder();

                sqlBuilder2.setTables(DATABASE_ELEMENTDATATABLE);

                sqlBuilder2.appendWhere(KEY_ELEMENTDATAELEMENID + " = " + elementID);
                c.close();
                c = sqlBuilder2.query(db, new String[] { KEY_ELEMENTDATAID }, null, null, null, null, null);

                c.move((int) elementDataPos);

                long elementDataID = c.getLong(c.getColumnIndex(KEY_ELEMENTDATAID));

                long delElementDataCount = db.delete(DATABASE_ELEMENTDATATABLE, KEY_ELEMENTDATAID + "=" + elementDataID, null);

                break;
            case FORMS:
                deleteElements(Long.valueOf(selection));
                deleteForm(Long.valueOf(selection));

                break;
        }

        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // ---get all books---
            case FORMS:
                return "vnd.android.cursor.dir/vnd.android.client.forms";
                // ---get a particular book---
            case FORMS_ID:
                return "vnd.android.cursor.item/vnd.android.client.forms ";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor c = null;

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case FORMS_ELEMENTS_EVENTS_ID:
                sqlBuilder.setTables(DATABASE_EVENTDATATABLE);
                sqlBuilder.appendWhere(KEY_EVENTDATAEVENTID + " = " + uri.getPathSegments().get(3));

                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case FORMS_ELEMENTS_ID_EVENTS:
                sqlBuilder.setTables(DATABASE_EVENTSTABLE);
                sqlBuilder.appendWhere(KEY_EVENTELEMENTID + " = " + uri.getPathSegments().get(2));

                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case FORMS_ELEMENTS_ID:
                sqlBuilder.setTables(DATABASE_ELEMENTDATATABLE);
                sqlBuilder.appendWhere(KEY_ELEMENTDATAELEMENID + " = " + uri.getPathSegments().get(2));

                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case FORMS_ID_ELEMENTS:
                sqlBuilder.setTables(DATABASE_ELEMENTTABLE);
                sqlBuilder.appendWhere(KEY_ELEMENTFORMID + " = " + uri.getPathSegments().get(1));
                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FORMS:
                sqlBuilder.setTables(DATABASE_FORMTABLE);
                if (uriMatcher.match(uri) == FORMS_ID)
                    sqlBuilder.appendWhere(KEY_FORMID + " = " + uri.getPathSegments().get(1));

                if (sortOrder == null || sortOrder == "")
                    sortOrder = KEY_FORMTITLE;

                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case FORMS_ID:
                sqlBuilder.setTables(DATABASE_FORMTABLE);
                sqlBuilder.appendWhere(KEY_FORMID + " = " + uri.getPathSegments().get(1));

                if (sortOrder == null || sortOrder == "")
                    sortOrder = KEY_FORMTITLE;

                c = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
        }

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        DataCollectorFormModel dcfm =  DataCollectorFormModel.fromByteArray(values.getAsByteArray("dcfm"));
        String username = values.getAsString("username");
        if (dcfm != null && username != null) {
            long key = getFormKey(dcfm.getTitle());
            if (key > -1) {
                updateForm(dcfm, username);
            } else {
                insertForm(dcfm, username);
                insertFormElements(dcfm);
            }
        }
        return 0;
    }
}