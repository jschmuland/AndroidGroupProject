package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by James Thibaudeau on 2017-03-21.
 */

public class AppDBHelper extends SQLiteOpenHelper {
    //Activity name
    public final static String ACTIVITY_NAME = "AppDBHelper";
    //Table names
    public final static String MEALPLAN_TABLE = "MEALPLAN";
    public final static String MEALS_TABLE = "MEALS";
    public final static String MEALPLAN_HAS_MEALS = "MEALPLAN_HAS_MEALS";
    public final static String EXERCISE_TABLE = "EXERCISE";
    public final static String SLEEP_TABLE = "SLEEP";
    public final static String FOOD_TABLE = "FOOD";
    //Common columns
    public final static String KEY_ID = "_ID";
    public final static String DATE = "DATE";
    public final static String CALORIES = "CALORIES";
    public final static String FOOD_ITEM = "FOOD_ITEM";
    //Sleep table columns
    public final static String HOURS_SLEPT = "HOURS_SLEPT";
    //Meal table columns

    public final static String MEAL_NAME = "MEAL_NAME";

    //Exercise table columns
    public final static String EXERCISE_NAME = "EXERCISE_NAME";
    public final static String EXERCISE_DURATION = "DURATION";
    //Create table queries
    public final static String SLEEP_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DATETIME, %s INTEGER);", SLEEP_TABLE, KEY_ID, DATE, HOURS_SLEPT);
    public final static String FOOD_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DATETIME, %s TEXT, %s INTEGER);", FOOD_TABLE, KEY_ID, DATE, FOOD_ITEM, CALORIES);

    public final static String EXERCISE_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DATETIME, %s TEXT, %s INTEGER, %s REAL);", EXERCISE_TABLE, KEY_ID, DATE, EXERCISE_NAME, CALORIES, EXERCISE_DURATION);

    //public final static String MEALPLAN_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DATETIME, %s INTEGER);", SLEEP_TABLE, KEY_ID, COL_DATE, HOURS_SLEPT);

    //DB name
    public final static String DATABASE_NAME = "Wellness.db";
    //Version
    static int VERSION_NUM = 2;

    public AppDBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL(SLEEP_QUERY);
        db.execSQL(FOOD_QUERY);
        db.execSQL(EXERCISE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }

    public void insertRecord(SQLiteDatabase db, String tableName, String columnName, String data) {
        Log.i(ACTIVITY_NAME, "Inserting record into db");
        ContentValues cValues = new ContentValues();
        cValues.put(columnName, data);
        db.insert(tableName, "NullPlaceHolder", cValues);
    }

    public void deleteRecord(SQLiteDatabase db, String tableName, String columName) {
        Log.i(ACTIVITY_NAME, "Deleting record from db;");
        //db.delete(tableName, dbHelper.KEY_ID + "=" + String.valueOf(idToDelete), null);
    }
}