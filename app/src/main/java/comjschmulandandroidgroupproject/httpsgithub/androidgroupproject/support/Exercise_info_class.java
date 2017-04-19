package comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.support;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.AppDBHelper;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.FoodTracker;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.MainActivity;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.MealPlanner;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.R;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.SleepTracker;
import comjschmulandandroidgroupproject.httpsgithub.androidgroupproject.models.ExerciseRecords;

/**
 * Created by carlo on 4/5/2017. Exercise results Class for the Exercise Activity
 */
public class Exercise_info_class extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "Exercise Info";
    protected TextView exerciseInfoView, exerciseNameView, exerciseTimeView, totalCalories, textViewDate;
    protected Button exerciseDataBase;
    protected String currentDateTimeString, passedName, passedInfo, dateSubstring;
    protected double timeInExercise, calPerMinute, totalCaloriesDouble;
    protected Context ctx;
    protected ProgressBar bar;
    boolean isTablet;
    protected ListView listView;
    ArrayList<ExerciseRecords> exerciseObjArray = new ArrayList<>();
    ExerciseAdapter exerciseAdapter;
    AppDBHelper dbHelper;
    SQLiteDatabase db;
    ExerciseAdapter messageAdapter;


    /**
     * onCreate method
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_info_class);
        ctx = this;
        exerciseAdapter = new ExerciseAdapter(this);    //set the ExerciseAdapter
        dbHelper = new AppDBHelper(this);              //set the database helper
        db = dbHelper.getWritableDatabase();           //set the SQLiteDatabase


        /*--------------------receiving values from the ExerciseRecords Class-----------*/
        passedName = getIntent().getStringExtra("exerciseName");
        passedInfo = getIntent().getStringExtra("exerciseCal");
        timeInExercise = getIntent().getDoubleExtra("setTime", timeInExercise);
        calPerMinute = Double.parseDouble(passedInfo);

        /*-----------------find all TextViews references----------------*/
        exerciseInfoView = (TextView) findViewById(R.id.textViewExerciseInfo);
        exerciseNameView = (TextView) findViewById(R.id.textViewExerciseName);
        exerciseTimeView = (TextView) findViewById(R.id.textViewTimeInExercise);
        textViewDate = (TextView) findViewById(R.id.textViewDate);
        totalCalories = (TextView) findViewById(R.id.textViewTotalCalories);


        /*------create a new ExerciseQuery object and execute-----*/
        ExerciseQuery eq = new ExerciseQuery();
        eq.execute(this);

        isTablet = (findViewById(R.id.exerciseFrameLayout) != null); //find out if this is a phone or tablet

        /*-----------------LisTView UPDATE----------------------*/
        listView = (ListView) findViewById(R.id.listViewExercise);
        messageAdapter = new ExerciseAdapter(this);
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //set the messageAdapter to the exerciseRecords object
                ExerciseRecords exerciseObj = messageAdapter.getItem(position);

                //create a new bundle object
                Bundle bun = new Bundle();

                //set the values to the bundle object
                bun.putInt("_ID", exerciseObj.getId());
                bun.putInt("arrayPosition", position);
                bun.putString("DATE", exerciseObj.getDate());
                bun.putString("NAME", exerciseObj.getExerciseName());
                bun.putDouble("CALORIES", exerciseObj.getCalories());
                bun.putDouble("DURATION", exerciseObj.getDuration());

                if (isTablet) {
                    //if is a tablet, create a ExerciseFragment object and set the arguments to it
                    ExerciseFragment frag = new ExerciseFragment();
                    frag.setArguments(bun);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.exerciseFrameLayout, frag).addToBackStack("_ID").commit();

                } else //isPhone
                {
                    //if it is a phone, create a new intent and put the values to it
                    Intent intent = new Intent(Exercise_info_class.this, ExerciseMessageDetails.class);
                    intent.putExtra("_ID", exerciseObj.getId()); //pass the Database ID to next activity
                    intent.putExtra("arrayPosition", position);
                    intent.putExtra("DATE", exerciseObj.getDate());
                    intent.putExtra("NAME", exerciseObj.getExerciseName());
                    intent.putExtra("CALORIES", exerciseObj.getCalories());
                    intent.putExtra("DURATION", exerciseObj.getDuration());
                    startActivityForResult(intent, 1, bun);
                }

            }
        });


        /*--------------------Set values to the TextViews-------------------*/
        exerciseNameView.setText(passedName);
        exerciseInfoView.setText(getString(R.string.caloriesPerMinute) + " " + passedInfo);
        exerciseTimeView.setText(getString(R.string.timeInExercise) + " " + timeInExercise + ": " + getString(R.string.exerciseMinutes));
        totalCalories.setText(getString(R.string.exerciseBurn) + ": " + calculateCalories(calPerMinute, timeInExercise) + " Calories");


        /*----------------------DATE----------------------*/
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Toast.makeText(getApplicationContext(), currentDateTimeString, Toast.LENGTH_LONG).show();
        dateSubstring = currentDateTimeString.substring(0, 12);
        textViewDate.setText(dateSubstring);


        /*--------------------Set action for the send Button-----------------------*/
        exerciseDataBase = (Button) findViewById(R.id.exerciseButtonSend);
        exerciseDataBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder secondBuilder = new AlertDialog.Builder(ctx);
                // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                 secondBuilder.setView(inflater.inflate(R.layout.exercise_message_dialog, null))

                        // Add action buttons
                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //new ExerciseRecords with parameters
                                ExerciseRecords setCurrentExercise = new ExerciseRecords(passedName, timeInExercise, dateSubstring, totalCaloriesDouble);
                                //new Exercise insert object and execute it
                                ExerciseInsert ei = new ExerciseInsert();
                                ei.execute(setCurrentExercise);

                                //add the exerciseInsert object to the array
                                exerciseObjArray.add(setCurrentExercise);
                                messageAdapter.notifyDataSetChanged();//update the listView

                                //create a snackBar to show the message
                                Snackbar.make(v, getString(R.string.exerciseSavingDatabase), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //new snackBar saying you canceled the action
                                Snackbar.make(v, getString(R.string.exerciseDialogCancel), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }
                        });

                secondBuilder.show();
            }
        });

        //set the progressBar, the max values and set the progress using the messageAdapter
        bar = (ProgressBar) findViewById(R.id.progressBarExercise);
        bar.setMax(100);
        bar.setProgress(messageAdapter.getCount());

        setTitle(getString(R.string.exercise)); //set the activity title

    }//end onCreate

    /**
     * Method used to return the total calories burned in exercise
     *
     * @param calPerMinute   receive the calories per minute
     * @param timeInExercise receives the total time in exercises
     * @return totalCalories return the total calories burned
     */
    public double calculateCalories(double calPerMinute, double timeInExercise) {

        totalCaloriesDouble = calPerMinute * timeInExercise;
        return totalCaloriesDouble;
    }//end calculateCalories method

    /**
     * method created to create an Option Menu
     * @param menu object
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.ft_toolbar, menu);
        MenuItem exerciseItem = menu.findItem(R.id.action_exercise);
        exerciseItem.setVisible(false);
        return true;
    }//end onCreateOptionsMenu

    /**
     *
     * @param item object
     * @return true or false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent; //new intent file
        switch (item.getItemId()) {
            case (R.id.action_foodtracker):
                intent = new Intent(Exercise_info_class.this, FoodTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_mealplanner):
                intent = new Intent(Exercise_info_class.this, MealPlanner.class);
                startActivity(intent);
                return true;
            case (R.id.action_sleep):
                intent = new Intent(Exercise_info_class.this, SleepTracker.class);
                startActivity(intent);
                return true;
            case (R.id.action_home):
                intent = new Intent(Exercise_info_class.this, MainActivity.class);
                startActivity(intent);
                return true;
            case (R.id.action_help):
                Log.i(ACTIVITY_NAME, "help");
                createHelpDialog();
                return true;
        }

        return false;

    }//end onOptionSelectedMenu

    /**
     * method to create a new dialog
     */
    public void createHelpDialog() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exercise_info_help_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder2.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.confirm_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        //new AlertDialog object created using the builder
        AlertDialog dialog2 = builder2.create();
        dialog2.setTitle(R.string.exercise_help_title); //set the dialog title
        dialog2.show(); //show the dialog
    }//end createHelpDialog

    /**
     * private class to insert the values to the database
     */
    private class ExerciseInsert extends AsyncTask<ExerciseRecords, Integer, String> {
        @Override
        protected String doInBackground(ExerciseRecords... args) {

            //send the values to the AppDBHelper class
            dbHelper.insertExerciseSession(args[0]);//update the database
            dbHelper.close();
            return "done";
        }//end ExerciseInsert()

        protected void onPostExecute(String args) {
            //notify user of constraints
            Context ct = getApplicationContext();
            CharSequence text = getString(R.string.exerciseAdded);
            int d2 = Toast.LENGTH_LONG;
            Toast t = Toast.makeText(ct, text, d2);
            t.show();
        }
    }//end ExerciseInsert

    /**
     * Class created to populate the exercise ArrayList
     */
    private class ExerciseQuery extends AsyncTask<Context, Integer, String> {

        @Override
        protected String doInBackground(Context... args) {

            //get all information from the dataBase
            exerciseObjArray = dbHelper.getAllExerciseRecords();
            dbHelper.close();

            return "done";
        }

    }//end ExerciseQuery

    /**
     * private class that extends the ArrayAdapter
     */
    private class ExerciseAdapter extends ArrayAdapter<ExerciseRecords> {

        /**
         *  constructor
         * @param ctx object
         */
        private ExerciseAdapter(Context ctx) {
            super(ctx, 0);
        }

        /**
         * get the array size
         * @return the exerciseObjArray size
         */
        @Override
        public int getCount() {
            return exerciseObjArray.size();
        }

        /**
         * get the object position
         * @param position object
         * @return the exerciseObjArray position
         */
        @Override
        public ExerciseRecords getItem(int position) {
            return exerciseObjArray.get(position);
        }


        /**
         *  get view, set items, return view
         * @param position object
         * @param convertView object
         * @param parent object
         * @return resultView
         */
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //new ExerciseRecords object getting the item position
            ExerciseRecords tempExercise = getItem(position);

            //inflate and view the row layout
            LayoutInflater inflater = Exercise_info_class.this.getLayoutInflater();
            View resultView = inflater.inflate(R.layout.exercise_row_layout, null);

            //create a viewDate TextView and set a text to it
            TextView viewDate = (TextView) resultView.findViewById(R.id.exerciseRowDate);
            viewDate.setText(tempExercise.getDate());

            //create a viewName TextView and set a text to it
            TextView viewName = (TextView) resultView.findViewById(R.id.exerciseRowName);
            viewName.setText(tempExercise.getExerciseName()); // get the string at position

            //create a viewDuration TextView and set a text to it
            TextView viewDuration = (TextView) resultView.findViewById(R.id.exerciseRowDuration);
            String tempDuration = String.valueOf(tempExercise.getDuration());
            viewDuration.setText(tempDuration + " " + getString(R.string.exerciseMinutes)); // get the string at position

            //create a viewCalories TextView and set a text to it
            TextView viewCalories = (TextView) resultView.findViewById(R.id.exerciseRowCalories);
            String tempCalories = String.valueOf(tempExercise.getCalories());
            viewCalories.setText(tempCalories + " " + getString(R.string.calories)); // get the string at position

            return resultView; //return the resultView
        }
    }//end ExerciseAdapter

    /**
     *
     * @param requestCode object
     * @param resultCode object
     * @param data object
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bun = data.getExtras();
                int arrayIndex = bun.getInt("arrayPosition");
                int dbKey = bun.getInt("_ID");
                deleteExerciseFromDb(arrayIndex, dbKey);
            }
        }
    }//end onActivityResult()

    /**
     * delete the exercise from the array and ask the dataBase to delete the information
     * @param arrayIndex object
     * @param dbKey object
     */
    public void deleteExerciseFromDb(int arrayIndex, int dbKey) {

        exerciseObjArray.remove(arrayIndex);
        dbHelper.deleteExerciseRecords(dbKey);
        messageAdapter.notifyDataSetChanged();
    }//end DeleteExerciseFromDb()

    /**
     * close the dataBase
     */
    public void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }//end OnDestroy()

}//end Class
