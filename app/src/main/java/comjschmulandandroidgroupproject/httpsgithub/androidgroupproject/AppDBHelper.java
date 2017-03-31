package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;

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
    public final static String FOOD_EATEN_TABLE = "FOOD_EATEN";
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
    public final static String FOOD_EATEN_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s DATETIME, %s TEXT, %s INTEGER);", FOOD_EATEN_TABLE, KEY_ID, DATE, FOOD_ITEM, CALORIES);
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
        db.execSQL(FOOD_EATEN_QUERY);
        db.execSQL(EXERCISE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
    }

    public ArrayList<Sleep> getAllSleep() {
        Log.i(ACTIVITY_NAME, "Called getAllSleep()");
        ArrayList<Sleep> sleepList = new ArrayList<Sleep>();
        String selectAll = "SELECT * FROM " + SLEEP_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectAll, null);

        DateFormat df = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        Date date = null;
        int id = 0;
        int duration = 0;

        if(c.moveToFirst()) {
            do {

                id = c.getInt(c.getColumnIndex(KEY_ID));
                duration = c.getInt(c.getColumnIndex(HOURS_SLEPT));

                try {
                    date = df.parse(c.getString(c.getColumnIndex(DATE)));
                } catch (ParseException e) {
                    Log.e("AppDBHelper", "PARSE ERROR");
                }

                Sleep sleep = new Sleep(id, date, duration);
            } while(c.moveToNext());
        }

        return sleepList;
    }

    public ArrayList<FoodEaten> getAllFoodEaten() {
        Log.i(ACTIVITY_NAME, "Called getAllFoodEaten()");
        ArrayList<FoodEaten> foodEatenList = new ArrayList<FoodEaten>();
        String selectAll = "SELECT * FROM " + FOOD_EATEN_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectAll, null);

        DateFormat df = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        Date date = null;
        int id = 0;
        int calories = 0;
        String foodName = "";

        if(c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(KEY_ID));
                calories = c.getInt(c.getColumnIndex(CALORIES));
                foodName = c.getString(c.getColumnIndex(FOOD_ITEM));

//                try {
//                    date = df.parse(c.getString(c.getColumnIndex(DATE)));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                date = new Date(1234567891);
                FoodEaten food = new FoodEaten(id, foodName, calories, date);
                foodEatenList.add(food);
            } while(c.moveToNext());
        }

        return foodEatenList;
    }

    public boolean insertFoodEaten(FoodEaten foodEaten){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FOOD_ITEM, foodEaten.getFoodName());
        values.put(CALORIES, foodEaten.getCalories());
        values.put(DATE, foodEaten.getDate().toString());

        if(db.insert(FOOD_EATEN_TABLE, null, values) >= 0) {
            return true;
        }
        return false;

    }
}