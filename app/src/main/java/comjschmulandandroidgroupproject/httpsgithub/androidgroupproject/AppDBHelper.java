package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.ExerciseRecords;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.FoodEaten;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.Sleep;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support.Exercise_info_class;


public class AppDBHelper extends SQLiteOpenHelper {
    //Activity name
    private final static String ACTIVITY_NAME = "AppDBHelper";
    //Table names
    private final static String MEALPLAN_TABLE = "MEALPLAN";
    private final static String MEALS_TABLE = "MEALS";
    private final static String MEALPLAN_HAS_MEALS = "MEALPLAN_HAS_MEALS";
    private final static String MEALS_HAS_FOOD = "MEALS_HAS_FOOD";
    public final static String EXERCISE_TABLE = "EXERCISE";
    protected final static String SLEEP_TABLE = "SLEEP";
    private final static String FOOD_EATEN_TABLE = "FOOD_EATEN";
    private final static String FOOD_TABLE = "FOOD";
    //Common columns
    public final static String KEY_ID = "_ID";
    protected final static String DATE = "DATE";
    private final static String CALORIES = "CALORIES";
    private final static String FOOD_ITEM = "FOOD_ITEM";
    private final static String KEY_MEAL_ID = "MEAL_ID";
    private final static String KEY_FOOD_ID = "FOOD_ID";
    private final static String KEY_MP_ID = "MEALPLAN_ID";
    private final static String DESCRIPTION = "DESRIPTION";
    //Sleep table columns
    protected final static String HOURS_SLEPT = "HOURS_SLEPT";
    //Meal table columns
    private final static String MEAL_NAME = "MEAL_NAME";
    //ExerciseRecords table columns
    private final static String EXERCISE_NAME = "EXERCISE_NAME";
    private final static String EXERCISE_DURATION = "DURATION";
    //Meal Plan table columns
    private final static String MEALPLAN_NAME = "MEALPLAN_NAME";
    //Create table queries
    private final static String SLEEP_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s INTEGER);", SLEEP_TABLE, KEY_ID, DATE, HOURS_SLEPT);
    private final static String FOOD_EATEN_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER, %s TEXT, %s INTEGER, %s TEXT);", FOOD_EATEN_TABLE, KEY_ID, DATE, FOOD_ITEM, CALORIES, DESCRIPTION);
    private final static String EXERCISE_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s REAL, %s REAL);", EXERCISE_TABLE, KEY_ID, DATE, EXERCISE_NAME, CALORIES, EXERCISE_DURATION);
    private final static String FOOD_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER);", FOOD_TABLE, KEY_ID, FOOD_ITEM, CALORIES);
    private final static String MEALS_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT);", MEALS_TABLE, KEY_ID, MEAL_NAME);
    private final static String MEALPLAN_QUERY = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT);", MEALPLAN_TABLE, KEY_ID, MEALPLAN_NAME);
    //Associative tables create
    private final static String MS_HAS_FOOD_QUERY = "CREATE TABLE " + MEALS_HAS_FOOD + " (" + KEY_ID +
    "INTEGER PRIMARY KEY," + KEY_FOOD_ID + " INTEGER, " + KEY_MEAL_ID + " INTEGER, FOREIGN KEY (" + KEY_FOOD_ID + ") REFERENCES " +FOOD_TABLE + " ("+KEY_ID+")," +
            "FOREIGN KEY (" + KEY_MEAL_ID +") REFERENCES " +MEALS_TABLE + " (" +KEY_ID+"))";
    private final static String MP_HAS_MEALS_QUERY = "CREATE TABLE " + MEALPLAN_HAS_MEALS + " (" + KEY_ID +
            "INTEGER PRIMARY KEY," + KEY_MP_ID + " INTEGER, " + KEY_MEAL_ID + " INTEGER, FOREIGN KEY (" + KEY_MP_ID + ") REFERENCES " +MEALPLAN_TABLE + " ("+KEY_ID+")," +
            "FOREIGN KEY (" + KEY_MEAL_ID +") REFERENCES " +MEALS_TABLE + " (" +KEY_ID+"))";
    //DB name
    public final static String DATABASE_NAME = "Wellness.db";
    //Version
    static int VERSION_NUM = 6;

    public AppDBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(ACTIVITY_NAME, "Calling onCreate");
        db.execSQL(SLEEP_QUERY);
        db.execSQL(FOOD_EATEN_QUERY);
        db.execSQL(EXERCISE_QUERY);
        db.execSQL(FOOD_QUERY);
        db.execSQL(MEALS_QUERY);
        db.execSQL(MS_HAS_FOOD_QUERY);
        db.execSQL(MEALPLAN_QUERY);
        db.execSQL(MP_HAS_MEALS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion=" + newVersion);
        String drop = "DROP TABLE IF EXISTS ";
        db.execSQL(drop + FOOD_EATEN_TABLE);
        db.execSQL(drop + SLEEP_TABLE);
        db.execSQL(drop + EXERCISE_TABLE);
        db.execSQL(drop + MEALS_HAS_FOOD);
        db.execSQL(drop + MEALPLAN_HAS_MEALS);
        db.execSQL(drop + FOOD_TABLE);
        db.execSQL(drop + MEALS_TABLE);
        db.execSQL(drop + MEALPLAN_TABLE);
        onCreate(db);
    }

    public ArrayList<Sleep> getAllSleep() {
        Log.i(ACTIVITY_NAME, "Called getAllSleep()");
        ArrayList<Sleep> sleepList = new ArrayList<Sleep>();
        String selectAll = "SELECT * FROM " + SLEEP_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectAll, null);

        Date date = null;
        long dateLong = 0;
        int id = 0;
        int duration = 0;

        if (c.moveToFirst()) {
            do {

                id = c.getInt(c.getColumnIndex(KEY_ID));
                duration = c.getInt(c.getColumnIndex(HOURS_SLEPT));
                dateLong = c.getLong(c.getColumnIndex(DATE));

                date = new Date(dateLong);
                Sleep sleep = new Sleep(id, date, duration);
                sleepList.add(sleep);
            } while (c.moveToNext());
        }

        return sleepList;
    }

    public boolean insertSleepSession(Sleep sleepSession){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HOURS_SLEPT,sleepSession.getDuration());
        values.put(DATE,sleepSession.getDate().getTime());

        if(db.insert(SLEEP_TABLE, null, values) >= 0){
            db.close();
            return true;
        }

        db.close();//closing resources
        return false;
    }

    public ArrayList<FoodEaten> getAllFoodEaten() {
        Log.i(ACTIVITY_NAME, "Called getAllFoodEaten()");
        ArrayList<FoodEaten> foodEatenList = new ArrayList<FoodEaten>();
        String selectAll = "SELECT * FROM " + FOOD_EATEN_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectAll, null);

        Date date;
        long dateLong;
        int id;
        int calories;
        String foodName;
        String description;

        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(KEY_ID));
                calories = c.getInt(c.getColumnIndex(CALORIES));
                foodName = c.getString(c.getColumnIndex(FOOD_ITEM));
                dateLong = c.getLong(c.getColumnIndex(DATE));
                description = c.getString((c.getColumnIndex(DESCRIPTION)));

                date = new Date(dateLong);
                FoodEaten food = new FoodEaten(id, foodName, calories, date, description);
                foodEatenList.add(food);
            } while (c.moveToNext());
        }

        Collections.reverse(foodEatenList);

        return foodEatenList;
    }

    public boolean insertFoodEaten(FoodEaten foodEaten) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FOOD_ITEM, foodEaten.getFoodName());
        values.put(CALORIES, foodEaten.getCalories());
        values.put(DATE, foodEaten.getDate().getTime());
        values.put(DESCRIPTION, foodEaten.getDescription());

        if (db.insert(FOOD_EATEN_TABLE, null, values) >= 0) {
            return true;
        }
        return false;

    }

    public boolean deleteFoodEaten(FoodEaten foodEaten){
        SQLiteDatabase db = this.getWritableDatabase();

        if(db.delete(FOOD_EATEN_TABLE, KEY_ID + "=" + String.valueOf(foodEaten.getId()), null) > 0){
            return true;
        }
        return false;

    }

    public ArrayList<ExerciseRecords> getAllExerciseRecords() {
        Log.i(ACTIVITY_NAME, "Called getAllExerciseRecords()");
        ArrayList<ExerciseRecords> exerciseList = new ArrayList<>();
        String selectAll = "SELECT * FROM " + EXERCISE_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectAll, null);

        String date;
        int id;
        double duration;
        double calories;
        String exerciseName;

        if (c.moveToFirst()) {
            do {

                id = c.getInt(c.getColumnIndex(KEY_ID));
                date = c.getString(c.getColumnIndex(DATE));
                exerciseName = c.getString(c.getColumnIndex(EXERCISE_NAME));
                calories = c.getDouble(c.getColumnIndex(CALORIES));
                duration = c.getDouble(c.getColumnIndex(EXERCISE_DURATION));

                ExerciseRecords exercise = new ExerciseRecords(id, exerciseName, duration, date, calories);
                exerciseList.add(exercise);
            } while (c.moveToNext());
        }

        return exerciseList;
    }

    public boolean insertExerciseSession(ExerciseRecords exerciseSession){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DATE,exerciseSession.getDate());
        values.put(EXERCISE_NAME, exerciseSession.getExerciseName());
        values.put(CALORIES, exerciseSession.getCalories());
        values.put(EXERCISE_DURATION,exerciseSession.getDuration());

        if(db.insert(EXERCISE_TABLE, null, values) >= 0){
            db.close();
            return true;
        }

        db.close();//closing resources
        return false;
    }


    public void deleteExerciseRecords(int key){

        SQLiteDatabase db = this.getWritableDatabase();
        String tempKey = String.valueOf(key);
        db.delete(EXERCISE_TABLE, KEY_ID + "=" + tempKey, null);

    }

}